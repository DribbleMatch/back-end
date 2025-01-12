package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.FileUtil;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamDetailResponseDto;
import com.sideProject.DribbleMatch.dto.user.response.UserTeamResponseDto;
import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamTag;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.teamMatchJoin.TeamMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.repository.teamMember.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TeamServiceImpl implements TeamService{

    @Value("${spring.dir.teamImagePath}")
    public String path;

    private final FileUtil fileUtil;

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    @Override
    @Transactional
    public Long createTeam(Long creatorId, TeamCreateRequestDto request) {

        checkTeamName(request.getName());

        // 팀 생성 시에는 팀 생성한 회원이 팀장, 팀 정보 수정에서 변경 가능
        User creator = userRepository.findById(creatorId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));
        Region region = regionRepository.findByRegionString(request.getRegionString()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));

        return teamRepository.save(Team.builder()
                        .name(request.getName())
                        .winning(0)
                        .maxNumber(request.getMaxNum())
                        .info(request.getInfo())
                        .tags(request.getTags())
                        .imagePath(request.getImage() == null ? path + File.separator + "team_default_image.png" : fileUtil.saveImage(request.getImage(), path, request.getName()))
                        .leader(creator)
                        .region(region)
                .build()).getId();
    }

    @Override
    public TeamDetailResponseDto getTeamDetail(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM));

        List<TeamMember> teamMemberList = teamMemberRepository.findByTeamId(teamId);
        List<User> userList = teamMemberList.stream()
                .map(TeamMember::getUser)  // TeamMember에서 User 추출
                .toList();
        List<UserTeamResponseDto> userTeamResponseList = userList.stream()
                .map(UserTeamResponseDto::of)  // User 객체를 UserTeamResponseDto로 변환
                .collect(Collectors.toList());

        return TeamDetailResponseDto.builder()
                .id(team.getId())
                .imagePath(team.getImagePath())
                .name(team.getName())
                .maxNum(team.getMaxNumber())
                .winningPercent(calculateWinPercent(team))
                .leaderNickName(team.getLeader().getNickName())
                .regionString(team.getRegion().getSiDo() + " " + team.getRegion().getSiGunGu())
                .info(team.getInfo())
                .tags(convertStringToTeamTagList(team.getTags()))
                .userList(userTeamResponseList)
                .build();
    }

    @Override
    public void checkTeamName(String name) {
        if(teamRepository.findByName(name).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_TEAM_NAME);
        }
    }

    @Override
    public Page<TeamListResponseDto> searchTeamsByUserId(Long userId, Pageable pageable) {
        Page<TeamMember> teamMemberPage = teamMemberRepository.findByUserId(userId, pageable);

        List<TeamListResponseDto> responseList = teamMemberPage.stream()
                .map(teamMember -> TeamListResponseDto.builder()
                        .id(teamMember.getTeam().getId())
                        .imagePath(teamMember.getTeam().getImagePath())
                        .name(teamMember.getTeam().getName())
                        .regionString(teamMember.getTeam().getRegion().getSiDo() + " " + teamMember.getTeam().getRegion().getSiGunGu())
                        .memberNumString(teamMemberRepository.findByTeamId(teamMember.getTeam().getId()).size() + "명 / " + teamMember.getTeam().getMaxNumber() +"명")
                        .winningPercent(calculateWinPercent(teamMember.getTeam()))
                        .build())
                .toList();

        return new PageImpl<>(responseList, pageable, teamMemberPage.getTotalElements());
    }

    @Override
    public Page<TeamListResponseDto> searchTeamsBySearchWord(String searchWord, Pageable pageable) {
        Page<Team> teamPage = teamRepository.findBySearch(searchWord, pageable);

        List<TeamListResponseDto> responseList = teamPage.stream()
                .map(team -> TeamListResponseDto.builder()
                        .id(team.getId())
                        .imagePath(team.getImagePath())
                        .name(team.getName())
                        .regionString(team.getRegion().getSiDo() + " " + team.getRegion().getSiGunGu())
                        .memberNumString(teamMemberRepository.findByTeamId(team.getId()).size() + "명 / " + team.getMaxNumber() +"명")
                        .winningPercent(calculateWinPercent(team))
                        .build())
                .toList();

        return new PageImpl<>(responseList, pageable, teamPage.getTotalElements());
    }

    private List<TeamTag> convertStringToTeamTagList(String tags) {
        return Arrays.stream(tags.split(","))
                .map(String::trim)
                .map(TeamTag::valueOf)
                .collect(Collectors.toList());
    }

    private double calculateWinPercent(Team team) {
        //todo: 승률 계산 시 종료된 경기만 포함하도록 수정
//        int entireGame = teamMatchJoinRepository.countTeamMatchJoinByTeam(team);
        int entireGame = 0;
        return entireGame == 0 ? 0 : Double.parseDouble(String.format("%.1f", (double) team.getWinning() / entireGame * 100));
    }
}

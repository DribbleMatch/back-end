package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.entity.userTeam.UserTeam;
import com.sideProject.DribbleMatch.repository.userTeam.UserTeamRepository;
import com.sideProject.DribbleMatch.service.region.RegionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final UserTeamRepository userTeamRepository;
    private final UserRepository userRepository;
    private final RegionService regionService;

    @Override
    public Long createTeam(Long creatorId, TeamCreateRequestDto request) {

        checkUniqueName(request.getName());

        // 팀 생성 시에는 팀 생성한 회원이 팀장, 팀 정보 수정에서 변경 가능
        User creator = userRepository.findById(creatorId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        Region region = regionService.findRegion(request.getRegionString());

        Team team = teamRepository.save(Team.builder()
                        .name(request.getName())
                        .winning(0)
                        .leader(creator)
                        .region(region)
                        .build());

        userTeamRepository.save(UserTeam.builder()
                .team(team)
                .user(creator)
                .build());

        return team.getId();
    }

    @Override
    public Long updateTeam(Long teamId, TeamUpdateRequestDto request) {

        checkUniqueName(request.getName());

        Region region = regionService.findRegion(request.getRegionString());
        User leader = userRepository.findById(request.getLeaderId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team teamToUpdate = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        teamToUpdate.updateTeam(request.getName(), leader, region);

        //todo: 변경된 리더가 팀원이 아닐 경우 에러 발생 처리
        return teamId;
    }

    @Override
    public String deleteTeam(Long teamId) {

        teamRepository.deleteById(teamId);

        return "팀이 삭제되었습니다.";
    }

    @Override
    @Transactional(readOnly = true)
    public Page<TeamResponseDto> findAllTeams(Pageable pageable, String regionString) {

        if (regionString.equals("ALL")) {
            Page<Team> teams = teamRepository.findAll(pageable);
            return teams
                    .map(team -> TeamResponseDto.of(team, regionService.findRegionString(team.getRegion().getId())));
        }

        List<Long> regionIds = regionService.findRegionIds(regionString);
        Page<Team> teams = teamRepository.findByRegionIds(pageable, regionIds);
        return teams
                .map(team -> TeamResponseDto.of(team, regionService.findRegionString(team.getRegion().getId())));
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponseDto findTeam(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        return TeamResponseDto.of(team, regionService.findRegionString(team.getRegion().getId()));
    }

    private void checkUniqueName(String name) {
        if(teamRepository.findByName(name).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_TEAM_NAME);
        }
    }
}

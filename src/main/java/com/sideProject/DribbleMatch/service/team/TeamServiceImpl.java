package com.sideProject.DribbleMatch.service.team;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.team.request.TeamCreateRequestDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamJoinRequestDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamMemberResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.dto.team.request.TeamUpdateRequestDto;
import com.sideProject.DribbleMatch.entity.joinTeam.TeamJoin;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.teamJoin.TeamJoinRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamServiceImpl implements TeamService{

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    // todo: 분리할 만한 로직?
    private final TeamJoinRepository teamJoinRepository;

    @Override
    public Long createTeam(Long creatorId, TeamCreateRequestDto request) {

        checkUniqueName(request.getName());

        // 팀 생성 시에는 팀 생성한 회원이 팀장, 팀 정보 수정에서 변경 가능
        User creator = userRepository.findById(creatorId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Region region = regionRepository.findByRegionString(request.getRegionString()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));

        Team team = teamRepository.save(TeamCreateRequestDto.toEntity(request, creator, region));

        // todo: userTeam을 만드는 로직을 이 메서드에서 담당하는게 맞을까?
        teamMemberRepository.save(TeamMember.builder()
                .team(team)
                .user(creator)
                .build());

        return team.getId();
    }

    @Override
    public Long updateTeam(Long teamId, TeamUpdateRequestDto request) {

        checkUniqueName(request.getName());

        Region region = regionRepository.findByRegionString(request.getRegionString()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
        User leader = userRepository.findById(request.getLeaderId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team teamToUpdate = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        teamToUpdate.updateTeam(request, leader, region);

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
                    .map(team -> TeamResponseDto.of(team, regionRepository.findRegionStringById(team.getRegion().getId()).orElseThrow(() ->
                            new CustomException(ErrorCode.NOT_FOUND_REGION_ID))));
        }

        List<Long> regionIds = regionRepository.findIdsByRegionString(regionString);
        Page<Team> teams = teamRepository.findByRegionIds(pageable, regionIds);
        return teams
                .map(team -> TeamResponseDto.of(team, regionRepository.findRegionStringById(team.getRegion().getId()).orElseThrow(() ->
                        new CustomException(ErrorCode.NOT_FOUND_REGION_ID))));
    }

    @Override
    @Transactional(readOnly = true)
    public TeamResponseDto findTeam(Long teamId) {

        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        return TeamResponseDto.of(team, regionRepository.findRegionStringById(team.getRegion().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_ID)));
    }

    @Override
    public Long join(TeamJoinRequestDto request, Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));
        Team team = teamRepository.findById(request.getTeamId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID));

        Optional<TeamMember> teamMember = teamMemberRepository.findByUserAndTeam(user,team);
        if(teamMember.isPresent()) {
            throw  new CustomException(ErrorCode.ALREADY_MEMBER);
        }

        TeamJoin teamJoin = teamJoinRepository.save(TeamJoin.builder()
                        .team(team)
                        .user(user)
                        .introduce(request.getIntroduce())
                        .build()
        );
        return teamJoin.getId();
    }

    @Override
    public Long cancel(Long joinId, Long userId) {
        return null;
    }

    @Override
    public Long approve(Long joinId, Long userId) {


        return null;
    }

    @Override
    public Long refuse(Long joinId, Long userId) {
        return null;
    }

    @Override
    public Long withdraw(Long memberId, Long teamId, Long userId) {
        return null;
    }

    @Override
    public List<TeamMemberResponseDto> findMember(Long teamId) {
        return null;
    }

    private void checkUniqueName(String name) {
        if(teamRepository.findByName(name).isPresent()) {
            throw new CustomException(ErrorCode.NOT_UNIQUE_TEAM_NAME);
        }
    }
}

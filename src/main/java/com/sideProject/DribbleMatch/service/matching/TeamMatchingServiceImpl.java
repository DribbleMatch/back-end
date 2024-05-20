package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.TeamMatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.TeamMatchingResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamMemberResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamResponseDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.TeamMatch;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.matching.TeamMatchingRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamMatchJoin.TeamMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class TeamMatchingServiceImpl implements TeamMatchingService {

    private final TeamMatchingRepository teamMatchRepository;
    private final TeamMatchJoinRepository teamMatchJoinRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;

    //todo: 1시간 이내에 예약된 매치가 있으면 안되게 수정
    @Override
    public Long createMatching(TeamMatchingCreateRequestDto request, Long teamId, Long userId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
        );

        //유저 정보 조회
        User user = userRepository.findById(userId).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_USER_ID)
        );
        TeamMember teamMember = teamMemberRepository.findByUserAndTeam(user,team).orElseThrow(() ->
            new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER)
        );
        if(!teamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        Region region = regionRepository.findByRegionString(request.getRegionString()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));

        //
        TeamMatch teamMatch = teamMatchRepository.save(request.toEntity(region,team));
        teamMatchJoinRepository.save(
            new TeamMatchJoin(
                team,
                teamMatch
            )
        );

        return teamMatch.getId();
    }

    @Override
    public Long updateMatching(TeamMatchingUpdateRequestDto request, Long matchId, Long userId) {
        TeamMatch teamMatch = teamMatchRepository.findById(matchId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MATCHING_ID)
        );

        //유저 정보 조회
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID)
        );
        TeamMember teamMember = teamMemberRepository.findByUserAndTeam(user,teamMatch.getHomeTeam()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER)
        );
        if(!teamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        Region region = regionRepository.findByRegionString(request.getRegionString()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));

        //
        teamMatch.update(request, region);
        teamMatchRepository.save(teamMatch);

        return teamMatch.getId();
    }

    @Override
    public Long deleteMatching(Long matchId, Long userId) {
        TeamMatch teamMatch = teamMatchRepository.findById(matchId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MATCHING_ID)
        );

        //팀 리더 정보 조회
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID)
        );
        TeamMember teamMember = teamMemberRepository.findByUserAndTeam(user,teamMatch.getHomeTeam()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER)
        );
        if(!teamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        //팀 매치에 등록한 팀 삭제
        List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findByTeamMatch(teamMatch);
        for(TeamMatchJoin teamMatchJoin: teamMatchJoins) {
            teamMatchJoinRepository.deleteById(teamMatchJoin.getId());
        }

        teamMatchRepository.deleteById(teamMatch.getId());

        return teamMatch.getId();
    }

    @Override
    public String joinMatching(Long matchId, Long teamId, Long userId) {
        TeamMatch teamMatch = teamMatchRepository.findById(matchId).orElseThrow(() ->
                new CustomException(ErrorCode.CANT_JOIN)
        );


        //팀 리더 정보 조회
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_ID)
        );
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID)
        );
        TeamMember teamMember = teamMemberRepository.findByUserAndTeam(user,team).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER)
        );
        if(!teamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        //팀 참여 여부 조회
        List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findByTeamMatch(teamMatch);
        if(!teamMatch.getStatus().equals(MatchingStatus.IN_PROGRESS) ||
                teamMatchJoins.size() >= teamMatch.getMaxTeam()
        ) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }
        for(TeamMatchJoin teamMatchJoin: teamMatchJoins) {
            if(Objects.equals(teamMatchJoin.getTeam().getId(), team.getId())) {
                throw new CustomException(ErrorCode.ALREADY_JOIN_TEAM);
            }
        }

        teamMatchJoinRepository.save(
                new TeamMatchJoin(
                        team,
                        teamMatch
                )
        );

        return "참가 신청이 완료되었습니다.";
    }

    @Override
    public TeamMatchingResponseDto findMatching(Long id) {
        TeamMatch teamMatch = teamMatchRepository.findById(id).orElseThrow(() ->
                new CustomException(ErrorCode.CANT_JOIN)
        );
        List<TeamMatchJoin> teamMatchJoins = teamMatchJoinRepository.findByTeamMatch(teamMatch);

        return TeamMatchingResponseDto.of(
                teamMatch,
                teamMatch.getRegion().getSiDo() + teamMatch.getRegion().getSiGunGu(),
                teamMatch.getStadium().getDetailAddress(),
                //todo: 리팩토링 대상?
                teamMatchJoins.stream().map((teamMatchJoin) -> {
                    Team team = teamMatchJoin.getTeam();
                    String region = team.getRegion().getSiDo() + team.getRegion().getSiGunGu();
                    return TeamResponseDto.of(team, region);
                }).collect(Collectors.toList())
        );
    }

    @Override
    public Page<TeamMatchingResponseDto> findMatching(Pageable pageable, String sido, LocalDateTime now) {
        Page<TeamMatch> teamMatches = teamMatchRepository.find(pageable, sido, now);
        return teamMatches.map((teamMatch) -> {
                    String region = teamMatch.getRegion().getSiDo() + teamMatch.getRegion().getSiGunGu();
                    return TeamMatchingResponseDto.of(
                            teamMatch,
                            region,
                            teamMatch.getStadium().getDetailAddress(),
                            new ArrayList<>()
                    );
                });
    }
}

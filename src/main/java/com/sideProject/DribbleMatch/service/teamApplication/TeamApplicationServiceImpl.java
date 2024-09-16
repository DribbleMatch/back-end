package com.sideProject.DribbleMatch.service.teamApplication;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.CommonUtil;
import com.sideProject.DribbleMatch.dto.teamApplication.TeamApplicationListResponseDto;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamApplication.ENUM.ApplicationStatus;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.teamApplication.TeamApplicationRepository;
import com.sideProject.DribbleMatch.repository.teamMember.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamApplicationServiceImpl implements TeamApplicationService{

    private final TeamApplicationRepository teamApplicationRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final TeamRepository teamRepository;

    @Override
    public void applyTeam(Long userId, Long teamId, String introduce) {

        // 이미 멤버인지 확인
        if (teamMemberRepository.findByUserIdAndTeamId(userId, teamId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_TEAM_MEMBER);
        }

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM));

        teamApplicationRepository.save(TeamApplication.builder()
                    .user(user)
                    .team(team)
                    .introduce(introduce)
                .build());
    }

    @Override
    public List<TeamApplication> findTeamApplicationsByUser(Long userId) {
        return teamApplicationRepository.findTeamApplicationByUserId(userId);
    }

    @Override
    public List<TeamApplicationListResponseDto> findTeamApplicationsByTeam(Long teamId) {

        List<TeamApplication> teamApplicationList = teamApplicationRepository.findTeamApplicationByTeamIdAndStatus(teamId, ApplicationStatus.WAIT);
        return teamApplicationList.stream()
                .map(teamApplication -> TeamApplicationListResponseDto.builder()
                        .id(teamApplication.getId())
                        .nickName(teamApplication.getUser().getNickName())
                        .positionString(teamApplication.getUser().getPositionString())
                        .level(CommonUtil.getLevel(teamApplication.getUser().getExperience()))
                        .introduce(teamApplication.getIntroduce())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public void changeTeamApplicationStatus(Long teamApplicationId, ApplicationStatus status) {

        TeamApplication teamApplication = teamApplicationRepository.findById(teamApplicationId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_APPLICATION));

        // 이미 멤버인지 확인
        if (teamMemberRepository.findByUserIdAndTeamId(teamApplication.getUser().getId(), teamApplication.getTeam().getId()).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_TEAM_MEMBER);
        }

        if (status.equals(ApplicationStatus.APPROVE)) {
            teamApplication.approve();

            User user = userRepository.findById(teamApplication.getUser().getId()).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_USER));
            Team team = teamRepository.findById(teamApplication.getTeam().getId()).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_TEAM));

            teamMemberRepository.save(TeamMember.builder()
                    .user(user)
                    .team(team)
                    .teamRole(TeamRole.MEMBER)
                    .build());

        } else if (status.equals(ApplicationStatus.REFUSE)) {
            teamApplication.refuse();
        }

        teamApplicationRepository.save(teamApplication);
    }

    @Override
    public void requestJoin(Long userId, Long teamId, String introduce) {

        checkTeamApplication(userId, teamId);

        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM));

        teamApplicationRepository.save(TeamApplication.builder()
                        .user(user)
                        .team(team)
                        .introduce(introduce)
                .build());
    }

    private void checkTeamApplication(Long userId, Long teamId) {

        // 이미 팀원일 때
        if (teamMemberRepository.findByUserIdAndTeamId(userId, teamId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_TEAM_MEMBER);
        }

        // 요청이 대기중일 때
        if (teamApplicationRepository.findTeamApplicationByUserIdAndTeamIdAndStatus(userId, teamId, ApplicationStatus.WAIT).isPresent()) {
            throw new CustomException(ErrorCode.WAITING_TEAM_APPLICATION);
        }

        // 요청이 거부된지 하루가 지나지 않았을 떄
        Optional<TeamApplication> teamApplication = teamApplicationRepository.findTeamApplicationByUserIdAndTeamIdAndStatus(userId, teamId, ApplicationStatus.REFUSE);
        if (teamApplication.isPresent() && (Duration.between(teamApplication.get().getUpdatedAt(), LocalDateTime.now()).toHours() <= 24)) {
            throw new CustomException(ErrorCode.TEAM_APPLICATION_REFUSE_COLL_DOWN);
        }
    }
}

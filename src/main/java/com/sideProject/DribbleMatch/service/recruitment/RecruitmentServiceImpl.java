package com.sideProject.DribbleMatch.service.recruitment;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.team.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.team.TeamMember;
import com.sideProject.DribbleMatch.entity.teamApplication.TeamApplication;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.recruitment.RecruitmentRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.team.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class RecruitmentServiceImpl implements RecruitmentService{

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final UserRepository userRepository;
    private final RegionRepository regionRepository;
    private final RecruitmentRepository recruitmentRepository;
    @Override
    public Long create(RecruitmentCreateRequestDto request, Long teamId, Long adminId) {
        Team team = teamRepository.findById(teamId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_APPLICATION));

        User admin = userRepository.findById(adminId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        TeamMember adminTeamMember = teamMemberRepository.findByUserAndTeam(admin,team).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER));
        if(!adminTeamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        //승인
        return recruitmentRepository.save(request.toEntity()).getId();
    }

    @Override
    public Long update(RecruitmentUpdateRequestDto request, Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        Recruitment recruitment = recruitmentRepository.findById(request.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_RECRUITMENT_ID));

        TeamMember adminTeamMember = teamMemberRepository.findByUserAndTeam(admin,recruitment.getTeam()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER));
        if(!adminTeamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        //승인
        recruitment.update(
                request.getTitle(),
                request.getContent(),
                request.getPositions(),
                request.getWinning()
        );


        return recruitmentRepository.save(recruitment).getId();
    }

    @Override
    public Page<RecruitmentResponseDto> find() {
        return null;
    }

    @Override
    public Long delete(Long recruitmentId, Long adminId) {
        User admin = userRepository.findById(adminId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER_ID));

        Recruitment recruitment = recruitmentRepository.findById(recruitmentId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_RECRUITMENT_ID));

        TeamMember adminTeamMember = teamMemberRepository.findByUserAndTeam(admin,recruitment.getTeam()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM_MEMBER));
        if(!adminTeamMember.getTeamRole().equals(TeamRole.ADMIN)) {
            throw new CustomException(ErrorCode.NO_AUTHORITY);
        }

        //승인
        recruitmentRepository.delete(recruitment);


        return recruitment.getId();
    }
}

package com.sideProject.DribbleMatch.service.recruitment;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentSearchParamRequest;
import com.sideProject.DribbleMatch.dto.recruitment.reqeuest.RecruitmentUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.entity.recruitment.Recruitment;
import com.sideProject.DribbleMatch.entity.teamMember.ENUM.TeamRole;
import com.sideProject.DribbleMatch.entity.team.Team;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.recruitment.RecruitmentRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.teamMember.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.team.TeamRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecruitmentServiceImpl implements RecruitmentService{

    private final RecruitmentRepository recruitmentRepository;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public void createRecruitment(RecruitmentCreateRequestDto requestDto) {

        Team team = teamRepository.findById(requestDto.getTeamId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM));

        recruitmentRepository.save(Recruitment.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .positionString(requestDto.getPositionString())
                .endAt(requestDto.getExpireDate())
                .team(team)
                .build());
    }

    @Override
    public Page<RecruitmentResponseDto> searchRecruitments(String searchWord, Pageable pageable) {

        Page<Recruitment> recruitmentPage =  recruitmentRepository.searchRecruitmentsInTimeOrderByCreatedAt(searchWord, pageable);

        List<RecruitmentResponseDto> responseList = recruitmentPage.stream()
                .map(recruitment -> RecruitmentResponseDto.builder()
                        .title(recruitment.getTitle())
                        .teamId(recruitment.getTeam().getId())
                        .teamName(recruitment.getTeam().getName())
                        .teamImagePath(recruitment.getTeam().getImagePath())
                        .positionString(recruitment.getPositionString())
                        .createdAt(recruitment.getCreatedAt().toLocalDate())
                        .endAt(recruitment.getEndAt())
                        .content(recruitment.getContent())
                        .build())
                .toList();

        return new PageImpl<>(responseList, pageable, recruitmentPage.getTotalElements());
    }
}

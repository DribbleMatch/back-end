package com.sideProject.ClutchShot.service.recruitment;

import com.sideProject.ClutchShot.common.error.CustomException;
import com.sideProject.ClutchShot.common.error.ErrorCode;
import com.sideProject.ClutchShot.dto.recruitment.reqeuest.RecruitmentCreateRequestDto;
import com.sideProject.ClutchShot.dto.recruitment.response.RecruitmentResponseDto;
import com.sideProject.ClutchShot.entity.recruitment.Recruitment;
import com.sideProject.ClutchShot.entity.team.Team;
import com.sideProject.ClutchShot.repository.recruitment.RecruitmentRepository;
import com.sideProject.ClutchShot.repository.team.TeamRepository;
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
    public Long createRecruitment(RecruitmentCreateRequestDto requestDto) {

        Team team = teamRepository.findById(requestDto.getTeamId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_TEAM));

        return recruitmentRepository.save(Recruitment.builder()
                .title(requestDto.getTitle())
                .content(requestDto.getContent())
                .positionString(requestDto.getPositionString())
                .endAt(requestDto.getExpireDate())
                .team(team)
                .build()).getId();
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

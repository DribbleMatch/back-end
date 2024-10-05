package com.sideProject.DribbleMatch.service.teamMatchJoin;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.teamMatchJoin.TeamMatchJoin;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.repository.matching.MatchingRepository;
import com.sideProject.DribbleMatch.repository.teamMatchJoin.TeamMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.teamMember.TeamMemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class TeamMatchJoinServiceImpl implements TeamMatchJoinService{

    private final TeamMemberRepository teamMemberRepository;
    private final TeamMatchJoinRepository teamMatchJoinRepository;
    private final MatchingRepository matchingRepository;

    @Override
    public void createTeamMatchJoin(Long matchingId, Long userId, String teamName) {
        //todo: 현재는 경기 생성시 팀을 설정하면 팀의 전체 멤베거 참여하도록 구현. 추후 팀에서 멤버도 선택할 수 있도록 재설계

        checkAlreadyJoin(matchingId, userId);

        if (Objects.equals(teamName, "")) {
            throw new CustomException(ErrorCode.EMPTY_TEAM_NAME);
        }

        List<TeamMember> teamMembers = teamMemberRepository.findAllByTeamName(teamName);

        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MATCHING));

        List<TeamMatchJoin> teamMatchJoinList = teamMembers.stream()
                .map(teamMember -> TeamMatchJoin.builder()
                        .teamMember(teamMember)
                        .matching(matching).build())
                .toList();

        teamMatchJoinRepository.saveAll(teamMatchJoinList);
    }

    @Override
    public void checkAlreadyJoin(Long matchingId, Long userId) {
        if (teamMatchJoinRepository.findByMatchingAndUser(matchingId, userId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_JOIN_TEAM_MATCH);
        }
    }
}

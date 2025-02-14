package com.sideProject.DribbleMatch.service.personalMatchJoin;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.PersonalMatchJoin;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.matching.MatchingRepository;
import com.sideProject.DribbleMatch.repository.personalMatchJoin.PersonalMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import groovy.transform.TypeChecked;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PersonalMatchJoinServiceImpl implements PersonalMatchJoinService{

    private final MatchingRepository matchingRepository;
    private final UserRepository userRepository;
    private final PersonalMatchJoinRepository personalMatchJoinRepository;

    @Override
    @Transactional
    public Long createPersonalMatchJoin(Long matchingId, Long userId, PersonalMatchingTeam personalMatchingTeam) {

        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MATCHING));
        User user = userRepository.findById(userId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));

        checkAlreadyJoin(matchingId, userId);
        checkMaxNum(matching, personalMatchingTeam);

        return personalMatchJoinRepository.save(PersonalMatchJoin.builder()
                        .matchingTeam(personalMatchingTeam)
                        .user(user)
                        .matching(matching)
                .build()).getId();
    }

    @Override
    @Transactional
    public void updateMatchingStatus(Long matchingId) {

        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MATCHING));

        Long upTeamMemberNum = personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matchingId, PersonalMatchingTeam.UP_TEAM);
        Long downTeamMemberNum = personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matchingId, PersonalMatchingTeam.DOWN_TEAM);

        if ((upTeamMemberNum + downTeamMemberNum) >= (matching.getMaxNum() * 2 * 8L / 10)) {
            matching.updateStatus(MatchingStatus.LAST_MINUTE);
            matchingRepository.save(matching);
        }

        if (upTeamMemberNum + downTeamMemberNum == matching.getMaxNum() * 2L - 1) {
            matching.updateStatus(MatchingStatus.RECRUITMENT_CLOSED);
            matchingRepository.save(matching);
        }
    }

    @Override
    public void checkAlreadyJoin(Long matchingId, Long userId) {
        if (personalMatchJoinRepository.findByMatchingIdAndUserId(matchingId, userId).isPresent()) {
            throw new CustomException(ErrorCode.ALREADY_JOIN_PERSONAL_MATCH);
        }
    }

    @Override
    public void checkMaxNum(Matching matching, PersonalMatchingTeam personalMatchingTeam) {
        if (personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matching.getId(), personalMatchingTeam) == matching.getMaxNum()) {
            throw new CustomException(ErrorCode.LIMIT_MEMBER_NUM);
        }
    }

}

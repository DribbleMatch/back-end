package com.sideProject.DribbleMatch.service.matching;

import com.querydsl.core.Tuple;
import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.common.util.CommonUtil;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingInputScoreRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.*;
import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.IsReservedStadium;
import com.sideProject.DribbleMatch.entity.matching.ENUM.MatchingStatus;
import com.sideProject.DribbleMatch.entity.matching.Matching;
import com.sideProject.DribbleMatch.entity.personalMatchJoin.ENUM.PersonalMatchingTeam;
import com.sideProject.DribbleMatch.entity.region.Region;
import com.sideProject.DribbleMatch.entity.teamMember.TeamMember;
import com.sideProject.DribbleMatch.entity.user.User;
import com.sideProject.DribbleMatch.repository.matching.MatchingRepository;
import com.sideProject.DribbleMatch.repository.personalMatchJoin.PersonalMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.region.RegionRepository;
import com.sideProject.DribbleMatch.repository.teamMatchJoin.TeamMatchJoinRepository;
import com.sideProject.DribbleMatch.repository.teamMember.TeamMemberRepository;
import com.sideProject.DribbleMatch.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingServiceImpl implements MatchingService{

    private final RegionRepository regionRepository;
    private final UserRepository userRepository;
    private final MatchingRepository matchingRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamMatchJoinRepository teamMatchJoinRepository;
    private final PersonalMatchJoinRepository personalMatchJoinRepository;

    @Override
    @Transactional
    public Long createMatching(MatchingCreateRequestDto requestDto, Long creatorId) {

        IsReservedStadium isReservedStadium = IsReservedStadium.NOT_RESERVED;
        Region region;
        String jibun = null;

        // 팀 멤버가 최대 모집 인원보다 많으면 에러
        List<TeamMember> teamMemberList = teamMemberRepository.findAllByTeamName(requestDto.getTeamName());
        if (requestDto.getMaxPeople() < teamMemberList.size()) {
            throw new CustomException(ErrorCode.TEAM_MAX_MEMBER_NUM);
        }

        if (requestDto.getRegionString().isEmpty()) {  // 경기장 확정
            isReservedStadium = IsReservedStadium.RESERVED;
            Map<String, Object> jibunInfo = getRegionAndJibunFromAddress(requestDto.getStadiumJibunAddress());
            region = (Region) jibunInfo.get("region");
            jibun = (String) jibunInfo.get("jibun");
        } else {  // 경기장 미확정
            region = regionRepository.findByRegionString(requestDto.getRegionString()).orElseThrow(() ->
                    new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
        }

        User creator = userRepository.findById(creatorId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_USER));

        return matchingRepository.save(Matching.builder()
                .name(requestDto.getName())
                .playPeople(requestDto.getPlayPeople())
                .maxPeople(requestDto.getMaxPeople())
                .startAt(requestDto.getStartAt())
                .endAt(requestDto.getStartAt().plusHours(requestDto.getHour()))
                .hour(requestDto.getHour())
                .gameKind(requestDto.getGameKind())
                .isOnlyWomen(requestDto.getIsOnlyWomen())
                .isReserved(isReservedStadium)
                .region(region)
                .jibun(jibun)
                .stadiumLoadAddress(Objects.equals(requestDto.getStadiumLoadAddress(), "") ? null : requestDto.getStadiumLoadAddress())
                .detailAddress(Objects.equals(requestDto.getDetailAddress(), "") ? null : requestDto.getDetailAddress())
                .upTeamScore(0)
                .downTeamScore(0)
                .creator(creator)
                .upTeamName(requestDto.getGameKind() == GameKind.PERSONAL ? "A팀" : requestDto.getTeamName())
                .downTeamName(requestDto.getGameKind() == GameKind.PERSONAL ? "B팀" : "(모집중)")
                .build()).getId();
    }

    @Override
    @Transactional
    public Long changeDownTeamName(Matching matching, String teamName) {
        matching.joinTeamMatching(teamName);
        return matchingRepository.save(matching).getId();
    }

    @Override
    @Transactional
    public void inputScore(MatchingInputScoreRequestDto requestDto) {

        Matching matching = matchingRepository.findById(requestDto.getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MATCHING));

        matching.inputScore(requestDto.getUpTeamScore(), requestDto.getDownTeamScore());

        matchingRepository.save(matching);
    }

    @Override
    @Transactional
    public void notPlayMatching(Long matchingId) {

        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MATCHING));

        matching.notPlayMatching();

        matchingRepository.save(matching);
    }

    @Override
    public Boolean checkHasNotInputScore(Long userId) {

        Long count = matchingRepository.countNoScorePersonalMatchingListByUserId(userId);

        return count <= 0;
    }

    @Override
    public List<MatchingSimpleResponseDto> getNotInputScoreMatchingList(Long userId) {
        List<Matching> matchingList = matchingRepository.findNotInputScoreMatchingList(userId);

        return matchingList.stream()
                .map(matching -> MatchingSimpleResponseDto.builder()
                        .id(matching.getId())
                        .upTeamName(matching.getUpTeamName())
                        .downTeamName(matching.getDownTeamName())
                        .startAt(matching.getStartAt())
                        .regionString(createMatchingRegionString(matching))
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<RecentMatchingResponseDto> getRecentMatchingList() {
        return matchingRepository.findRecentMatchingOrderByRemainTime().stream()
                .map(matching -> RecentMatchingResponseDto.builder()
                        .id(matching.getId())
                        .remainTime(CommonUtil.getRemainTimeString(LocalDateTime.now(), matching.getStartAt()))
                        .matchingName(matching.getName())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public Page<MatchingDetailTestResponseDto> searchMatchings(String searchWord, Pageable pageable, LocalDate date) {
        Page<Matching> matchingPage = matchingRepository.searchMatchingListByStartDateOrderByStartTime(searchWord, pageable, date);

        List<MatchingDetailTestResponseDto> responseList = matchingPage.stream()
                .map(matching -> MatchingDetailTestResponseDto.builder()
                        .id(matching.getId())
                        .startAt(matching.getStartAt())
                        .name(matching.getName())
                        .isOnlyWomen(matching.getIsOnlyWomen())
                        .gameKind(matching.getGameKind())
                        .playMemberNum(matching.getPlayPeople())
                        .maxMemberNum(matching.getMaxPeople())
                        .regionString(regionRepository.findRegionStringById(matching.getRegion().getId()).orElseThrow(() ->
                                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING)))
                        .isReservedStadium(matching.getIsReserved())
                        .hour(matching.getHour())
                        .upTeamMemberNum(
                                matching.getGameKind() == GameKind.TEAM ?
                                        teamMatchJoinRepository.countTeamMatchJoinByMatchingIdAndTeamName(matching.getId(), matching.getUpTeamName()).intValue() :
                                        personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.UP_TEAM).intValue()
                        )
                        .downTeamMemberNum(
                                matching.getGameKind() == GameKind.TEAM ?
                                        teamMatchJoinRepository.countTeamMatchJoinByMatchingIdAndTeamName(matching.getId(), matching.getDownTeamName()).intValue() :
                                        personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.DOWN_TEAM).intValue()
                        )
                        .build())
                .collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, matchingPage.getTotalElements());
    }

    @Override
    public MatchingUserDetailResponseDto getMatchingDetail(Long matchingId) {

        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MATCHING));

        return MatchingUserDetailResponseDto.builder()
                .id(matching.getId())
                .name(matching.getName())
                .isReservedStadium(matching.getIsReserved())
                .regionString(createMatchingRegionString(matching))
                .playMemberNum(matching.getPlayPeople())
                .maxMemberNum(matching.getMaxPeople())
                .isOnlyWomen(matching.getIsOnlyWomen())
                .gameKind(matching.getGameKind())
                .startAt(matching.getStartAt())
                .endAt(matching.getStartAt().plusHours(matching.getHour()))
                .hour(matching.getHour())
                .upTeamName(matching.getUpTeamName())
                .downTeamName(matching.getDownTeamName())
                .upTeamMember(convertUserListToMatchingDto(matching.getGameKind() == GameKind.PERSONAL ?
                        personalMatchJoinRepository.findUserByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.UP_TEAM) :
                        teamMatchJoinRepository.findAllUsersByMatchingIdAndTeamName(matching.getId(), matching.getUpTeamName())))
                .downTeamMember(convertUserListToMatchingDto(matching.getGameKind() == GameKind.PERSONAL ?
                        personalMatchJoinRepository.findUserByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.DOWN_TEAM) :
                        teamMatchJoinRepository.findAllUsersByMatchingIdAndTeamName(matching.getId(), matching.getDownTeamName())))
                .build();
    }

    @Override
    public Page<MatchingDetailTestResponseDto> getReservedMatchingList(Long userId, GameKind gameKind, Pageable pageable) {

        Page<Matching> matchingPage = Page.empty();

        if (gameKind.equals(GameKind.TEAM)) {
            matchingPage = matchingRepository.findTeamMatchingListByUserIdOrderByStartAt(userId, pageable, MatchingStatus.RECRUITING);
        } else if (gameKind.equals(GameKind.PERSONAL)) {
            matchingPage = matchingRepository.findPersonalMatchingListByUserIdOrderByStartAt(userId, pageable, MatchingStatus.RECRUITING);
        }

        List<MatchingDetailTestResponseDto> responseList = matchingPage.stream()
                .map(matching -> MatchingDetailTestResponseDto.builder()
                        .id(matching.getId())
                        .upTeamName(matching.getUpTeamName())
                        .downTeamName(matching.getDownTeamName())
                        .upTeamMemberNum(matching.getGameKind() == GameKind.TEAM ?
                                teamMatchJoinRepository.countTeamMatchJoinByMatchingIdAndTeamName(matching.getId(), matching.getUpTeamName()).intValue() :
                                personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.UP_TEAM).intValue()
                        )
                        .downTeamMemberNum(matching.getGameKind() == GameKind.TEAM ?
                                teamMatchJoinRepository.countTeamMatchJoinByMatchingIdAndTeamName(matching.getId(), matching.getDownTeamName()).intValue() :
                                personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.DOWN_TEAM).intValue()
                        )
                        .startAt(matching.getStartAt())
                        .endAt(matching.getEndAt())
                        .regionString(createMatchingRegionString(matching))
                        .playMemberNum(matching.getPlayPeople())
                        .maxMemberNum(matching.getMaxPeople())
                        .build()
                ).collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, matchingPage.getTotalElements());
    }

    @Override
    public Page<MatchingDetailTestResponseDto> getEndedMatchingList(Long userId, GameKind gameKind, Pageable pageable) {

        Page<Matching> matchingPage = Page.empty();

        if (gameKind.equals(GameKind.TEAM)) {
            matchingPage = matchingRepository.findTeamMatchingListByUserIdOrderByStartAt(userId, pageable, MatchingStatus.FINISHED);
        } else if (gameKind.equals(GameKind.PERSONAL)) {
            matchingPage = matchingRepository.findPersonalMatchingListByUserIdOrderByStartAt(userId, pageable, MatchingStatus.FINISHED);
        }

        List<MatchingDetailTestResponseDto> responseList = matchingPage.stream()
                .map(matching -> MatchingDetailTestResponseDto.builder()
                        .id(matching.getId())
                        .upTeamScore(matching.getUpTeamScore())
                        .downTeamScore(matching.getDownTeamScore())
                        .upTeamName(matching.getUpTeamName())
                        .downTeamName(matching.getDownTeamName())
                        .startAt(matching.getStartAt())
                        .endAt(matching.getEndAt())
                        .regionString(createMatchingRegionString(matching))
                        .playMemberNum(matching.getPlayPeople())
                        .build()
                ).collect(Collectors.toList());

        return new PageImpl<>(responseList, pageable, matchingPage.getTotalElements());
    }



    private Map<String, Object> getRegionAndJibunFromAddress(String stadiumAddress) {

        String[] parts = stadiumAddress.split(" ");

        String jibun = parts[parts.length - 1];
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < parts.length - 1; i++) {
            result.append(parts[i]);
            if (i < parts.length - 2) {
                result.append(" ");
            }
        }

        String jibunAddress = result.toString();
        Region region = regionRepository.findByRegionString(jibunAddress).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));

        Map<String, Object> resultMap = new HashMap<>();
        resultMap.put("region", region);
        resultMap.put("jibun", jibun);

        return resultMap;
    }

    private String createMatchingRegionString(Matching matching) {
        String regionString = regionRepository.findRegionStringById(matching.getRegion().getId()).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_REGION_STRING));
        if (!matching.getIsReserved().equals(IsReservedStadium.RESERVED)) {
            return regionString;
        } else {
            if (matching.getStadiumLoadAddress() == null) {
                return regionString + " " + matching.getJibun() + " " + matching.getDetailAddress();
            } else {
                return matching.getStadiumLoadAddress() + " " + matching.getDetailAddress();
            }
        }
    }

    private List<MatchingUserResponseDto> convertUserListToMatchingDto(List<User> userList) {
        return userList.stream()
                .map(user -> MatchingUserResponseDto.builder()
                        .user(user)
                        .build())
                .collect(Collectors.toList());
    }
}
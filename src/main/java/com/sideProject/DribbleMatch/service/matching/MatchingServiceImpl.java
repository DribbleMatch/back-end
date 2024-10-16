package com.sideProject.DribbleMatch.service.matching;

import com.sideProject.DribbleMatch.common.error.CustomException;
import com.sideProject.DribbleMatch.common.error.ErrorCode;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingCreateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.request.MatchingUpdateRequestDto;
import com.sideProject.DribbleMatch.dto.matching.response.MatchingDetailResponseDto;
import com.sideProject.DribbleMatch.dto.matching.response.MatchingResponseDto;
import com.sideProject.DribbleMatch.dto.matching.response.MatchingUserResponseDto;
import com.sideProject.DribbleMatch.dto.team.response.TeamListResponseDto;
import com.sideProject.DribbleMatch.entity.matching.ENUM.GameKind;
import com.sideProject.DribbleMatch.entity.matching.ENUM.IsReservedStadium;
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
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MatchingServiceImpl implements MatchingService{

    private final RegionRepository regionRepository;
    private final MatchingRepository matchingRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamMatchJoinRepository teamMatchJoinRepository;
    private final PersonalMatchJoinRepository personalMatchJoinRepository;

    @Override
    @Transactional
    public Long createMatching(MatchingCreateRequestDto requestDto) {

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

        return matchingRepository.save(Matching.builder()
                    .name(requestDto.getName())
                    .playPeople(requestDto.getPlayPeople())
                    .maxPeople(requestDto.getMaxPeople())
                    .startAt(requestDto.getStartAt())
                    .hour(requestDto.getHour())
                    .gameKind(requestDto.getGameKind())
                    .isOnlyWomen(requestDto.getIsOnlyWomen())
                    .isReserved(isReservedStadium)
                    .region(region)
                    .jibun(jibun)
                    .stadiumLoadAddress(Objects.equals(requestDto.getStadiumLoadAddress(), "") ? null : requestDto.getStadiumLoadAddress())
                    .detailAddress(Objects.equals(requestDto.getDetailAddress(), "") ? null : requestDto.getDetailAddress())
                .build()).getId();
    }

    @Override
    public Page<MatchingResponseDto> searchMatchings(String searchWord, Pageable pageable, LocalDate date) {
        Page<Matching> matchingPage = matchingRepository.searchMatchingsByStartDateOrderByStartTime(searchWord, pageable, date);

        List<MatchingResponseDto> responseList = matchingPage.stream()
                .map(matching -> MatchingResponseDto.builder()
                        .id(matching.getId())
                        .startAt(matching.getStartAt().toLocalTime())
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
                                        teamMatchJoinRepository.countTeamMatchJoinByMatching(matching).intValue() :
                                        personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matching, PersonalMatchingTeam.UP_TEAM).intValue()
                        )
                        .downTeamMemberNum(
                                matching.getGameKind() == GameKind.TEAM ?
                                        0 :
                                        personalMatchJoinRepository.countPersonalMatchJoinByMatchingAndTeam(matching, PersonalMatchingTeam.DOWN_TEAM).intValue()
                        )
                        .build())
                .toList();

        return new PageImpl<>(responseList, pageable, matchingPage.getTotalElements());
    }

    @Override
    public MatchingDetailResponseDto getMatchingDetail(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId).orElseThrow(() ->
                new CustomException(ErrorCode.NOT_FOUND_MATCHING));

        LinkedHashMap<String, List<TeamMember>> teamInfo = teamMatchJoinRepository.findTeamInfoByMatchingId(matching.getId());

        return createMatchingUserResponseDto(matching, teamInfo);
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

    private MatchingDetailResponseDto createMatchingUserResponseDto(Matching matching, LinkedHashMap<String, List<TeamMember>> teamInfo) {

        List<String> teamNameList = new ArrayList<>(teamInfo.keySet());

        // 팀 이름 처리
        String aTeamName = "A팀";
        String bTeamName = "B팀";

        if (teamNameList.size() == 2) {
            aTeamName = teamNameList.get(0);
            bTeamName = teamNameList.get(1);
        } else if (teamNameList.size() == 1) {
            aTeamName = teamNameList.get(0);
            bTeamName = "";
        }

        // 팀 인원 처리
        List<MatchingUserResponseDto> aTeamMember = new ArrayList<>();
        List<MatchingUserResponseDto> bTeamMember = new ArrayList<>();

        if (teamInfo.size() == 2) {
            aTeamMember = teamInfo.get(teamNameList.get(0)).stream()
                    .map(teamMember -> MatchingUserResponseDto
                            .builder()
                            .user(teamMember.getUser())
                            .build()).collect(Collectors.toList());

            bTeamMember = teamInfo.get(teamNameList.get(1)).stream()
                    .map(teamMember -> MatchingUserResponseDto
                            .builder()
                            .user(teamMember.getUser())
                            .build()).collect(Collectors.toList());
        } else if (teamInfo.size() == 1) {
            aTeamMember = teamInfo.get(teamNameList.get(0)).stream()
                    .map(teamMember -> MatchingUserResponseDto
                            .builder()
                            .user(teamMember.getUser())
                            .build()).collect(Collectors.toList());
        } else {
            aTeamMember = convertUserListToMatchingDto(personalMatchJoinRepository.findUserByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.UP_TEAM));
            bTeamMember = convertUserListToMatchingDto(personalMatchJoinRepository.findUserByMatchingAndTeam(matching.getId(), PersonalMatchingTeam.DOWN_TEAM));
        }

        return MatchingDetailResponseDto.builder()
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
                .aTeamName(aTeamName)
                .bTeamName(bTeamName)
                .aTeamMember(aTeamMember)
                .bTeamMember(bTeamMember)
                .build();
    }

    private List<MatchingUserResponseDto> convertUserListToMatchingDto(List<User> userList) {
        return userList.stream()
                .map(user -> MatchingUserResponseDto.builder()
                        .user(user)
                        .build())
                .collect(Collectors.toList());
    }
}

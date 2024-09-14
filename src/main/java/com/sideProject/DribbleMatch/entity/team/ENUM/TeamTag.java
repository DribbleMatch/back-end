package com.sideProject.DribbleMatch.entity.team.ENUM;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum TeamTag {

    // 경기 스타일

    FAST_PACED("빠른 템포를 선호", "F0F8FF"),
    CALM_PACED("침착하고 신중한 플레이 선호", "F5F5DC"),
    DEFENSIVE_MINDED("수비가 중요", "FAF0E6"),
    OFFENSIVE_MINDED("공격이 중요", "FFF8DC"),
    SHOOTING_MINDED("슈팅이 중요", "F0FFF0"),
    PHYSICAL_MINDED("몸싸움이 중요", "FFFAF0"),
    STRATEGIC_MINDED("전략이 중요", "F5FFFA"),

    // 분위기
    YOUTHFUL("젊은 선수들이 많은 팀", "F8F8FF"),
    EXPERIENCED("경험이 많은 팀", "FFF5EE"),
    TOURNAMENT_FOCUSED("대회 위주", "FDF5E6"),
    SCRIMMAGE_FOCUSED("연습 경기 위주", "E6E6FA"),
    ENJOY("즐기기 위해서", "F0FFFF"),
    SERIOUS("진지한 마음가짐", "FFFAFA"),
    TEAM_ORIENTED("팀워크가 중요", "F0E68C");


    public final String explain;
    public final String color;
}

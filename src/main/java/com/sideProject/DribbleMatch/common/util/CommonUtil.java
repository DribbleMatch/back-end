package com.sideProject.DribbleMatch.common.util;

import com.sideProject.DribbleMatch.entity.user.ENUM.Skill;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Map;

@Component
public class CommonUtil {

    public static int getLevel(int experience) {

        int level = 1;

        for(int toLevelUp = 100; experience - toLevelUp >= 0; level++) {
            toLevelUp = (level) * 100;
            experience -= toLevelUp;
        }

        return level;
    }

    public static double getExperiencePercentToLevelUp(int experience) {

        int level = 1;
        int toLevelUp;

        for(toLevelUp = 100; experience - toLevelUp >= 0; level++) {
            toLevelUp = level * 100;
            experience -= toLevelUp;
        }

        return Double.parseDouble(String.format("%.1f", (double) experience / (level * 100) * 100));
    }

    public static String createPositionString(String positionString) {
        return positionString.replace("C", "센터")
                .replace("PW", "파워포워드")
                .replace("SF", "스몰포워드")
                .replace("SG", "슈팅가드")
                .replace("PG", "포인트가드")
                .replace(",", " ,");
    }

    public static String createSkillString(Skill skill) {

        Map<Skill, String> skillMap = Map.of(
                Skill.BEGINNER, "초급자",
                Skill.INTERMEDIATE, "중급자",
                Skill.ADVANCED, "고급자",
                Skill.FORMER_ATHLETE, "프로 선수 출신",
                Skill.ATHLETE, "프로 선수"
        );

        return skillMap.getOrDefault(skill, "알 수 없음");
    }

    public static int calculateAge(LocalDate birth) {

        LocalDate currentDate = LocalDate.now();

        int birthYear = birth.getYear();
        int currentYear = currentDate.getYear();

        return currentYear - birthYear + 1;
    }

    public static String getRemainTimeString(LocalDateTime now, LocalDateTime startAt) {
        Duration duration = Duration.between(now, startAt);
        long days = duration.toDays();
        long hours = duration.minusDays(days).toHours();
        long minutes = duration.minusHours(hours).toMinutes();

        StringBuilder result = new StringBuilder();
        if (days > 0) {
            result.append(days).append("일 ");
        }
        if (hours > 0) {
            result.append(hours).append("시간");
        }
        if (minutes > 0 && days == 0 && hours == 0) {
            result.append(minutes).append("분");
        }

        return result.toString().trim();
    }
}

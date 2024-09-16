package com.sideProject.DribbleMatch.common.util;

import org.springframework.stereotype.Component;

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
}

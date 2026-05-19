package com.lab4;

/**
 * 주어진 요일이 주말(SATURDAY, SUNDAY)인지 판별
 */
public class WeekendChecker {

    public static boolean isWeekend(DayOfWeek day) {
        return day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY;
    }
}

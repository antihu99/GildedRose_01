package com.lab4;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JUnit 5 다양한 매개변수화 테스트
 */
class ParameterizedTestTest {

    // 1. 문자열 목록 — @ValueSource
    @ParameterizedTest
    @ValueSource(strings = {"hong", "kim", "park"})
    void printName(String name) {
        assertNotNull(name);
        assertFalse(name.isBlank());
    }

    // 2. Enum 전체 — @EnumSource
    @ParameterizedTest
    @EnumSource(DayOfWeek.class)
    void printDayOfWeek(DayOfWeek day) {
        assertNotNull(day);
    }

    // 3. Enum 선택 — names 필터
    @ParameterizedTest
    @EnumSource(value = DayOfWeek.class,
            names = {"MONDAY", "WEDNESDAY", "FRIDAY"})
    void testWeekdays(DayOfWeek day) {
        assertNotNull(day);
        assertFalse(WeekendChecker.isWeekend(day));
    }

    // 4. CSV 다중 입력 — @CsvSource
    @ParameterizedTest
    @CsvSource({
            "apple, 1",
            "banana, 2",
            "'lemon, lime', 3"
    })
    void testWithCsvSource(String fruit, int rank) {
        assertNotNull(fruit);
        assertTrue(rank > 0);
    }

    // 5. 주말 판별 — 성공/실패 혼합
    //    MONDAY는 주말 아님 → 이 테스트는 실패!
    //    → 의도적 실패로 TDD 진행
    @ParameterizedTest(name = "{0} 주말여부")
    @EnumSource(value = DayOfWeek.class,
            names = {"SATURDAY", "MONDAY", "SUNDAY"})
    void testIsWeekend(DayOfWeek day) {
        // ⚠ MONDAY가 포함돼 테스트 실패 → 의도된 것
        // → WeekendChecker 로직 점검 기회
        assertTrue(
                WeekendChecker.isWeekend(day)
        );
    }
}

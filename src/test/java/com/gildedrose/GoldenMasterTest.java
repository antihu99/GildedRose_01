package com.gildedrose;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@DisplayName("Golden Master regression test")
class GoldenMasterTest {
    private static final int THIRTY_DAY_SIMULATION_DAYS = 31;
    private static final String GOLDEN_MASTER_FILE = "/golden_master_expected.txt";

    @Test
    @DisplayName("TexttestFixture 30일 출력이 기준 출력과 동일하다")
    void texttestFixtureOutputMatchesGoldenMaster() throws IOException {
        String expected = readGoldenMaster();
        String actual = TexttestFixture.generateReport(THIRTY_DAY_SIMULATION_DAYS);

        assertEquals(normalizeLineEndings(expected), normalizeLineEndings(actual));
    }

    private String readGoldenMaster() throws IOException {
        try (InputStream input = getClass().getResourceAsStream(GOLDEN_MASTER_FILE)) {
            if (input == null) {
                fail("Golden master file not found: " + GOLDEN_MASTER_FILE);
            }
            return new String(input.readAllBytes(), StandardCharsets.UTF_8);
        }
    }

    private String normalizeLineEndings(String text) {
        if (text.startsWith("\uFEFF")) {
            text = text.substring(1);
        }
        return text.replace("\r\n", "\n").replace('\r', '\n');
    }
}

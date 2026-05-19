package com.example;

import java.util.Arrays;
import java.util.stream.Collectors;

public class StringProcessor {

    /**
     * 입력 문자열의 단어들을 정렬하여 반환
     */
    public static String process(String input) {
        return Arrays.stream(input.split("\\s+"))
                .sorted()
                .collect(Collectors.joining(" "));
    }
}

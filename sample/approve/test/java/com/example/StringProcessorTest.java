package com.example;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

public class StringProcessorTest {

    // 기본 사용법
    @Test
    public void testSortWords() {
        String input =
                "banana apple Orange Grape";
        String result =
                StringProcessor.process(input);

        Approvals.verify(result);
        // → ClassName.methodName
        //   .approved.txt 와 비교
    }
}

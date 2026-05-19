package com.example;

import org.approvaltests.Approvals;
import org.junit.jupiter.api.Test;

import java.util.List;

public class PersonTest {

    // 컬렉션 Approval
    @Test
    public void approveList() {
        List<String> names = List.of("Alice", "Bob", "Charlie");
        Approvals.verifyAll("Names", names);
    }
}

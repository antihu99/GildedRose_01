package com.lab4;

/**
 * 과일과 등급 검증 (null·빈 값·양수 체크)
 */
public class FruitRanker {

    private final String fruit;
    private final int rank;

    public FruitRanker(String fruit, int rank) {
        this.fruit = fruit;
        this.rank = rank;
    }

    public String getFruit() {
        return fruit;
    }

    public int getRank() {
        return rank;
    }

    public boolean isValid() {
        return fruit != null && !fruit.isBlank() && rank > 0;
    }
}

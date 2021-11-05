package com.example.model;

public enum YellowGemTile {
    REWARD(10),
    REQUIRED_SCORE(0),
    MAXIMUM_ACHIEVABLE_TIMES(15);

    private final int value;

    YellowGemTile(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

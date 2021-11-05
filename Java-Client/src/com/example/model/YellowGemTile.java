package com.example.model;

public enum YellowGemTile {
    REWARD(5),
    REQUIRED_SCORE(0),
    MAXIMUM_REACHABLE_TIMES(15);

    private final int value;

    YellowGemTile(int value) {
        this.value = value;
    }
}

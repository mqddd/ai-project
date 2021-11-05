package com.example.model;

public enum RedGemTile {
    REWARD(75),
    REQUIRED_SCORE(0),
    MAXIMUM_REACHABLE_TIMES(15);

    private final int value;

    RedGemTile(int value) {
        this.value = value;
    }
}

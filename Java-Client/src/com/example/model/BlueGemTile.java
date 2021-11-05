package com.example.model;

public enum BlueGemTile {
    REWARD(75),
    REQUIRED_SCORE(0),
    MAXIMUM_REACHABLE_TIMES(15);

    private final int value;

    BlueGemTile(int value) {
        this.value = value;
    }
}

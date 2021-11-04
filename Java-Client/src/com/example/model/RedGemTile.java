package com.example.model;

public class RedGemTile extends BaseTile {

    public static final int REWARD = 75;
    public static final int REQUIRED_SCORE = 0;
    public static final int MAXIMUM_REACHABLE_TIMES = 15;

    public RedGemTile(int x, int y) {
        super(x, y, TileType.RED_GEM);
    }
}

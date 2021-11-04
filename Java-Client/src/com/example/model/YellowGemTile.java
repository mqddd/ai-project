package com.example.model;

public class YellowGemTile extends BaseTile {

    public static final int REWARD = 75;
    public static final int REQUIRED_SCORE = 0;
    public static final int MAXIMUM_REACHABLE_TIMES = 15;

    public YellowGemTile(int x, int y) {
        super(x, y, TileType.YELLOW_GEM);
    }
}

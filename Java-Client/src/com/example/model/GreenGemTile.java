package com.example.model;

public class GreenGemTile extends BaseTile {

    public static final int REWARD = 75;
    public static final int REQUIRED_SCORE = 0;
    public static final int MAXIMUM_REACHABLE_TIMES = 15;

    public GreenGemTile(int x, int y) {
        super(x, y, TileType.GREEN_GEM);
    }
}

package com.example.model;

public class BaseTile {

    private int x;
    private int y;
    private TileType type;

    public BaseTile(int x, int y) {
        this.x = x;
        this.y = y;
        this.type = TileType.EMPTY;
    }

    public BaseTile(int x, int y, TileType type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public TileType getType() {
        return type;
    }

    public void setType(TileType type) {
        this.type = type;
    }
}

package com.example.model.dto;

import com.example.model.Tile;

import java.util.List;

public class ComplexBFSNode extends Node {
    private List<Tile> pickedGems;
    private List<Tile> remainedGems;
    private int bound;

    public ComplexBFSNode(int level, int costUntilHere, Tile currentTile, List<Tile> pickedGems, List<Tile> remainedGems) {
        super(level, costUntilHere, currentTile);
        this.pickedGems = pickedGems;
        this.remainedGems = remainedGems;
    }

    public List<Tile> getPickedGems() {
        return pickedGems;
    }

    public void setPickedGems(List<Tile> pickedGems) {
        this.pickedGems = pickedGems;
    }

    public List<Tile> getRemainedGems() {
        return remainedGems;
    }

    public void setRemainedGems(List<Tile> remainedGems) {
        this.remainedGems = remainedGems;
    }

    public int getBound() {
        return bound;
    }

    public void setBound(int bound) {
        this.bound = bound;
    }
}

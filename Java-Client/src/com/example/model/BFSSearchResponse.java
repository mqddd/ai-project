package com.example.model;

import java.util.List;

public class BFSSearchResponse {
    private List<Tile> path;
    private int cost;
    private Tile goal;
    private Tile source;
    private int goalLevel;

    public BFSSearchResponse(List<Tile> path, int cost, Tile goal, Tile source, int goalLevel) {
        this.path = path;
        this.cost = cost;
        this.goal = goal;
        this.source = source;
        this.goalLevel = goalLevel;
    }

    public List<Tile> getPath() {
        return path;
    }

    public void setPath(List<Tile> path) {
        this.path = path;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public Tile getGoal() {
        return goal;
    }

    public void setGoal(Tile goal) {
        this.goal = goal;
    }

    public Tile getSource() {
        return source;
    }

    public void setSource(Tile source) {
        this.source = source;
    }

    public int getGoalLevel() {
        return goalLevel;
    }

    public void setGoalLevel(int goalLevel) {
        this.goalLevel = goalLevel;
    }
}

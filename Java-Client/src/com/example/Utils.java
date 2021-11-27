package com.example;

import com.example.model.*;

import java.util.*;

public class Utils {

    private Agent agent;
    private Map<TileType, List<Tile>> tileTypeListMap;
    private Tile myAgentTile;
    private int teleportCost;

    public Utils(Agent agent) {
        this.agent = agent;
        this.tileTypeListMap = new HashMap<>();
        this.initAgentTile();
        this.initTileTypeListMap();
        this.initProperties();
        this.teleportCost = this.tileTypeListMap.get(TileType.TELEPORT).size() - 1;
    }

    /*
        this function update all utils properties except
        agent and teleportCost which are constant
     */
    public void refreshProperties() {
        this.tileTypeListMap = new HashMap<>();
        this.initProperties();
    }

    /*
        this function inits agent tile. we call it
        before initializing other properties because
        we call possibleToGetGem on each gem tile
        which uses agent tile so agent tile should
        not be null
     */
    private void initAgentTile() {
        for (int i = 0; i < this.agent.getGrid().length; i++) {
            for (int j = 0; j < this.agent.getGrid()[0].length; j++) {
                if (this.agent.getGrid()[i][j].contains(String.valueOf(agent.getCharacter()))) {
                    TileType type = getTileType(i, j);
                    this.myAgentTile = new Tile(i, j, type);
                }
            }
        }
    }

    private void initTileTypeListMap() {
        for (TileType type : TileType.values()) {
            this.tileTypeListMap.put(type, new ArrayList<>());
        }
    }

    private void initProperties() {
        for (int i = 0; i < this.agent.getGrid().length; i++) {
            for (int j = 0; j < this.agent.getGrid()[0].length; j++) {
                addTileToTileTypeListMap(i, j);
            }
        }
    }

    private void addTileToTileTypeListMap(int x, int y) {
        TileType type = getTileType(x, y);
        Tile tile = new Tile(x, y, type);
        if (type.equals(TileType.YELLOW_GEM) || type.equals(TileType.GREEN_GEM) ||
                type.equals(TileType.RED_GEM) || type.equals(TileType.BLUE_GEM))
            if (possibleToGetGem(this.myAgentTile, tile))
                this.tileTypeListMap.get(type).add(tile);
            else
                this.tileTypeListMap.get(type).add(tile);
    }

    public TileType getTileType(int x, int y) {
        if (this.agent.getGrid()[x][y].contains(TileType.EMPTY.getValue()))
            return TileType.EMPTY;
        else if (this.agent.getGrid()[x][y].contains(TileType.TELEPORT.getValue()))
            return TileType.TELEPORT;
        else if (this.agent.getGrid()[x][y].contains(TileType.YELLOW_GEM.getValue()))
            return TileType.YELLOW_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.GREEN_GEM.getValue()))
            return TileType.GREEN_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.RED_GEM.getValue()))
            return TileType.RED_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.BLUE_GEM.getValue()))
            return TileType.BLUE_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.WALL.getValue()))
            return TileType.WALL;
        else throw new IllegalStateException("Tile isn't valid!");
    }

    private boolean possibleToGetGem(Tile myAgentTile, Tile goalTile) {
        int minCostToGoal = heuristic(myAgentTile, goalTile);
        int myAgentScore = this.agent.getAgentScores()[0];
        int requiredScore;
        if (goalTile.getType().equals(TileType.YELLOW_GEM))
            requiredScore = YellowGemTile.REQUIRED_SCORE.getValue();
        else if (goalTile.getType().equals(TileType.GREEN_GEM))
            requiredScore = GreenGemTile.REQUIRED_SCORE.getValue();
        else if (goalTile.getType().equals(TileType.RED_GEM))
            requiredScore = RedGemTile.REQUIRED_SCORE.getValue();
        else if (goalTile.getType().equals(TileType.BLUE_GEM))
            requiredScore = BlueGemTile.REQUIRED_SCORE.getValue();
        else throw new IllegalStateException("goal should be gem!");
        if ((myAgentScore - minCostToGoal) >= requiredScore) {
            if (goalTile.getType().equals(TileType.YELLOW_GEM)) {
                return this.agent.getCollectedGemsMap().get(TileType.YELLOW_GEM) < YellowGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else if (goalTile.getType().equals(TileType.GREEN_GEM)) {
                return this.agent.getCollectedGemsMap().get(TileType.GREEN_GEM) < GreenGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else if (goalTile.getType().equals(TileType.RED_GEM)) {
                return this.agent.getCollectedGemsMap().get(TileType.RED_GEM) < RedGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else if (goalTile.getType().equals(TileType.BLUE_GEM)) {
                return this.agent.getCollectedGemsMap().get(TileType.BLUE_GEM) < BlueGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else throw new IllegalStateException("goal should be gem!");
        }
        return false;
    }

    private int heuristic(Tile myAgentTile, Tile goalTile) {
        return Math.abs(myAgentTile.getX() - goalTile.getX()) + Math.abs(myAgentTile.getY() - goalTile.getY());
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Map<TileType, List<Tile>> getTileTypeListMap() {
        return tileTypeListMap;
    }

    public void setTileTypeListMap(Map<TileType, List<Tile>> tileTypeListMap) {
        this.tileTypeListMap = tileTypeListMap;
    }

    public Tile getMyAgentTile() {
        return myAgentTile;
    }

    public void setMyAgentTile(Tile myAgentTile) {
        this.myAgentTile = myAgentTile;
    }

    public int getTeleportCost() {
        return teleportCost;
    }

    public void setTeleportCost(int teleportCost) {
        this.teleportCost = teleportCost;
    }
}

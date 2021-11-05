package com.example;

import com.example.model.*;

import java.util.LinkedList;
import java.util.List;

public class Utils {

    private List<Tile> remainedYellowGems;
    private List<Tile> remainedGreenGems;
    private List<Tile> remainedRedGems;
    private List<Tile> remainedBlueGems;
    private List<Tile> teleportTiles;
    private Tile myAgentTile;
    private Agent agent;
    private int teleportCost;

    public Utils(Agent agent) {
        this.remainedYellowGems = new LinkedList<>();
        this.remainedGreenGems = new LinkedList<>();
        this.remainedRedGems = new LinkedList<>();
        this.remainedBlueGems = new LinkedList<>();
        this.teleportTiles = new LinkedList<>();
        this.agent = agent;
        this.initProperties();
        this.teleportCost = this.teleportTiles.size() - 1;
    }

    public void resetProperties() {
        this.remainedYellowGems = new LinkedList<>();
        this.remainedGreenGems = new LinkedList<>();
        this.remainedRedGems = new LinkedList<>();
        this.remainedBlueGems = new LinkedList<>();
        this.teleportTiles = new LinkedList<>();
        this.initProperties();
    }

    private void initProperties() {
        for (int i = 0; i < this.agent.getGrid().length; i++) {
            for (int j = 0; j < this.agent.getGrid()[0].length; j++) {
                if (this.agent.getGrid()[i][j].contains(TileType.YELLOW_GEM.getValue())) {
                    Tile yellowGem = new Tile(i, j, TileType.YELLOW_GEM);
                    if (!this.getAgent().getBlockedGems().contains(yellowGem))
                        this.remainedYellowGems.add(yellowGem);
                }
                if (this.agent.getGrid()[i][j].contains(TileType.GREEN_GEM.getValue())) {
                    Tile greenGem = new Tile(i, j, TileType.GREEN_GEM);
                    if (!this.getAgent().getBlockedGems().contains(greenGem))
                        this.remainedGreenGems.add(greenGem);
                }
                if (this.agent.getGrid()[i][j].contains(TileType.RED_GEM.getValue())) {
                    Tile redGem = new Tile(i, j, TileType.RED_GEM);
                    if (!this.getAgent().getBlockedGems().contains(redGem))
                        this.remainedRedGems.add(redGem);
                }
                if (this.agent.getGrid()[i][j].contains(TileType.BLUE_GEM.getValue())) {
                    Tile blueGem = new Tile(i, j, TileType.BLUE_GEM);
                    if (!this.getAgent().getBlockedGems().contains(blueGem))
                        this.remainedBlueGems.add(blueGem);
                }
                if (this.agent.getGrid()[i][j].contains(TileType.TELEPORT.getValue())) {
                    Tile teleport = new Tile(i, j, TileType.TELEPORT);
                    this.teleportTiles.add(teleport);
                }
                if (this.agent.getGrid()[i][j].contains(String.valueOf(agent.getCharacter()))) {
                    this.myAgentTile = createTile(i, j);
                }
            }
        }
    }

    private Tile createTile(int x, int y) {
        if (this.getAgent().getGrid()[x][y].contains(TileType.EMPTY.getValue()))
            return new Tile(x, y, TileType.EMPTY);
        else if (this.getAgent().getGrid()[x][y].contains(TileType.TELEPORT.getValue()))
            return new Tile(x, y, TileType.TELEPORT);
        else if (this.getAgent().getGrid()[x][y].contains(TileType.YELLOW_GEM.getValue()))
            return new Tile(x, y, TileType.YELLOW_GEM);
        else if (this.getAgent().getGrid()[x][y].contains(TileType.GREEN_GEM.getValue()))
            return new Tile(x, y, TileType.GREEN_GEM);
        else if (this.getAgent().getGrid()[x][y].contains(TileType.RED_GEM.getValue()))
            return new Tile(x, y, TileType.RED_GEM);
        else if (this.getAgent().getGrid()[x][y].contains(TileType.BLUE_GEM.getValue()))
            return new Tile(x, y, TileType.BLUE_GEM);
        else if (this.getAgent().getGrid()[x][y].contains(TileType.WALL.getValue()))
            return new Tile(x, y, TileType.WALL);
        else throw new IllegalStateException("map isn't valid!");
    }

    public List<Tile> getRemainedYellowGems() {
        return remainedYellowGems;
    }

    public void setRemainedYellowGems(List<Tile> remainedYellowGems) {
        this.remainedYellowGems = remainedYellowGems;
    }

    public List<Tile> getRemainedGreenGems() {
        return remainedGreenGems;
    }

    public void setRemainedGreenGems(List<Tile> remainedGreenGems) {
        this.remainedGreenGems = remainedGreenGems;
    }

    public List<Tile> getRemainedRedGems() {
        return remainedRedGems;
    }

    public void setRemainedRedGems(List<Tile> remainedRedGems) {
        this.remainedRedGems = remainedRedGems;
    }

    public List<Tile> getRemainedBlueGems() {
        return remainedBlueGems;
    }

    public void setRemainedBlueGems(List<Tile> remainedBlueGems) {
        this.remainedBlueGems = remainedBlueGems;
    }

    public List<Tile> getTeleportTiles() {
        return teleportTiles;
    }

    public void setTeleportTiles(List<Tile> teleportTiles) {
        this.teleportTiles = teleportTiles;
    }

    public Tile getMyAgentTile() {
        return myAgentTile;
    }

    public void setMyAgentTile(Tile myAgentTile) {
        this.myAgentTile = myAgentTile;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public int getTeleportCost() {
        return teleportCost;
    }

    public void setTeleportCost(int teleportCost) {
        this.teleportCost = teleportCost;
    }
}

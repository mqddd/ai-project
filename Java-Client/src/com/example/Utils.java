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
    private Agent myAgent;
    private int teleportCost;

    public Utils(Agent myAgent) {
        this.remainedYellowGems = new LinkedList<>();
        this.remainedGreenGems = new LinkedList<>();
        this.remainedRedGems = new LinkedList<>();
        this.remainedBlueGems = new LinkedList<>();
        this.teleportTiles = new LinkedList<>();
        this.myAgent = myAgent;
        this.initProperties();
        this.teleportCost = this.teleportTiles.size() - 1;
    }

    private void initProperties() {
        for (int i = 0; i < this.myAgent.getGrid().length; i++) {
            for (int j = 0; j < this.myAgent.getGrid()[0].length; j++) {
                if (this.myAgent.getGrid()[i][j].contains("1")) {
                    this.remainedYellowGems.add(new Tile(i, j, TileType.YELLOW_GEM));
                }
                if (this.myAgent.getGrid()[i][j].contains("2")) {
                    this.remainedGreenGems.add(new Tile(i, j, TileType.GREEN_GEM));
                }
                if (this.myAgent.getGrid()[i][j].contains("3")) {
                    this.remainedRedGems.add(new Tile(i, j, TileType.RED_GEM));
                }
                if (this.myAgent.getGrid()[i][j].contains("4")) {
                    this.remainedBlueGems.add(new Tile(i, j, TileType.BLUE_GEM));
                }
                if (this.myAgent.getGrid()[i][j].contains("T")) {
                    this.teleportTiles.add(new Tile(i, j, TileType.TELEPORT));
                }
                if (this.myAgent.getGrid()[i][j].contains(String.valueOf(myAgent.getCharacter()))) {
                    this.myAgentTile = createTile(i, j);
                }
            }
        }
    }

    public Tile createTile(int x, int y) {
        if (this.getMyAgent().getGrid()[x][y].contains("E"))
            return new Tile(x, y, TileType.EMPTY);
        else if (this.getMyAgent().getGrid()[x][y].contains("T"))
            return new Tile(x, y, TileType.TELEPORT);
        else if (this.getMyAgent().getGrid()[x][y].contains("1"))
            return new Tile(x, y, TileType.YELLOW_GEM);
        else if (this.getMyAgent().getGrid()[x][y].contains("2"))
            return new Tile(x, y, TileType.GREEN_GEM);
        else if (this.getMyAgent().getGrid()[x][y].contains("3"))
            return new Tile(x, y, TileType.RED_GEM);
        else if (this.getMyAgent().getGrid()[x][y].contains("4"))
            return new Tile(x, y, TileType.BLUE_GEM);
        else if (this.getMyAgent().getGrid()[x][y].contains("W"))
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

    public Agent getMyAgent() {
        return myAgent;
    }

    public void setMyAgent(Agent myAgent) {
        this.myAgent = myAgent;
    }

    public int getTeleportCost() {
        return teleportCost;
    }

    public void setTeleportCost(int teleportCost) {
        this.teleportCost = teleportCost;
    }
}

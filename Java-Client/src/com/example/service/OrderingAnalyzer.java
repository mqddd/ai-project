package com.example.service;

import com.example.Utils;
import com.example.model.Tile;
import com.example.model.TileType;

import java.util.LinkedList;
import java.util.List;

public class OrderingAnalyzer {

    private Utils utils;

    public OrderingAnalyzer(Utils utils) {
        this.utils = utils;
    }

    public Tile getOptimalGoal(Tile myAgentTile) {
        List<Tile> allGems = new LinkedList<>();
        allGems.addAll(utils.getRemainedYellowGems());
        allGems.addAll(utils.getRemainedGreenGems());
        allGems.addAll(utils.getRemainedRedGems());
        allGems.addAll(utils.getRemainedBlueGems());

        int minCost = Integer.MAX_VALUE;
        Tile optimalGoal = null;
        for (Tile gem : allGems) {
            int costOfGem = this.heuristic(myAgentTile, gem);
            if (costOfGem < minCost && possibleToGetGem(myAgentTile, gem)) {
                minCost = costOfGem;
                optimalGoal = gem;
            }
        }

        return optimalGoal;
    }

    private int heuristic(Tile myAgentTile, Tile goalTile) {
        return Math.abs(myAgentTile.getX() - goalTile.getX()) + Math.abs(myAgentTile.getY() - goalTile.getY());
    }

    private boolean possibleToGetGem(Tile myAgentTile, Tile goalTile) {
        int minCostToGoal = heuristic(myAgentTile, goalTile);
        int myAgentScore = utils.getMyAgent().getAgentScores()[0];
        int requiredScore;
        if (goalTile.getType().equals(TileType.YELLOW_GEM))
            requiredScore = 0;
        else if (goalTile.getType().equals(TileType.GREEN_GEM))
            requiredScore = 15;
        else if (goalTile.getType().equals(TileType.RED_GEM))
            requiredScore = 50;
        else if (goalTile.getType().equals(TileType.BLUE_GEM))
            requiredScore = 140;
        else throw new IllegalStateException("goal should be gem!");
        if ((myAgentScore - minCostToGoal) >= requiredScore) {
            if (goalTile.getType().equals(TileType.YELLOW_GEM)) {
                return utils.getMyAgent().getCollectedYellowGems() < 15;
            }
            else if (goalTile.getType().equals(TileType.GREEN_GEM)) {
                return utils.getMyAgent().getCollectedGreenGems() < 8;
            }
            else if (goalTile.getType().equals(TileType.RED_GEM)) {
                return utils.getMyAgent().getCollectedRedGems() < 5;
            }
            else if (goalTile.getType().equals(TileType.BLUE_GEM)) {
                return utils.getMyAgent().getCollectedBlueGems() < 4;
            }
            else throw new IllegalStateException("goal should be gem!");
        }
        return false;
    }
}

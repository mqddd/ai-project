package com.example.logic;

import com.example.Utils;
import com.example.model.*;

import java.util.LinkedList;
import java.util.List;

public class OrderingAnalyzer {

    private Utils utils;

    public OrderingAnalyzer(Utils utils) {
        this.utils = utils;
    }

    public Tile getOptimalGoal(Tile myAgentTile) {
        List<Tile> allGems = concatenateLists(
                utils.getRemainedYellowGems(),
                utils.getRemainedGreenGems(),
                utils.getRemainedRedGems(),
                utils.getRemainedBlueGems());

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

    private List<Tile> concatenateLists(List<Tile>... lists) {
        List<Tile> result = new LinkedList<>();
        for (List<Tile> list : lists)
            result.addAll(list);
        return result;
    }

    private int heuristic(Tile myAgentTile, Tile goalTile) {
        return Math.abs(myAgentTile.getX() - goalTile.getX()) + Math.abs(myAgentTile.getY() - goalTile.getY());
    }

    private boolean possibleToGetGem(Tile myAgentTile, Tile goalTile) {
        int minCostToGoal = heuristic(myAgentTile, goalTile);
        int myAgentScore = utils.getAgent().getAgentScores()[0];
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
                return utils.getAgent().getCollectedYellowGems() < YellowGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else if (goalTile.getType().equals(TileType.GREEN_GEM)) {
                return utils.getAgent().getCollectedGreenGems() < GreenGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else if (goalTile.getType().equals(TileType.RED_GEM)) {
                return utils.getAgent().getCollectedRedGems() < RedGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else if (goalTile.getType().equals(TileType.BLUE_GEM)) {
                return utils.getAgent().getCollectedBlueGems() < BlueGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else throw new IllegalStateException("goal should be gem!");
        }
        return false;
    }
}

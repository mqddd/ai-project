package com.example.logic;

import com.example.Utils;
import com.example.model.*;

import java.util.*;

public class OrderingAnalyzer {

    private Utils utils;

    public OrderingAnalyzer(Utils utils) {
        this.utils = utils;
    }

    public Tile getOptimalGoal(Tile myAgentTile) {
        List<Tile> gemsToBeOrdered = utils.BFSToGetTopNearestGems(myAgentTile, 5);
        List<Tile> optimalOrder = utils.getOptimalOrder(myAgentTile, gemsToBeOrdered);
        System.out.println("optimal order: " + optimalOrder);
        if (!optimalOrder.isEmpty())
            return optimalOrder.get(0);
        else return null;
    }

}

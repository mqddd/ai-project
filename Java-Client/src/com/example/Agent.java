package com.example;

import com.example.model.Tile;
import com.example.logic.OrderingAnalyzer;
import com.example.logic.RoutingAnalyzer;
import com.example.model.TileType;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Agent extends BaseAgent {

    private Map<TileType, Integer> collectedGemsMap;
    private long timeout;
    private Set<Tile> blockedGems;

    public Agent() {
        super();
    }

    public Agent(String serverIp, int serverPort) {
        super(serverIp, serverPort);
        this.collectedGemsMap = new HashMap();
        initializeCollectedGemsMap();
        this.timeout = 1000000000;
        this.blockedGems = new HashSet<>();
    }

    private void initializeCollectedGemsMap() {
        for (TileType type : TileType.values())
            if (type.equals(TileType.YELLOW_GEM) || type.equals(TileType.GREEN_GEM)
                || type.equals(TileType.RED_GEM) || type.equals(TileType.BLUE_GEM))
                this.collectedGemsMap.put(type, 0);

    }

    public Agent(String serverIp) {
        super(serverIp);
    }

    public Agent(int serverPort) {
        super(serverPort);
    }

    @Override
    public Action doTurn() {
//        if (collectedGreenGems == getTurnCount() - 3 && getTurnCount() != 1)
//            System.out.println("whyyy");
        long startTime = System.nanoTime();
        if (this.getTurnCount() == 10)
            System.out.println("dd");
        Utils utils = new Utils(this);
        OrderingAnalyzer orderingAnalyzer = new OrderingAnalyzer(utils);
//        orderingAnalyzer.printTopNearestGems(utils.getMyAgentTile());
        RoutingAnalyzer routingAnalyzer = new RoutingAnalyzer(utils, orderingAnalyzer);
//        Tile optimalGoal = orderingAnalyzer.getOptimalGoal(utils.getMyAgentTile());
        Tile optimalGoal = orderingAnalyzer.getOptimalGoal2(utils.getMyAgentTile());
        Action nextAction = routingAnalyzer.getNextAction(utils.getMyAgentTile(), optimalGoal);
        long endTime = System.nanoTime();
        System.out.println("all time elapsed: " + (endTime - startTime));
        System.out.println("____________________________________________________");
        return nextAction;
    }

    public static void main(String[] args) throws IOException {
        var client = new Agent("127.0.0.1", 9921);
        client.play();
    }

    public Map<TileType, Integer> getCollectedGemsMap() {
        return collectedGemsMap;
    }

    public void setCollectedGemsMap(Map<TileType, Integer> collectedGemsMap) {
        this.collectedGemsMap = collectedGemsMap;
    }

    public Set<Tile> getBlockedGems() {
        return blockedGems;
    }

    public void setBlockedGems(Set<Tile> blockedGems) {
        this.blockedGems = blockedGems;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }
}

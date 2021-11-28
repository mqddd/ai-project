package com.example;

import com.example.model.Tile;
import com.example.logic.OrderingAnalyzer;
import com.example.logic.RoutingAnalyzer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Agent extends BaseAgent {

    private long timeout;
    private Set<Tile> blockedGems;

    public Agent() {
        super();
    }

    public Agent(String serverIp, int serverPort) {
        super(serverIp, serverPort);
        this.timeout = 1000000000;
        this.blockedGems = new HashSet<>();
    }

    public Agent(String serverIp) {
        super(serverIp);
    }

    public Agent(int serverPort) {
        super(serverPort);
    }

    @Override
    public Action doTurn() {
        long startTime = System.nanoTime();
        Utils utils = new Utils(this);
        OrderingAnalyzer orderingAnalyzer = new OrderingAnalyzer(utils);
//        orderingAnalyzer.printTopNearestGems(utils.getMyAgentTile());
        RoutingAnalyzer routingAnalyzer = new RoutingAnalyzer(utils, orderingAnalyzer);
//        Tile optimalGoal = orderingAnalyzer.getOptimalGoal(utils.getMyAgentTile());
        Tile optimalGoal = orderingAnalyzer.getOptimalGoal(utils.getMyAgentTile());
        Action nextAction = routingAnalyzer.getNextAction(utils.getMyAgentTile(), optimalGoal);
        if (getTurnCount() == 8)
            System.out.println("whyyyyy");
        long endTime = System.nanoTime();
        System.out.println("all time elapsed: " + (endTime - startTime));
        System.out.println("____________________________________________________");
        return nextAction;
    }

    public static void main(String[] args) throws IOException {
        var client = new Agent("127.0.0.1", 9921);
        client.play();
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

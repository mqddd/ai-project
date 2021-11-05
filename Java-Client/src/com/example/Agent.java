package com.example;

import com.example.model.Tile;
import com.example.logic.OrderingAnalyzer;
import com.example.logic.RoutingAnalyzer;

import java.io.IOException;

public class Agent extends BaseAgent {

    private int collectedYellowGems;
    private int collectedGreenGems;
    private int collectedRedGems;
    private int collectedBlueGems;

    public Agent() {
        super();
    }

    public Agent(String serverIp, int serverPort) {
        super(serverIp, serverPort);
        this.collectedYellowGems = 0;
        this.collectedGreenGems = 0;
        this.collectedRedGems = 0;
        this.collectedBlueGems = 0;
    }

    public Agent(String serverIp) {
        super(serverIp);
    }

    public Agent(int serverPort) {
        super(serverPort);
    }

    @Override
    public Action doTurn() {
        Utils utils = new Utils(this);
        OrderingAnalyzer orderingAnalyzer = new OrderingAnalyzer(utils);
        RoutingAnalyzer routingAnalyzer = new RoutingAnalyzer(utils);
        Tile optimalGoal = orderingAnalyzer.getOptimalGoal(utils.getMyAgentTile());
        return routingAnalyzer.getNextAction(utils.getMyAgentTile(), optimalGoal);
    }

    public static void main(String[] args) throws IOException {
        var client = new Agent("127.0.0.1", 9921);
        client.play();
    }

    public int getCollectedYellowGems() {
        return collectedYellowGems;
    }

    public void setCollectedYellowGems(int collectedYellowGems) {
        this.collectedYellowGems = collectedYellowGems;
    }

    public int getCollectedGreenGems() {
        return collectedGreenGems;
    }

    public void setCollectedGreenGems(int collectedGreenGems) {
        this.collectedGreenGems = collectedGreenGems;
    }

    public int getCollectedRedGems() {
        return collectedRedGems;
    }

    public void setCollectedRedGems(int collectedRedGems) {
        this.collectedRedGems = collectedRedGems;
    }

    public int getCollectedBlueGems() {
        return collectedBlueGems;
    }

    public void setCollectedBlueGems(int collectedBlueGems) {
        this.collectedBlueGems = collectedBlueGems;
    }
}

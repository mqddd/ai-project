package com.example;

import com.example.logic.AdversarialAnalyzer;
import com.example.model.Tile;
import com.example.logic.OrderingAnalyzer;
import com.example.logic.RoutingAnalyzer;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Agent extends BaseAgent {

    private long timeout;
    private Set<Tile> blockedGems;
    private int trapUsed;
    private int remainedTrap;
    private long overallMaxTimeElapsed;

    public Agent() {
        super();
    }

    public Agent(String serverIp, int serverPort) {
        super(serverIp, serverPort);
        this.timeout = 1000000000;
        this.blockedGems = new HashSet<>();
        this.trapUsed = 0;
        this.remainedTrap = this.getTrapCount();
        this.overallMaxTimeElapsed = Integer.MIN_VALUE;
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
        this.remainedTrap = this.getTrapCount() - this.trapUsed;
        Utils utils = new Utils(this);
        OrderingAnalyzer orderingAnalyzer = new OrderingAnalyzer(utils);
        RoutingAnalyzer routingAnalyzer = new RoutingAnalyzer(utils, orderingAnalyzer);

//        AdversarialAnalyzer adversarialAnalyzer = new AdversarialAnalyzer(utils, orderingAnalyzer);
//        Action action = adversarialAnalyzer.getNextAction();

        long endTime = System.nanoTime();
        long timeElapsed = endTime - startTime;
        System.out.println("time elapsed: " + timeElapsed);
        Action nextAction = Action.NoOp;
//        if (timeElapsed > 0.9 * this.timeout) {
            Tile optimalGoal = orderingAnalyzer.getOptimalGoal(utils.getMyAgentTile());
            nextAction = routingAnalyzer.getNextAction(utils.getMyAgentTile(), optimalGoal);
//        }
        if (timeElapsed > overallMaxTimeElapsed)
            overallMaxTimeElapsed = timeElapsed;
        System.out.println("overall max time elapsed: " + overallMaxTimeElapsed);
        System.out.println("____________________________________________________");

//
//        if (action != null) {
//            return action;
//        }
//        else {
            System.out.println("**************** adversarial action was null! ***************");
            return nextAction;
//        }
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

    public int getTrapUsed() {
        return trapUsed;
    }

    public void setTrapUsed(int trapUsed) {
        this.trapUsed = trapUsed;
    }

    public int getRemainedTrap() {
        return remainedTrap;
    }

    public void setRemainedTrap(int remainedTrap) {
        this.remainedTrap = remainedTrap;
    }

}

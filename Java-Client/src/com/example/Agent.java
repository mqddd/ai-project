package com.example;

import com.example.logic.ReinforcementAnalyzer;
import com.example.model.Tile;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class Agent extends BaseAgent {

    private long timeout;
    private Set<Tile> blockedGems;
    private int trapUsed;
    private int remainedTrap;
    private ReinforcementAnalyzer reinforcementAnalyzer;

    public Agent() {
        super();
    }

    public Agent(String serverIp, int serverPort) {
        super(serverIp, serverPort);
        this.timeout = 1000000000;
        this.blockedGems = new HashSet<>();
        this.trapUsed = 0;
        this.remainedTrap = this.getTrapCount();
        this.reinforcementAnalyzer = new ReinforcementAnalyzer();
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
        this.reinforcementAnalyzer.updateParams(utils);
        this.reinforcementAnalyzer.qStep();
        Tile agentTile = utils.getMyAgentTile();
        return this.reinforcementAnalyzer.action(agentTile.getX(), agentTile.getY());
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

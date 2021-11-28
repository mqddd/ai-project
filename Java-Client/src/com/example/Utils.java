package com.example;

import com.example.model.*;
import com.example.model.dto.BFSSearchResponse;
import com.example.model.dto.ComplexBFSNode;
import com.example.model.dto.SimpleBFSNode;

import java.util.*;

public class Utils {

    private Agent agent;
    private Map<TileType, Integer> myAgentCollectedGemsMap;
    private Map<TileType, Integer> opponentCollectedGemsMap;
    private Map<TileType, List<Tile>> tileTypeListMap;
    private Tile myAgentTile;
    private final int teleportCost;

    public Utils(Agent agent) {
        this.agent = agent;
        this.initAgentTile();

        this.myAgentCollectedGemsMap = new HashMap<>();
        this.opponentCollectedGemsMap = new HashMap<>();
        this.initCollectedGemsMap();

        this.tileTypeListMap = new HashMap<>();
        this.initTileTypeListMap();
        this.updateTileTypeListMap();

        this.teleportCost = this.tileTypeListMap.get(TileType.TELEPORT).size() - 1;
    }

    /*
        this function update all utils properties except
        agent and teleportCost which are constant
     */
    public void refreshTileTypeListMap() {
        this.tileTypeListMap = new HashMap<>();
        this.initTileTypeListMap();
        this.updateTileTypeListMap();
    }

    /*
        this function inits agent tile. we call it
        before initializing other properties because
        we call possibleToGetGem on each gem tile
        which uses agent tile so agent tile should
        not be null
     */
    private void initAgentTile() {
        for (int i = 0; i < this.agent.getGrid().length; i++) {
            for (int j = 0; j < this.agent.getGrid()[0].length; j++) {
                if (this.agent.getGrid()[i][j].contains(String.valueOf(agent.getCharacter()))) {
                    TileType type = getTileType(i, j);
                    this.myAgentTile = new Tile(i, j, type);
                }
            }
        }
    }

    private void initTileTypeListMap() {
        for (TileType type : TileType.values()) {
            tileTypeListMap.put(type, new ArrayList<>());
        }
    }

    private void updateTileTypeListMap() {
        for (int i = 0; i < this.agent.getGrid().length; i++) {
            for (int j = 0; j < this.agent.getGrid()[0].length; j++) {
                addTileToTileTypeListMap(i, j);
            }
        }
    }

    private void addTileToTileTypeListMap(int x, int y) {
        TileType type = getTileType(x, y);
        Tile tile = new Tile(x, y, type);
        if (type.equals(TileType.YELLOW_GEM) || type.equals(TileType.GREEN_GEM) ||
                type.equals(TileType.RED_GEM) || type.equals(TileType.BLUE_GEM)) {
            if (possibleToGetGem(this.myAgentTile, tile) && !this.agent.getBlockedGems().contains(tile))
                this.tileTypeListMap.get(type).add(tile);
        } else
                this.tileTypeListMap.get(type).add(tile);
    }

    private void initCollectedGemsMap() {
        int index = 0;
        for (TileType type : TileType.values()) {
            if (type.equals(TileType.YELLOW_GEM) || type.equals(TileType.GREEN_GEM) ||
                    type.equals(TileType.RED_GEM) || type.equals(TileType.BLUE_GEM)) {
                if (this.agent.getAgentCount() == 2) {
                    if (this.agent.getCharacter() == 'A') {
                        this.myAgentCollectedGemsMap.put(type, this.agent.getAgentGems()[0][index]);
                        this.opponentCollectedGemsMap.put(type, this.agent.getAgentGems()[1][index]);
                    } else {
                        this.myAgentCollectedGemsMap.put(type, this.agent.getAgentGems()[1][index]);
                        this.opponentCollectedGemsMap.put(type, this.agent.getAgentGems()[0][index]);
                    }
                } else {
                    this.myAgentCollectedGemsMap.put(type, this.agent.getAgentGems()[0][index]);
                }
                index++;
            }
        }
    }

    public TileType getTileType(int x, int y) {
        if (this.agent.getGrid()[x][y].contains(TileType.EMPTY.getValue()))
            return TileType.EMPTY;
        else if (this.agent.getGrid()[x][y].contains(TileType.TELEPORT.getValue()))
            return TileType.TELEPORT;
        else if (this.agent.getGrid()[x][y].contains(TileType.YELLOW_GEM.getValue()))
            return TileType.YELLOW_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.GREEN_GEM.getValue()))
            return TileType.GREEN_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.RED_GEM.getValue()))
            return TileType.RED_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.BLUE_GEM.getValue()))
            return TileType.BLUE_GEM;
        else if (this.agent.getGrid()[x][y].contains(TileType.WALL.getValue()))
            return TileType.WALL;
        else throw new IllegalStateException("Tile isn't valid!");
    }

    public boolean possibleToGetGem(Tile agentTile, Tile goalTile) {
        int minCostToGoal = heuristic(agentTile, goalTile);
        int myAgentScore = this.agent.getAgentScores()[0];
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
                return this.myAgentCollectedGemsMap.get(TileType.YELLOW_GEM) < YellowGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else if (goalTile.getType().equals(TileType.GREEN_GEM)) {
                return this.myAgentCollectedGemsMap.get(TileType.GREEN_GEM) < GreenGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else if (goalTile.getType().equals(TileType.RED_GEM)) {
                return this.myAgentCollectedGemsMap.get(TileType.RED_GEM) < RedGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else if (goalTile.getType().equals(TileType.BLUE_GEM)) {
                return this.myAgentCollectedGemsMap.get(TileType.BLUE_GEM) < BlueGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            } else throw new IllegalStateException("goal should be gem!");
        }
        return false;
    }

    public int heuristic(Tile start, Tile end) {
        return Math.abs(start.getX() - end.getX()) + Math.abs(start.getY() - end.getY());
    }


    /*
     a branch and bound algorithm to get optimal order of picking gems
     */
    public List<Tile> getOptimalOrder(Tile source, List<Tile> destinationGems) {
        if (destinationGems == null)
            return null;

        PriorityQueue<ComplexBFSNode> queue = new PriorityQueue<>();
        List<Tile> pickedGems = new LinkedList<>();
        ComplexBFSNode startNode = new ComplexBFSNode(0, 0, source, pickedGems, destinationGems);
        startNode.setBound(bound(startNode));
        queue.add(startNode);
        int minimumCost = Integer.MAX_VALUE;
        ComplexBFSNode bestNode = new ComplexBFSNode(0, 0, source, pickedGems, destinationGems);

        while (!queue.isEmpty()) {
            ComplexBFSNode current = queue.remove();
            if (current.getBound() < minimumCost) {
                for (Tile childTile : current.getRemainedGems()) {
                    int costFromCurrentToChild = costFromParentToChild(current.getCurrentTile(), childTile);
                    List<Tile> childPickedGems = new LinkedList<>(current.getPickedGems());
                    childPickedGems.add(childTile);
                    List<Tile> remainedChildGems = new LinkedList<>(current.getRemainedGems());
                    remainedChildGems.remove(childTile);
                    ComplexBFSNode childNode = new ComplexBFSNode(current.getLevel() + 1,
                            current.getCostUntilHere() + costFromCurrentToChild,
                            childTile,
                            childPickedGems,
                            remainedChildGems);
                    childNode.setBound(bound(childNode));

                    if (childNode.getCostUntilHere() < minimumCost && childNode.getRemainedGems().size() == 0) {
                        minimumCost = childNode.getCostUntilHere();
                        bestNode = childNode;
                    }

                    if (bound(childNode) < minimumCost)
                        queue.add(childNode);
                }
            }
        }

        return bestNode.getPickedGems();
    }

    private int costFromParentToChild(Tile parent, Tile child) {
        BFSSearchResponse bfsSearchResponse = bfsSearch(parent, child);
        if (bfsSearchResponse != null) return bfsSearchResponse.getCost();
        else throw new NullPointerException("bfs response is null!");
    }

    private int bound(ComplexBFSNode node) {
        List<Tile> remainedGems = new LinkedList<>(node.getRemainedGems());
        remainedGems.add(node.getCurrentTile());
        int costUntilHere = node.getCostUntilHere();
        int minRemainedCost = 0;
        for (int i = 0; i < remainedGems.size(); i++) {
            int min = Integer.MAX_VALUE;
            for (int j = 0; j <remainedGems.size(); j++) {
                if (i == j)
                    continue;
                Tile iTile = remainedGems.get(i);
                Tile jTile = remainedGems.get(j);
                int cost = Math.abs(iTile.getX() - jTile.getX()) + Math.abs(iTile.getY() - jTile.getY());
                if (cost < min)
                    min = cost;
            }
            minRemainedCost += min;
        }

        return costUntilHere + minRemainedCost;
    }

    public List<Tile> BFSToGetTopNearestGems(Tile start, int maxGemsToExplore) {
        PriorityQueue<SimpleBFSNode> frontier = new PriorityQueue<>();
        Set<Tile> exploredTiles = new HashSet<>();
        List<Tile> exploredGems = new LinkedList<>();
        List<Tile> cameFrom = new LinkedList<>();
        SimpleBFSNode startNode = new SimpleBFSNode(0, 0, start, cameFrom);
        List<Tile> allGems = concatenateLists(
                this.tileTypeListMap.get(TileType.YELLOW_GEM),
                this.tileTypeListMap.get(TileType.GREEN_GEM),
                this.tileTypeListMap.get(TileType.RED_GEM),
                this.tileTypeListMap.get(TileType.BLUE_GEM));

        frontier.add(startNode);
        exploredTiles.add(start);

        while (!frontier.isEmpty()) {
            SimpleBFSNode current = frontier.remove();

            if (exploredGems.size() == maxGemsToExplore || exploredGems.size() == allGems.size())
                return exploredGems;

            if ((current.getCurrentTile().getType().equals(TileType.YELLOW_GEM) ||
                    current.getCurrentTile().getType().equals(TileType.GREEN_GEM) ||
                    current.getCurrentTile().getType().equals(TileType.RED_GEM) ||
                    current.getCurrentTile().getType().equals(TileType.BLUE_GEM)) &&
                    allGems.contains(current.getCurrentTile())) {
                exploredGems.add(current.getCurrentTile());
            }
            exploredTiles.add(current.getCurrentTile());

            for (SimpleBFSNode successor : getNeighbors(current)) {

                if (!exploredTiles.contains(successor.getCurrentTile()) && !(frontier.contains(successor))) {
                    frontier.add(successor);
                } else if (frontier.contains(successor)) {
                    frontier.forEach(node -> {
                        if (node.equals(successor) && node.getCostUntilHere() > successor.getCostUntilHere()) {
                            updateFrontier(successor, node);
                        }
                    });
                }
            }
        }

        return exploredGems;
    }

    public BFSSearchResponse bfsSearch(Tile source, Tile destination) {
        PriorityQueue<SimpleBFSNode> frontier = new PriorityQueue<>();
        Set<Tile> exploredSet = new HashSet<>();

        List<Tile> cameFrom = new LinkedList<>();
        frontier.add(new SimpleBFSNode(0, 0, source, cameFrom));
        exploredSet.add(source);

        while (!frontier.isEmpty()) {
            SimpleBFSNode current = frontier.remove();

            if (current.getCurrentTile().equals(destination))
                return new BFSSearchResponse(
                        current.getCameFrom(),
                        current.getCostUntilHere(),
                        destination,
                        source,
                        current.getLevel());

            exploredSet.add(current.getCurrentTile());
            for (SimpleBFSNode successor : getNeighbors(current)) {

                if (!exploredSet.contains(successor.getCurrentTile()) && !(frontier.contains(successor))) {
                    frontier.add(successor);
                } else if (frontier.contains(successor)) {
                    frontier.forEach(node -> {
                        if (node.equals(successor) && node.getCostUntilHere() > successor.getCostUntilHere()) {
                            updateFrontier(successor, node);
                        }
                    });
                }
            }
        }
        return null;
    }

    private void updateFrontier(SimpleBFSNode successor, SimpleBFSNode node) {
        node.setLevel(successor.getLevel());
        node.setCameFrom(successor.getCameFrom());
        node.setCostUntilHere(successor.getCostUntilHere());
    }

    private List<SimpleBFSNode> getNeighbors(SimpleBFSNode current) {
        List<SimpleBFSNode> neighbors = new LinkedList<>();
        if (current.getCurrentTile().getType().equals(TileType.TELEPORT)) {
            neighbors.addAll(getTeleportNeighbors(current));
        }
        neighbors.addAll(getPhysicalNeighbors(current));
        return neighbors;
    }

    private List<SimpleBFSNode> getPhysicalNeighbors(SimpleBFSNode currentNode) {
        List<SimpleBFSNode> neighbors = new LinkedList<>();
        Tile currentTile = currentNode.getCurrentTile();
        if (currentTile.getY() - 1 >= 0) {
            addNeighborTile(neighbors, currentTile.getX(), currentTile.getY() - 1, currentNode);
        }
        if (currentTile.getY() + 1 < this.agent.getGrid()[0].length) {
            addNeighborTile(neighbors, currentTile.getX(), currentTile.getY() + 1, currentNode);
        }
        if (currentTile.getX() - 1 >= 0) {
            addNeighborTile(neighbors, currentTile.getX() - 1, currentTile.getY(), currentNode);
        }
        if (currentTile.getX() + 1 < this.agent.getGrid().length) {
            addNeighborTile(neighbors, currentTile.getX() + 1, currentTile.getY(), currentNode);
        }
        return neighbors;
    }

    private void addNeighborTile(List<SimpleBFSNode> neighbors, int x, int y, SimpleBFSNode currentNode) {
        Tile neighbor;
        TileType type = this.getTileType(x, y);
        if (type != TileType.WALL) {
            neighbor = new Tile(x, y, type);
            List<Tile> cameFrom = new LinkedList<>(currentNode.getCameFrom());
            cameFrom.add(neighbor);
            neighbors.add(new SimpleBFSNode(currentNode.getLevel() + 1, currentNode.getCostUntilHere() + 1,
                    neighbor, cameFrom));
        }
    }

    private List<SimpleBFSNode> getTeleportNeighbors(SimpleBFSNode currentTeleportNode) {
        List<Tile> neighborTeleportTiles = new LinkedList<>(this.getTileTypeListMap().get(TileType.TELEPORT));
        neighborTeleportTiles.remove(currentTeleportNode.getCurrentTile());
        List<SimpleBFSNode> neighborTeleportNodes = new LinkedList<>();
        for (Tile teleportTile : neighborTeleportTiles) {
            List<Tile> cameFrom = new LinkedList<>(currentTeleportNode.getCameFrom());
            cameFrom.add(teleportTile);
            neighborTeleportNodes.add(new SimpleBFSNode(currentTeleportNode.getLevel() + 1,
                    currentTeleportNode.getCostUntilHere() + this.getTeleportCost(),
                    teleportTile, cameFrom));
        }
        return neighborTeleportNodes;
    }

    @SafeVarargs
    private List<Tile> concatenateLists(List<Tile>... lists) {
        List<Tile> result = new ArrayList<>();
        for (List<Tile> list : lists)
            result.addAll(list);
        return result;
    }

    public Agent getAgent() {
        return agent;
    }

    public void setAgent(Agent agent) {
        this.agent = agent;
    }

    public Map<TileType, List<Tile>> getTileTypeListMap() {
        return tileTypeListMap;
    }

    public void setTileTypeListMap(Map<TileType, List<Tile>> tileTypeListMap) {
        this.tileTypeListMap = tileTypeListMap;
    }

    public Tile getMyAgentTile() {
        return myAgentTile;
    }

    public void setMyAgentTile(Tile myAgentTile) {
        this.myAgentTile = myAgentTile;
    }

    public int getTeleportCost() {
        return teleportCost;
    }

    public Map<TileType, Integer> getMyAgentCollectedGemsMap() {
        return myAgentCollectedGemsMap;
    }

    public void setMyAgentCollectedGemsMap(Map<TileType, Integer> myAgentCollectedGemsMap) {
        this.myAgentCollectedGemsMap = myAgentCollectedGemsMap;
    }

    public Map<TileType, Integer> getOpponentCollectedGemsMap() {
        return opponentCollectedGemsMap;
    }

    public void setOpponentCollectedGemsMap(Map<TileType, Integer> opponentCollectedGemsMap) {
        this.opponentCollectedGemsMap = opponentCollectedGemsMap;
    }
}

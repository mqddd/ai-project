package com.example.logic;

import com.example.Utils;
import com.example.model.*;

import java.util.*;

public class OrderingAnalyzer {

    private Utils utils;

    public OrderingAnalyzer(Utils utils) {
        this.utils = utils;
    }

    public Tile getOptimalGoal2(Tile myAgentTile) {
        List<Tile> gemsToBeOrdered = BFSToGetTopNearestGems(myAgentTile, 5);
        List<Tile> optimalOrder = getOptimalOrder(myAgentTile, gemsToBeOrdered);
        System.out.println("optimal order: " + optimalOrder);
        if (!optimalOrder.isEmpty())
            return optimalOrder.get(0);
        else return null;
    }

    /*
     a branch and bound algorithm to get optimal order of picking gems
     */
    private List<Tile> getOptimalOrder(Tile myAgentTile, List<Tile> gemsToBeOrdered) {
        if (gemsToBeOrdered == null)
            return null;

        PriorityQueue<Node2> queue = new PriorityQueue<>();
        List<Tile> pickedGems = new LinkedList<>();
        Node2 startNode = new Node2(0, 0, pickedGems, myAgentTile, gemsToBeOrdered);
        startNode.bound = bound(startNode);
        queue.add(startNode);
        int minimumCost = Integer.MAX_VALUE;
        Node2 bestNode = new Node2(0, 0, pickedGems, myAgentTile, gemsToBeOrdered);

        while (!queue.isEmpty()) {
            Node2 current = queue.remove();
            if (current.bound < minimumCost) {
                for (Tile childTile : current.remainedGems) {
                    int costFromCurrentToChild = costFromParentToChild(current.currentTile, childTile);
                    List<Tile> childPickedGems = new LinkedList<>(current.pickedGems);
                    childPickedGems.add(childTile);
                    List<Tile> remainedChildGems = new LinkedList<>(current.remainedGems);
                    remainedChildGems.remove(childTile);
                    Node2 childNode = new Node2(current.level + 1,
                            current.costUntilHere + costFromCurrentToChild,
                            childPickedGems,
                            childTile,
                            remainedChildGems);
                    childNode.bound = bound(childNode);

                    if (childNode.costUntilHere < minimumCost && childNode.remainedGems.size() == 0) {
                        minimumCost = childNode.costUntilHere;
                        bestNode = childNode;
                    }

                    if (bound(childNode) < minimumCost)
                        queue.add(childNode);
                }
            }
        }

        return bestNode.pickedGems;
    }

    private int costFromParentToChild(Tile parent, Tile child) {
        BFSSearchResponse bfsSearchResponse = bfsSearch(parent, child);
        if (bfsSearchResponse != null) return bfsSearchResponse.getCost();
        else throw new NullPointerException("bfs response is null!");
    }

    private int bound(Node2 node) {
        List<Tile> remainedGems = new LinkedList<>(node.remainedGems);
        remainedGems.add(node.currentTile);
        int costUntilHere = node.costUntilHere;
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

    private BFSSearchResponse bfsSearch(Tile myAgentTile, Tile goalTile) {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Set<Tile> exploredSet = new HashSet<>();

        List<Tile> cameFrom = new LinkedList<>();
        frontier.add(new Node(0, 0, cameFrom, myAgentTile));
        exploredSet.add(myAgentTile);

        while (!frontier.isEmpty()) {
            Node current = frontier.remove();

            if (current.currentTile.equals(goalTile))
                return new BFSSearchResponse(
                        current.cameFrom,
                        current.costUntilHere,
                        goalTile,
                        myAgentTile,
                        current.level);

            exploredSet.add(current.currentTile);
            for (Node successor : getNeighbors(current)) {

                if (!exploredSet.contains(successor.currentTile) && !(frontier.contains(successor))) {
                    frontier.add(successor);
                } else if (frontier.contains(successor)) {
                    frontier.forEach(node -> {
                        if (node.equals(successor) && node.costUntilHere > successor.costUntilHere) {
                            updateFrontier(successor, node);
                        }
                    });
                }
            }
        }
        return null;
    }

    private List<Tile> BFSToGetTopNearestGems(Tile start, int maxGemsToExplore) {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Set<Tile> exploredTiles = new HashSet<>();
        List<Tile> exploredGems = new LinkedList<>();
        List<Tile> cameFrom = new LinkedList<>();
        Node startNode = new Node(0, 0, cameFrom, start);
        List<Tile> allGems = concatenateLists(
                utils.getTileTypeListMap().get(TileType.YELLOW_GEM),
                utils.getTileTypeListMap().get(TileType.GREEN_GEM),
                utils.getTileTypeListMap().get(TileType.RED_GEM),
                utils.getTileTypeListMap().get(TileType.BLUE_GEM));

        frontier.add(startNode);
        exploredTiles.add(start);

        while (!frontier.isEmpty()) {
            Node current = frontier.remove();

            if (exploredGems.size() == maxGemsToExplore || exploredGems.size() == allGems.size())
                return exploredGems;

            if ((current.currentTile.getType().equals(TileType.YELLOW_GEM) ||
                    current.currentTile.getType().equals(TileType.GREEN_GEM) ||
                    current.currentTile.getType().equals(TileType.RED_GEM) ||
                    current.currentTile.getType().equals(TileType.BLUE_GEM)) &&
                    allGems.contains(current.currentTile)) {
                exploredGems.add(current.currentTile);
            }
            exploredTiles.add(current.currentTile);

            for (Node successor : getNeighbors(current)) {

                if (!exploredTiles.contains(successor.currentTile) && !(frontier.contains(successor))) {
                    frontier.add(successor);
                } else if (frontier.contains(successor)) {
                    frontier.forEach(node -> {
                        if (node.equals(successor) && node.costUntilHere > successor.costUntilHere) {
                            updateFrontier(successor, node);
                        }
                    });
                }
            }
        }

        return exploredGems;
    }

    private void updateFrontier(Node successor, Node node) {
        node.level = successor.level;
        node.cameFrom = successor.cameFrom;
        node.costUntilHere = successor.costUntilHere;
    }

    private List<Node> getNeighbors(Node current) {
        List<Node> neighbors = new LinkedList<>();
        if (current.currentTile.getType().equals(TileType.TELEPORT)) {
            neighbors.addAll(getTeleportNeighbors(current));
        }
        neighbors.addAll(getPhysicalNeighbors(current));
        return neighbors;
    }

    private List<Node> getPhysicalNeighbors(Node currentNode) {
        List<Node> neighbors = new LinkedList<>();
        Tile currentTile = currentNode.currentTile;
        if (currentTile.getY() - 1 >= 0) {
            addNeighborTile(neighbors, currentTile.getX(), currentTile.getY() - 1, currentNode);
        }
        if (currentTile.getY() + 1 < utils.getAgent().getGrid()[0].length) {
            addNeighborTile(neighbors, currentTile.getX(), currentTile.getY() + 1, currentNode);
        }
        if (currentTile.getX() - 1 >= 0) {
            addNeighborTile(neighbors, currentTile.getX() - 1, currentTile.getY(), currentNode);
        }
        if (currentTile.getX() + 1 < utils.getAgent().getGrid().length) {
            addNeighborTile(neighbors, currentTile.getX() + 1, currentTile.getY(), currentNode);
        }
        return neighbors;
    }

    private void addNeighborTile(List<Node> neighbors, int x, int y, Node currentNode) {
        Tile neighbor;
        TileType type = utils.getTileType(x, y);
        if (type != TileType.WALL) {
            neighbor = new Tile(x, y, type);
            List<Tile> cameFrom = new LinkedList<>(currentNode.cameFrom);
            cameFrom.add(neighbor);

            neighbors.add(new Node(currentNode.level + 1, currentNode.costUntilHere + 1,
                    cameFrom, neighbor));
        }
    }

    private List<Node> getTeleportNeighbors(Node currentTeleportNode) {
        List<Tile> neighborTeleportTiles = new LinkedList<>(utils.getTileTypeListMap().get(TileType.TELEPORT));
        neighborTeleportTiles.remove(currentTeleportNode.currentTile);
        List<Node> neighborTeleportNodes = new LinkedList<>();
        for (Tile teleportTile : neighborTeleportTiles) {
            List<Tile> cameFrom = new LinkedList<>(currentTeleportNode.cameFrom);
            cameFrom.add(teleportTile);
            neighborTeleportNodes.add(new Node(currentTeleportNode.level + 1,
                    currentTeleportNode.costUntilHere + utils.getTeleportCost(),
                    cameFrom, teleportTile));
        }
        return neighborTeleportNodes;
    }

    // phase one

    public Tile getOptimalGoal(Tile myAgentTile) {
        List<Tile> allGems = concatenateLists(
                utils.getTileTypeListMap().get(TileType.YELLOW_GEM),
                utils.getTileTypeListMap().get(TileType.GREEN_GEM),
                utils.getTileTypeListMap().get(TileType.RED_GEM),
                utils.getTileTypeListMap().get(TileType.BLUE_GEM));

        int minCost = Integer.MAX_VALUE;
        Tile optimalGoal = null;
        if (!allGems.isEmpty()) {
            if (possibleToGetGem(myAgentTile, allGems.get(0)))
                optimalGoal = allGems.get(0);
        } else return null;
        for (Tile gem : allGems) {
            int costOfGem = this.heuristic(myAgentTile, gem);
            if (costOfGem <= minCost && possibleToGetGem(myAgentTile, gem)) {
                if (costOfGem == minCost) {
                    if (optimalGoal == null) continue;
                    if (gem.getType().ordinal() > optimalGoal.getType().ordinal()) {
                        optimalGoal = gem;
                    }
                } else {
                    minCost = costOfGem;
                    optimalGoal = gem;
                }
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
                return utils.getAgent().getCollectedGemsMap().get(TileType.YELLOW_GEM) < YellowGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else if (goalTile.getType().equals(TileType.GREEN_GEM)) {
                return utils.getAgent().getCollectedGemsMap().get(TileType.GREEN_GEM) < GreenGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else if (goalTile.getType().equals(TileType.RED_GEM)) {
                return utils.getAgent().getCollectedGemsMap().get(TileType.RED_GEM) < RedGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else if (goalTile.getType().equals(TileType.BLUE_GEM)) {
                return utils.getAgent().getCollectedGemsMap().get(TileType.BLUE_GEM) < BlueGemTile.MAXIMUM_ACHIEVABLE_TIMES.getValue();
            }
            else throw new IllegalStateException("goal should be gem!");
        }
        return false;
    }
}

class Node2 implements Comparable<Node2> {
    int level;
    int costUntilHere;
    List<Tile> pickedGems;
    Tile currentTile;
    List<Tile> remainedGems;
    int bound;

    Node2(int level, int costUntilHere, List<Tile> pickedGems, Tile currentTile, List<Tile> remainedGems) {
        this.level = level;
        this.costUntilHere = costUntilHere;
        this.pickedGems = pickedGems;
        this.currentTile = currentTile;
        this.remainedGems = remainedGems;
    }

    @Override
    public int hashCode() {
        return (20 * this.currentTile.getX()) + this.currentTile.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Node2 node = (Node2) obj;
        return node.currentTile.getX() == this.currentTile.getX()
                && node.currentTile.getY() == this.currentTile.getY();
    }

    @Override
    public int compareTo(Node2 node) {
        return Integer.compare(this.costUntilHere, node.costUntilHere);
    }
}

package com.example.logic;

import com.example.BaseAgent;
import com.example.Utils;
import com.example.model.*;

import java.util.*;

public class RoutingAnalyzer {

    private Utils utils;
    private OrderingAnalyzer orderingAnalyzer;

    public RoutingAnalyzer(Utils utils, OrderingAnalyzer orderingAnalyzer) {
        this.utils = utils;
        this.orderingAnalyzer = orderingAnalyzer;
    }

    public BaseAgent.Action getNextAction(Tile myAgentTile, Tile goalTile) {
        BFSSearchResponse bfsSearchResponse = bfsSearch(myAgentTile, goalTile);
        while (bfsSearchResponse == null) {
            utils.getAgent().getBlockedGems().add(goalTile);
            utils.resetProperties();
            goalTile = orderingAnalyzer.getOptimalGoal(myAgentTile);
            if (goalTile == null)
                break;
            bfsSearchResponse = bfsSearch(myAgentTile, goalTile);
        }
        List<Tile> path;
        if (bfsSearchResponse != null) {
            path = bfsSearchResponse.getPath();
            if (path.size() == 1) {
                switch (goalTile.getType()) {
                    case YELLOW_GEM:
                        if (utils.getAgent().getAgentScores()[0] - 1 >= YellowGemTile.REQUIRED_SCORE.getValue())
                            utils.getAgent().setCollectedYellowGems(utils.getAgent().getCollectedYellowGems() + 1);
                        break;
                    case GREEN_GEM:
                        if (utils.getAgent().getAgentScores()[0] - 1 >= GreenGemTile.REQUIRED_SCORE.getValue())
                            utils.getAgent().setCollectedGreenGems(utils.getAgent().getCollectedGreenGems() + 1);
                        break;
                    case RED_GEM:
                        if (utils.getAgent().getAgentScores()[0] - 1 >= RedGemTile.REQUIRED_SCORE.getValue())
                            utils.getAgent().setCollectedRedGems(utils.getAgent().getCollectedRedGems() + 1);
                        break;
                    case BLUE_GEM:
                        if (utils.getAgent().getAgentScores()[0] - 1 >= BlueGemTile.REQUIRED_SCORE.getValue())
                            utils.getAgent().setCollectedBlueGems(utils.getAgent().getCollectedBlueGems() + 1);
                        break;
                    default:
                        throw new IllegalStateException("goal type should be of gem!");
                }
            }
            System.out.println(utils.getAgent().getTurnCount());
            System.out.println("agent: " + myAgentTile.toString());
            System.out.println("goal: " + goalTile.toString());
            System.out.println("path: " + path);
            System.out.println("cost to gaol: " + bfsSearchResponse.getCost());
            System.out.println("agent score: " + utils.getAgent().getAgentScores()[0]);
            System.out.println("_______________________________________");
        } else {
            return BaseAgent.Action.NoOp;
        }
        if (path.isEmpty())
            return BaseAgent.Action.NoOp;
        return getNextStep(myAgentTile, path.get(0));
    }

    private BaseAgent.Action getNextStep(Tile source, Tile nextStep) {
        if (source.getX() == nextStep.getX() && source.getY() - nextStep.getY() == -1)
            return BaseAgent.Action.Right;
        else if (source.getX() == nextStep.getX() && source.getY() - nextStep.getY() == 1)
            return BaseAgent.Action.Left;
        else if (source.getY() == nextStep.getY() && source.getX() - nextStep.getX() == -1)
            return BaseAgent.Action.Down;
        else if (source.getY() == nextStep.getY() && source.getX() - nextStep.getX() == 1)
            return BaseAgent.Action.Up;
        else if (source.getType().equals(TileType.TELEPORT) && nextStep.getType().equals(TileType.TELEPORT))
            return BaseAgent.Action.Teleport;
        else throw new IllegalStateException("unknown step!");
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
        TileType type;
        if (utils.getAgent().getGrid()[x][y].contains(TileType.EMPTY.getValue())) {
            type = TileType.EMPTY;
        } else if (utils.getAgent().getGrid()[x][y].equals(TileType.WALL.getValue())) {
            type = TileType.WALL;
        } else if (utils.getAgent().getGrid()[x][y].contains(TileType.YELLOW_GEM.getValue())) {
            type = TileType.YELLOW_GEM;
        } else if (utils.getAgent().getGrid()[x][y].contains(TileType.GREEN_GEM.getValue())) {
            type = TileType.GREEN_GEM;
        } else if (utils.getAgent().getGrid()[x][y].contains(TileType.RED_GEM.getValue())) {
            type = TileType.RED_GEM;
        } else if (utils.getAgent().getGrid()[x][y].contains(TileType.BLUE_GEM.getValue())) {
            type = TileType.BLUE_GEM;
        } else if (utils.getAgent().getGrid()[x][y].contains(TileType.TELEPORT.getValue())) {
            type = TileType.TELEPORT;
        } else throw new IllegalStateException("Tile type isn't known!");
        if (type != TileType.WALL) {
            neighbor = new Tile(x, y, type);
            List<Tile> cameFrom = new LinkedList<>(currentNode.cameFrom);
            cameFrom.add(neighbor);

            neighbors.add(new Node(currentNode.level + 1, currentNode.costUntilHere + 1,
                    cameFrom, neighbor));
        }
    }

    private List<Node> getTeleportNeighbors(Node currentTeleportNode) {
        List<Tile> neighborTeleportTiles = new LinkedList<>(utils.getTeleportTiles());
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
}

class Node implements Comparable<Node> {
    int level;
    int costUntilHere;
    List<Tile> cameFrom;
    Tile currentTile;

    Node(int level, int costUntilHere, List<Tile> cameFrom, Tile currentTile) {
        this.level = level;
        this.costUntilHere = costUntilHere;
        this.cameFrom = cameFrom;
        this.currentTile = currentTile;
    }

    @Override
    public int hashCode() {
        return (20 * this.currentTile.getX()) + this.currentTile.getY();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || obj.getClass() != this.getClass()) return false;
        Node node = (Node) obj;
        return node.currentTile.getX() == this.currentTile.getX()
                && node.currentTile.getY() == this.currentTile.getY();
    }

    @Override
    public int compareTo(Node node) {
        return Integer.compare(this.costUntilHere, node.costUntilHere);
    }
}

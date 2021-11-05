package com.example.service;

import com.example.BaseAgent;
import com.example.Utils;
import com.example.model.Tile;
import com.example.model.TileType;
import com.example.model.dto.BFSSearchResponseDTO;

import java.util.*;

public class RoutingAnalyzer {

    private Utils utils;

    public RoutingAnalyzer(Utils utils) {
        this.utils = utils;
    }

    public BaseAgent.Action getNextAction(Tile myAgentTile, Tile goalTile) {
        BFSSearchResponseDTO bfsSearchResponseDTO = bfsSearch(myAgentTile, goalTile);
        List<Tile> path;
        if (bfsSearchResponseDTO != null) {
            path = bfsSearchResponseDTO.getPath();
            System.out.println(utils.getMyAgent().getTurnCount());
            System.out.println("agent: " + myAgentTile.toString());
            System.out.println("goal: " + goalTile.toString());
            System.out.println("path: " + path);
            System.out.println("cost to gaol: " + bfsSearchResponseDTO.getCost());
            System.out.println("agent score: " + utils.getMyAgent().getAgentScores()[0]);
            System.out.println("_______________________________________");
        } else {
            return BaseAgent.Action.NoOp;
        }
        if (path.isEmpty()) {
            return BaseAgent.Action.NoOp;
        }
        return getNextStep(myAgentTile, path.get(0));
    }

    private BaseAgent.Action getNextStep(Tile source, Tile nextStep) {
        if (source.getX() == nextStep.getX() && source.getY() - nextStep.getY() == -1)
            return BaseAgent.Action.Right;
        else if (source.getX() == nextStep.getX() && source.getY() - nextStep.getY() == 1)
            return BaseAgent.Action.Left;
        else if (source.getY() == nextStep.getY() && source.getX() - nextStep.getX() == -1)
            return BaseAgent.Action.Down;
        else if (source.getY() == nextStep.getY() && source.getX() - nextStep.getX() == 1) {
            return BaseAgent.Action.Up;
        }
        else if (source.getType().equals(TileType.TELEPORT) && nextStep.getType().equals(TileType.TELEPORT))
            return BaseAgent.Action.Teleport;
        else throw new IllegalStateException("unknown step!");
    }

    private BFSSearchResponseDTO bfsSearch(Tile myAgentTile, Tile goalTile) {
        PriorityQueue<Node> frontier = new PriorityQueue<>();
        Set<Tile> exploredSet = new HashSet<>();

        List<Tile> cameFrom = new LinkedList<>();
        frontier.add(new Node(0, 0, cameFrom, myAgentTile));
        exploredSet.add(myAgentTile);

        while (!frontier.isEmpty()) {
            Node current = frontier.remove();

            if (current.currentTile.equals(goalTile))
                return new BFSSearchResponseDTO(
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
                            System.out.println("node in frontier updated!");
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
        Tile leftTile;
        Tile rightTile;
        Tile upTile;
        Tile downTile;
        if (currentTile.getY() - 1 >= 0) {
            TileType type;
            if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() - 1].contains("E")) {
                type = TileType.EMPTY;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() - 1].equals("W")) {
                type = TileType.WALL;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() - 1].contains("1")) {
                type = TileType.YELLOW_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() - 1].contains("2")) {
                type = TileType.GREEN_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() - 1].contains("3")) {
                type = TileType.RED_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() - 1].contains("4")) {
                type = TileType.BLUE_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() - 1].contains("T")) {
                type = TileType.TELEPORT;
            } else throw new IllegalStateException("Tile type isn't known!");
            if (type != TileType.WALL) {
                leftTile = new Tile(currentTile.getX(), currentTile.getY() - 1, type);
                List<Tile> cameFrom = new LinkedList<>(currentNode.cameFrom);
                cameFrom.add(leftTile);
                neighbors.add(new Node(currentNode.level + 1, currentNode.costUntilHere + 1,
                        cameFrom, leftTile));
            }
        }
        if (currentTile.getY() + 1 < utils.getMyAgent().getGrid()[0].length) {
            TileType type;
            if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() + 1].contains("E")) {
                type = TileType.EMPTY;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() + 1].equals("W")) {
                type = TileType.WALL;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() + 1].contains("1")) {
                type = TileType.YELLOW_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() + 1].contains("2")) {
                type = TileType.GREEN_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() + 1].contains("3")) {
                type = TileType.RED_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() + 1].contains("4")) {
                type = TileType.BLUE_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX()][currentTile.getY() + 1].contains("T")) {
                type = TileType.TELEPORT;
            } else throw new IllegalStateException("Tile type isn't known!");
            if (type != TileType.WALL) {
                rightTile = new Tile(currentTile.getX(), currentTile.getY() + 1, type);
                List<Tile> cameFrom = new LinkedList<>(currentNode.cameFrom);
                cameFrom.add(rightTile);
                neighbors.add(new Node(currentNode.level + 1, currentNode.costUntilHere + 1,
                        cameFrom, rightTile));
            }
        }
        if (currentTile.getX() - 1 >= 0) {
            TileType type;
            if (utils.getMyAgent().getGrid()[currentTile.getX() - 1][currentTile.getY()].contains("E")) {
                type = TileType.EMPTY;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() - 1][currentTile.getY()].equals("W")) {
                type = TileType.WALL;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() - 1][currentTile.getY()].contains("1")) {
                type = TileType.YELLOW_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() - 1][currentTile.getY()].contains("2")) {
                type = TileType.GREEN_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() - 1][currentTile.getY()].contains("3")) {
                type = TileType.RED_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() - 1][currentTile.getY()].contains("4")) {
                type = TileType.BLUE_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() - 1][currentTile.getY()].contains("T")) {
                type = TileType.TELEPORT;
            } else throw new IllegalStateException("Tile type isn't known!");
            if (type != TileType.WALL) {
                upTile = new Tile(currentTile.getX() - 1, currentTile.getY(), type);
                List<Tile> cameFrom = new LinkedList<>(currentNode.cameFrom);
                cameFrom.add(upTile);
                neighbors.add(new Node(currentNode.level + 1, currentNode.costUntilHere + 1,
                        cameFrom, upTile));
            }
        }
        if (currentTile.getX() + 1 < utils.getMyAgent().getGrid().length) {
            TileType type;
            if (utils.getMyAgent().getGrid()[currentTile.getX() + 1][currentTile.getY()].contains("E")) {
                type = TileType.EMPTY;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() + 1][currentTile.getY()].equals("W")) {
                type = TileType.WALL;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() + 1][currentTile.getY()].contains("1")) {
                type = TileType.YELLOW_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() + 1][currentTile.getY()].contains("2")) {
                type = TileType.GREEN_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() + 1][currentTile.getY()].contains("3")) {
                type = TileType.RED_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() + 1][currentTile.getY()].contains("4")) {
                type = TileType.BLUE_GEM;
            } else if (utils.getMyAgent().getGrid()[currentTile.getX() + 1][currentTile.getY()].contains("T")) {
                type = TileType.TELEPORT;
            } else throw new IllegalStateException("Tile type isn't known!");
            if (type != TileType.WALL) {
                downTile = new Tile(currentTile.getX() + 1, currentTile.getY(), type);
                List<Tile> cameFrom = new LinkedList<>(currentNode.cameFrom);
                cameFrom.add(downTile);
                neighbors.add(new Node(currentNode.level + 1, currentNode.costUntilHere + 1,
                        cameFrom, downTile));
            }
        }
        return neighbors;
    }

    private List<Node> getTeleportNeighbors(Node currentTeleportNode) {
        List<Tile> neighborTeleportTiles = new ArrayList<>(utils.getTeleportTiles());
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

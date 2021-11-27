package com.example;

import com.example.model.BlueGemTile;
import com.example.model.Tile;
import com.example.model.TileType;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Test {

    public static void main(String[] args) {
        Tile tile1 = new Tile(1, 222, TileType.WALL);
        Node node1 = new Node(0, 2, null, tile1);

        Tile tile2 = new Tile(1, 33, TileType.BLUE_GEM);
//        System.out.println(tile2.getType().REWARD.getValue());
        Node node2 = new Node(1, 22, null, tile2);

        List<Tile> tiles = new LinkedList<>();
        Set<Tile> tileSet = new HashSet<>();
       tileSet.add(tile1);
       tileSet.add(tile2);
        System.out.println(tileSet);

//        System.out.println(node1.equals(node2));
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
        return (20 * this.currentTile.getX()) + 20 * this.currentTile.getY();
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
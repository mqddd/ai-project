package com.example.model;

import java.util.List;

public class TeleportTile extends BaseTile {

    public static List<TeleportTile> connectedTeleportTiles;

    public TeleportTile(int x, int y) {
        super(x, y, TileType.TELEPORT);
    }

    public int costToTeleport() {
        return TeleportTile.connectedTeleportTiles.size() - 1;
    }
}

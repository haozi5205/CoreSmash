package com.breakthecore.tiles;

import com.breakthecore.GameController;
import com.breakthecore.tilemap.Tilemap;
import com.breakthecore.tilemap.TilemapManager;
import com.breakthecore.tilemap.TilemapTile;

import java.util.Random;

public class ColorBomb extends Tile implements Launchable {
    private static Random rand = new Random();

    public ColorBomb(int id) {
        super(id);
    }

    @Override
    public void onLaunch() {

    }

    @Override
    public void onCollide(MovingBall ball, TilemapTile tileHit, GameController controller) {
        int idToDestroy = tileHit.getTile() instanceof RegularTile ? tileHit.getTileID() : rand.nextInt(10);
        int collidedTileX = tileHit.getX();
        int collidedTileY = tileHit.getY();

        TilemapManager tmm = controller.getBehaviourPack().tilemapManager;

        for (int y = collidedTileY - 2; y < collidedTileY + 3; ++y) {
            for (int x = collidedTileX - 2; x < collidedTileX + 3; ++x) {
                if ((Tilemap.getTileDistance(x, y, collidedTileX, collidedTileY) < 3)) {
                    TilemapTile tmTile = tmm.getTilemapTile(tileHit.getLayerId(), x, y);
                    if (tmTile == null ||
                            (tmTile.getX() == 0 && tmTile.getY() == 0)) continue;
                    if (tmTile.getTileID() == idToDestroy) {
                        tmm.destroyDisconnectedTiles(tmTile);
                        tmm.removeTile(tmTile);
                    }
                }
            }
        }
    }
}

package com.breakthecore.tiles;

import com.breakthecore.GameController;
import com.breakthecore.tilemap.Tilemap;
import com.breakthecore.tilemap.TilemapManager;
import com.breakthecore.tilemap.TilemapTile;

public class BombTile extends Tile implements Launchable {

    BombTile(int id) {
        super(id);
    }

    @Override
    public void onLaunch() {

    }

    @Override
    public void onCollide(MovingBall movingBall, TilemapTile tileHit, GameController controller) {
        int collidedTileX = tileHit.getX();
        int collidedTileY = tileHit.getY();

        TilemapManager tmm = controller.getBehaviourPack().tilemapManager;
        for (int y = collidedTileY - 2; y < collidedTileY + 3; ++y) {
            for (int x = collidedTileX - 2; x < collidedTileX + 3; ++x) {
                if (Tilemap.getTileDistance(x, y, collidedTileX, collidedTileY) < 2) {
                    tmm.removeTile(tileHit.getLayerId(), x, y);
                }
            }
        }
    }
}

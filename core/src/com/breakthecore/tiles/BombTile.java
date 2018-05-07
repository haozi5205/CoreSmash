package com.breakthecore.tiles;

import com.breakthecore.Coords2D;
import com.breakthecore.GameController;
import com.breakthecore.tilemap.Tilemap;
import com.breakthecore.managers.CollisionDetector;
import com.breakthecore.tilemap.TilemapManager;
import com.breakthecore.tilemap.TilemapTile;

public class BombTile extends Tile {
    public BombTile() {
        super(TileDictionary.getIdOf(TileType.BOMB));
    }

    @Override
    public void onCollide(MovingBall movingBall, TilemapTile tileHit, GameController.BehaviourPowerPack pack) {
        Coords2D posT = tileHit.getRelativePosition();
        Tilemap tm = pack.tilemapManager.getTilemap(tileHit.getTilemapId());

        int collidedTileX = posT.x;
        int collidedTileY = posT.y;

        for (int y = collidedTileY - 2; y < collidedTileY + 3; ++y) {
            for (int x = collidedTileX - 2; x < collidedTileX + 3; ++x) {
                if (tm.getTileDistance(x, y, collidedTileX, collidedTileY) < 2) {
                    tm.destroyRelativeTile(x, y);
                }
            }
        }
    }

    @Override
    public void update(float delta) {

    }
}

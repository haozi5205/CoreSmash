package com.breakthecore.levels;

import com.breakthecore.managers.MovingTileManager;
import com.breakthecore.managers.StatsManager;
import com.breakthecore.managers.TilemapManager;

/**
 * Level is the interface used to manipulate the GameScreen for the needs of the different game levels.
 */

public interface Level {
    void initialize(StatsManager statsManager, TilemapManager tilemapManager, MovingTileManager movingTileManager);

    void update(float delta, TilemapManager tilemapManager);

    void end(boolean roundWon, StatsManager statsManager);

    int getLevelNumber();
}

package com.breakthecore.tiles;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Michail on 19/3/2018.
 */

public class MovingTile extends TileContainer {
    private float m_speed;
    private float scale;
    private boolean flag;

    public MovingTile(float x, float y, int speed) {
        this(new Vector2(x, y), speed);
    }

    public MovingTile(Vector2 pos, int speed) {
        scale = 1/2f;
        m_speed = speed * 100;
        positionInWorld.set(pos);
    }

    public MovingTile(Vector2 pos, int speed, Tile tile) {
        this(pos, speed);
        m_tile = tile;
    }

    public MovingTile(float x, float y, int speed, Tile tile) {
        this(x, y, speed);
        m_tile = tile;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public Tile getTile() {
        return m_tile;
    }

    public float getSpeed() {
        return m_speed;
    }

    public void setSpeed(float speed) {
        m_speed = speed * 100;
    }

    public float getScale() {
        return scale;
    }

    public boolean getFlag() {
        return flag;
    }

    public void setFlag(boolean x) {
        flag = x;
    }

    public void dispose() {
        flag = true;
    }

    public void moveBy(float x, float y) {
            positionInWorld.add(x, y);
    }

    public void update(float delta) {
        moveBy(0, m_speed * delta);
    }

    public Tile extractTile() {
        Tile res = m_tile;
        m_tile = null;
        return res;
    }

    public boolean hasTile() {
        return m_tile != null;
    }

    public int getColor() {
        return m_tile.getColor();
    }

}

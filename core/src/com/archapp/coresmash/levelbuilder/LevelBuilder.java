package com.archapp.coresmash.levelbuilder;

import com.archapp.coresmash.GameTarget;
import com.archapp.coresmash.managers.RenderManager;
import com.archapp.coresmash.tilemap.Map;
import com.archapp.coresmash.tilemap.TilemapTile;
import com.archapp.coresmash.tiles.TileFactory;
import com.archapp.coresmash.tiles.TileType;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

final public class LevelBuilder {
    private OrthographicCamera camera;
    private ScreenToWorld screenToWorld;
    private Map map;

    private LevelSettings levelSettings;
    private List<MapSettings> mapSettings;
    private int astronautCount;

    private int layer;
    private TileType tileType;

    boolean layerIndicatorEnabled = true;

    LevelBuilder(OrthographicCamera cam) {
        camera = cam;
        screenToWorld = new ScreenToWorld();
        levelSettings = new LevelSettings();
        mapSettings = new ArrayList<>();
        map = new Map();

        map.newLayer();
        mapSettings.add(new MapSettings());
        mapSettings.get(0).chained = true;
    }

    public void setTileType(TileType type) {
        tileType = type;
    }

    public void moveOffsetBy(float x, float y) {
        setOffset(getOffsetX() + x, getOffsetY() + y);
    }

    public void rotateLayer(float degrees) {
        map.forceRotateLayer(layer, degrees);
    }

    /**
     * Draws at the given screen coordinates
     */
    public void paintAt(float x, float y) {
        Vector3 relative = map.getWorldToLayerCoords(layer, screenToWorld.convert(x, y));

        if (map.isTileEmpty(layer, (int) relative.x, (int) relative.y)) {
            map.placeTile(layer, (int) relative.x, (int) relative.y, TileFactory.createTile(tileType));
            if (tileType.getMajorType().equals(TileType.MajorType.ASTRONAUT))
                ++astronautCount;
        }
    }

    public float getPositionX(int layer) {
        return map.getLayerPositionX(layer);
    }

    public int getTotalTileCount() {
        return map.totalBallCount();
    }

    public float getPositionY(int layer) {
        return map.getLayerPositionY(layer);
    }

    public void eraseAt(float x, float y) {
        Vector3 relative = map.getWorldToLayerCoords(layer, screenToWorld.convert(x, y));
        TilemapTile tile = map.getTilemapTile(layer, (int) relative.x, (int) relative.y);
        if (tile != null && tile.getTile().getTileType().getMajorType().equals(TileType.MajorType.ASTRONAUT))
            --astronautCount;
        map.removeTile(layer, (int) relative.x, (int) relative.y);
    }

    public void setLayerIndicator(boolean enabled) {
        layerIndicatorEnabled = enabled;
    }

    public void setChained(boolean chained) {
        mapSettings.get(layer).chained = chained;
    }

    public void setCCWRotation(boolean ccw) {
        mapSettings.get(layer).rotateCCW = ccw;
    }

    public void setOriginMinSpeed(int min) {
        mapSettings.get(layer).minMapSpeed = min;
    }

    public void setOriginMaxSpeed(int max) {
        mapSettings.get(layer).maxMapSpeed = max;
    }

    public int getOriginMinSpeed() {
        return mapSettings.get(layer).minMapSpeed;
    }

    public int getOriginMaxSpeed() {
        return mapSettings.get(layer).maxMapSpeed;
    }

    public int getMinSpeed() {
        return mapSettings.get(layer).minSpeed;
    }

    public int getMaxSpeed() {
        return mapSettings.get(layer).maxSpeed;
    }

    public void setCrossMinSpeed(int min) {
        mapSettings.get(layer).minSpeed = min;
    }

    public void setCrossMaxSpeed(int max) {
        mapSettings.get(layer).maxSpeed = max;
    }

    public void setColorCount(int amount) {
        mapSettings.get(layer).colorCount = amount;
    }

    public void setLives(int lives) {
        levelSettings.livesLimit = lives;
    }

    public void setMoves(int moves) {
        levelSettings.movesLimit = moves;
    }

    public void setTime(int time) {
        levelSettings.timeLimit = time;
    }

    public void setTargetScoreOne(int score) {
        if (score > 0)
            levelSettings.targetScores.one = score;
    }

    public void setTargetScoreTwo(int score) {
        if (score > 0)
            levelSettings.targetScores.two = score;
    }

    public void setTargetScoreThree(int score) {
        if (score > 0)
            levelSettings.targetScores.three = score;
    }

    int getTargetScoreOne() {
        return levelSettings.targetScores.one;
    }

    int getTargetScoreTwo() {
        return levelSettings.targetScores.two;
    }

    int getTargetScoreThree() {
        return levelSettings.targetScores.three;
    }

    public void setLauncherSize(int size) {
        levelSettings.launcherSize = size;
    }

    public void setLauncherCooldown(float cooldown) {
        levelSettings.launcherCooldown = cooldown;
    }

    public void setBallSpeed(int speed) {
        levelSettings.ballSpeed = speed;
    }

    public void setOrigin(float x, float y) {
        mapSettings.get(layer).origin.set(x, y);
        map.setOrigin(layer, x, y);
        map.validate(layer);
    }

    public void setOffset(float x, float y) {
        mapSettings.get(layer).offset.set(x, y);
        map.setOffset(layer, x, y);
        map.validate(layer);
    }

    public float getOriginX() {
        return map.getLayerOriginX(layer);
    }

    public float getOriginY() {
        return map.getLayerOriginY(layer);
    }

    public float getOffsetX() {
        return map.getLayerOffsetX(layer);
    }

    public float getOffsetY() {
        return map.getLayerOffsetY(layer);
    }

    public int getBallSpeed() {
        return levelSettings.ballSpeed;
    }

    public int getLives() {
        return levelSettings.livesLimit;
    }

    public int getMoves() {
        return levelSettings.movesLimit;
    }

    public int getTime() {
        return levelSettings.timeLimit;
    }

    public int getLauncherSize() {
        return levelSettings.launcherSize;
    }

    public float getLauncherCooldown() {
        return levelSettings.launcherCooldown;
    }

    public int getColorCount() {
        return mapSettings.get(layer).colorCount;
    }

    public boolean isChained() {
        return mapSettings.get(layer).isChained();
    }

    public boolean isCCWRotationEnabled() {
        return mapSettings.get(layer).rotateCCW;
    }

    public float getDefPositionX() {
        return map.getDefPositionX(); //should return per layer
    }

    public float getDefPositionY() {
        return map.getDefPositionY(); //should return per layer
    }

    public void draw(RenderManager renderManager) {
        renderManager.spriteBatchBegin(camera.combined);
        if (layerIndicatorEnabled) {
            int maxTilemaps = map.layerCount();
            renderManager.setColorTint(Color.GRAY);
            for (int i = 0; i < maxTilemaps; ++i) {
                if (i == layer) continue;
                map.draw(renderManager, i);
            }
            if (map.layerExists(layer)) {
                renderManager.setColorTint(Color.WHITE);
                map.draw(renderManager, layer);
            }
        } else {
            map.draw(renderManager);
        }
        renderManager.spriteBatchEnd();

        if (map.layerCount() > 0) {
            ShapeRenderer shapeRenderer = renderManager.shapeRendererStart(camera.combined, ShapeRenderer.ShapeType.Line);
            shapeRenderer.setColor(Color.GOLDENROD);
            shapeRenderer.circle(map.getLayerOriginX(layer) + getDefPositionX(), map.getLayerOriginY(layer) + getDefPositionY(), 20);

            shapeRenderer.line(getPositionX(layer) - 20, getPositionY(layer), getPositionX(layer) + 20, getPositionY(layer));
            shapeRenderer.line(getPositionX(layer), getPositionY(layer) - 20, getPositionX(layer), getPositionY(layer) + 20);
            shapeRenderer.circle(map.getLayerOriginX(layer) + getDefPositionX(), map.getLayerOriginY(layer) + getDefPositionY(), 20);
            renderManager.shapeRendererEnd();
        }
    }

    public boolean saveAs(String name) {
        levelSettings.targets.clear();
        if (levelSettings.targetScores.one != 0 ||
                levelSettings.targetScores.two != 0 ||
                levelSettings.targetScores.three != 0) {
            levelSettings.targets.add(GameTarget.SCORE);
        }

        if (astronautCount > 0)
            levelSettings.targets.add(GameTarget.ASTRONAUTS);
        // XXX(16/6/2018): Fix this array shit
        return LevelParser.saveAs(name, map, levelSettings, mapSettings.toArray(new MapSettings[mapSettings.size()]));
    }

    public boolean load(String name) {
        ParsedLevel parsedLevel = LevelParser.loadFrom(name, LevelListParser.Source.EXTERNAL);
        if (parsedLevel == null) return false;

        map.reset();
        astronautCount = 0;

        levelSettings.copy(parsedLevel.levelSettings);

        for (int i = 0; i < parsedLevel.getMapCount(); ++i) {
            if (i < mapSettings.size()) {
                mapSettings.get(i).copy(parsedLevel.mapSettings.get(i));
            } else {
                MapSettings ms = new MapSettings();
                ms.copy(parsedLevel.mapSettings.get(i));
                mapSettings.add(ms);
            }
        }

        for (int parsedLayer = 0; parsedLayer < parsedLevel.getMapCount(); ++parsedLayer) {
            map.newLayer();
            map.setOrigin(parsedLayer, mapSettings.get(parsedLayer).getOrigin());
            map.setOffset(parsedLayer, mapSettings.get(parsedLayer).getOffset());

            for (ParsedTile tile : parsedLevel.mapTiles.get(parsedLayer)) {
                map.placeTile(parsedLayer, tile.x, tile.y, TileFactory.getTileFromID(tile.tileID));
                if (TileType.getTileTypeFromID(tile.tileID).getMajorType().equals(TileType.MajorType.ASTRONAUT))
                    ++astronautCount;
            }
        }

        if (map.layerCount() == 0) {
            map.newLayer();
        }

        map.validate();
        return true;
    }

    public int upLayer() {
        ++layer;
        if (!map.layerExists(layer)) {
            map.newLayer();
            mapSettings.add(new MapSettings());
        }
        return layer;
    }

    public int downLayer() {
        if (layer != 0) {
            --layer;
        }
        return layer;
    }

    public int getLayer() {
        return layer;
    }

    public TileType getCurrentTileType() {
        return tileType;
    }

    private class ScreenToWorld {
        private Vector3 screenCoords = new Vector3();

        public Vector3 convert(float x, float y) {
            screenCoords.set(x, y, 0);
            return camera.unproject(screenCoords);
        }
    }
}

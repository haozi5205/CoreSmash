package com.archapp.coresmash.tiles;

import com.archapp.coresmash.GameController;
import com.archapp.coresmash.sound.SoundManager;
import com.archapp.coresmash.tilemap.TilemapTile;
import com.archapp.coresmash.tilemap.effect.DestroyRadiusEffect;

public class FireBall extends Tile implements Launchable {
//    private static final SoundManager.SoundEffect explosionSound = SoundManager.get().getSoundAsset("explosion1");
//    private static final SoundManager.SoundEffect launchSound = SoundManager.get().getSoundAsset("launch1");

    FireBall(TileType type) {
        super(type);
    }

    @Override
    public void onLaunch() {
        SoundManager.get().play(SoundManager.SoundTrack.FIREBALL_LAUNCH);
    }

    @Override
    public void onCollide(MovingBall movingBall, TilemapTile tileHit, GameController controller) {
        DestroyRadiusEffect.newInstance(2, tileHit.getLayerID(), tileHit.getX(), tileHit.getY())
                .apply(controller.getBehaviourPack().tilemapManager);
        SoundManager.get().play(SoundManager.SoundTrack.FIREBALL_EXPLOSION);
    }
}

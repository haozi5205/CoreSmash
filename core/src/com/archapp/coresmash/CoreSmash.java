package com.archapp.coresmash;

import com.archapp.coresmash.managers.RenderManager;
import com.archapp.coresmash.platform.AdManager;
import com.archapp.coresmash.platform.FeedbackMailHandler;
import com.archapp.coresmash.screens.LoadingScreen;
import com.archapp.coresmash.sound.SoundManager;
import com.archapp.coresmash.ui.UIUtils;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.io.PrintWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Stack;

public class CoreSmash extends Game {
    public static String APP_VERSION = "0.1.3.1";
    public static boolean DEV_MODE = false;
    public static boolean LOG_CRASHES = true;
    public static boolean DEBUG_TABLET = false;

    private Viewport viewport;
    private RenderManager renderManager;
    private SoundManager soundManager;
    private AssetManager assetManager;
    private UserAccount userAccount;
    private Skin skin;
    private PlatformSpecificManager platformSpecificManager;
    private AdManager adManager;
    private FeedbackMailHandler feedbackMailHandler;

    private Stack<Screen> screenStack;

    public CoreSmash() {
        this(new PlatformSpecificManager());
    }

    public CoreSmash(PlatformSpecificManager platformSpecificManager) {
        this.platformSpecificManager = platformSpecificManager;
        this.adManager = platformSpecificManager.adManager;
        this.feedbackMailHandler = platformSpecificManager.feedbackMailHandler;
    }

    @Override
    public void create() {
        Thread.currentThread().setUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable err) {
                if (LOG_CRASHES) {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.getDefault());
                    try (Writer writer = Gdx.files.external("/CoreSmash/crash-logs/" + "crash_" + format.format(Calendar.getInstance().getTime())).writer(false)) {
                        err.printStackTrace(new PrintWriter(writer));
                    } catch (Exception wtfJustHappened) {
                    }
                }
                err.printStackTrace();
                Gdx.app.exit();
            }
        });

        screenStack = new Stack<>();

        viewport = new ScreenViewport();

        Gdx.input.setCatchBackKey(true);

        soundManager = SoundManager.get();
        assetManager = new AssetManager();
        renderManager = new RenderManager(assetManager);

        skin = new Skin();
        userAccount = new UserAccount();

        super.setScreen(new LoadingScreen(this));
        Gdx.gl.glClearColor(16 / 255f, 16 / 255f, 24 / 255f, 1);
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        super.render();
        soundManager.update(Gdx.graphics.getDeltaTime());
    }

    public FeedbackMailHandler getFeedbackMailHandler() {
        return feedbackMailHandler;
    }

    public AdManager getAdManager() {
        return adManager;
    }

    public Skin getSkin() {
        return skin;
    }

    public UserAccount getUserAccount() {
        return userAccount;
    }

    public void setPrevScreen() {
        screenStack.pop();
        super.setScreen(screenStack.peek());
    }

    public void setScreen(Screen screen) {
        screenStack.push(screen);
        super.setScreen(screen);
    }

    public Viewport getUIViewport() {
        return viewport;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }

    public PlatformSpecificManager getPlatformSpecificManager() {
        return platformSpecificManager;
    }

    @Override
    public void dispose() {
        super.dispose();
        assetManager.dispose();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        UIUtils.updateScreenActor(width, height);
        super.resize(width, height);
    }
}

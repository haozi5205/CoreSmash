package com.coresmash.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Container;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Value;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.coresmash.levels.CampaignScreen;
import com.coresmash.sound.SoundManager;
import com.coresmash.ui.UIComponent;
import com.coresmash.ui.UIFactory;
import com.coresmash.ui.UIUtils;

/**
 * Created by Michail on 16/3/2018.
 */

public class MainMenuScreen extends ScreenBase {
    private CampaignScreen campaignScreen;
    private com.coresmash.levelbuilder.LevelBuilderScreen levelBuilderScreen;
    private Stage stage;
    private Skin skin;
    private Stack rootStack;
    private UIComponent uiMenuOverlay;
    private UIMainMenu uiMainMenu;

    /* TODO: Should be part of some options class */
    private SoundManager.SoundAsset backgroundMusic;

    public MainMenuScreen(com.coresmash.CoreSmash game) {
        super(game);
        stage = new Stage(game.getUIViewport());
        setupMainMenuStage(stage);

        screenInputMultiplexer.addProcessor(stage);
        screenInputMultiplexer.addProcessor(new InputAdapter() {
            @Override
            public boolean keyDown(int keycode) {
                if (keycode == Input.Keys.BACK || keycode == Input.Keys.ESCAPE) {
                    levelBuilderScreen.saveProgress();
                    Gdx.app.exit();
                    return true;
                }
                return false;
            }
        });

        campaignScreen = new CampaignScreen(gameInstance);
        levelBuilderScreen = new com.coresmash.levelbuilder.LevelBuilderScreen(gameInstance);
        backgroundMusic = SoundManager.get().getSoundAsset("backgroundMusic");
//        backgroundMusic.loop();
    }

    @Override
    public void render(float delta) {
        stage.act(delta);
        stage.draw();
    }

    private void setupMainMenuStage(Stage stage) {
        skin = gameInstance.getSkin();
        rootStack = new Stack();
        rootStack.setFillParent(true);
        stage.addActor(rootStack);

        uiMainMenu = new UIMainMenu();
        uiMenuOverlay = new UIOverlay();

        Image background = new Image(skin.getDrawable("MenuBackground"));
        background.setScaling(Scaling.fill);
        background.setAlign(Align.top);

        rootStack.add(background);
        rootStack.add(uiMainMenu.getRoot());
        rootStack.add(uiMenuOverlay.getRoot());
    }

    private void checkForLocalAccount() {
        Preferences prefs = Gdx.app.getPreferences("account");

        if (!prefs.getBoolean("valid", false)) {
            Gdx.input.getTextInput(new Input.TextInputListener() {
                @Override
                public void input(String text) {
                    if (text.length() < 3 || text.length() > 20) {
                        checkForLocalAccount();// XXX(26/4/2018): Recursive...
                        return;
                    }
                    Preferences prefs = Gdx.app.getPreferences("account");
                    prefs.putString("username", text);
                    prefs.putBoolean("valid", true);
                    prefs.flush();
                    gameInstance.getUserAccount().setUsername(text);
                }

                @Override
                public void canceled() {
                    checkForLocalAccount();
                }
            }, "Setup Username:", "", "Min 3 - Max 20 Characters");
        }
    }

    private class UIMainMenu implements UIComponent {
        Table root;

        public UIMainMenu() {
            root = new Table();

            ImageButton imgPlay = UIFactory.createImageButton(skin.getDrawable("PlayButton"), skin.newDrawable("PlayButton", Color.GRAY));
            imgPlay.getImageCell().grow();
            imgPlay.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    campaignScreen.updateInfo(); // XXX(26/5/2018): Remove this
                    gameInstance.setScreen(campaignScreen);
                }
            });
            imgPlay.setTransform(true);
            imgPlay.setOrigin(imgPlay.getWidth() / 2, imgPlay.getHeight() / 2);
            imgPlay.setRotation(-.75f);
            imgPlay.addAction(Actions.forever(
                    Actions.sequence(
                            Actions.rotateBy(1.5f, 1.5f),
                            Actions.rotateBy(-1.5f, 1.5f)
                    )));
            imgPlay.addAction(Actions.forever(
                    Actions.sequence(
                            Actions.scaleBy(.02f, .04f, 0.75f),
                            Actions.scaleBy(-.02f, -.04f, 0.75f)
                    )));


            root.defaults()
                    .width(Value.percentWidth(3 / 5f, rootStack))
                    .height(Value.percentHeight(2 / 14f, rootStack));

            Label versInfo = new Label("v." + gameInstance.VERSION + " | Michail Angelos Gakis", skin, "h6", Color.DARK_GRAY);
            versInfo.setAlignment(Align.bottom);

            root.bottom();
            root.add(imgPlay).padBottom(Value.percentHeight(1 / 14f, root)).row();
            root.add(versInfo).align(Align.center).height(versInfo.getPrefHeight());
        }

        private Container<TextButton> newMenuButton(String text, String name, EventListener el) {
            TextButton bt = UIFactory.createTextButton(text, skin.get("menuButton", TextButton.TextButtonStyle.class));
            bt.setName(name);
            bt.addListener(el);

            Container<TextButton> result = new Container<>(bt);
            result.setTransform(true);
            result.setOrigin(bt.getWidth() / 2, bt.getHeight() / 2);
            result.fill();
            result.setRotation(-.25f);
            result.addAction(Actions.forever(Actions.sequence(Actions.rotateBy(.5f, 1.5f, Interpolation.smoother), Actions.rotateBy(-.5f, 1.5f, Interpolation.smoother))));
            result.addAction(Actions.forever(Actions.sequence(Actions.scaleBy(.02f, .02f, 0.75f), Actions.scaleBy(-.02f, -.02f, 0.75f))));
            return result;
        }

        @Override
        public Group getRoot() {
            return root;
        }
    }

    private class UIOverlay implements UIComponent {
        Table root;

        public UIOverlay() {
            ImageButton.ImageButtonStyle imgbsMap = new ImageButton.ImageButtonStyle();
            imgbsMap.up = skin.getDrawable("boxSmall");
            imgbsMap.imageUp = skin.getDrawable("map");
            imgbsMap.imageDown = skin.newDrawable("map", Color.RED);

            ImageButton imgbMap = new ImageButton(imgbsMap);
            imgbMap.getImage().setScaling(Scaling.fill);
            imgbMap.getImageCell().padLeft(5).pad(3);
            imgbMap.addListener(UIUtils.getButtonSoundListener());
            imgbMap.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    gameInstance.setScreen(levelBuilderScreen);
                }
            });

            root = new Table();
            root.bottom().left();
            root.add(imgbMap)
                    .size(Value.percentWidth(1 / 7f, rootStack))
                    .maxSize(Value.percentHeight(1/8f, rootStack))
                    .padBottom(Value.percentHeight(1 / 16f, rootStack))
                    .padLeft(-10)
                    .left();
        }

        @Override
        public Group getRoot() {
            return root;
        }
    }

}
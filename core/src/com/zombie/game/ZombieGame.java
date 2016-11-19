package com.zombie.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.zombie.game.game.SceneInputProcessor;
import com.zombie.game.gui.GUITable;
import com.zombie.game.game.GameScene;

public class ZombieGame extends ApplicationAdapter {

    private GUITable statusBar;

    public GameScene gameScene;
    SceneInputProcessor inputProcessor;
    public OrthographicCamera uiCamera;
    public Stage uiStage;

    @Override
    public void create() {
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);

        uiStage = new Stage();
        uiCamera = new OrthographicCamera();
        uiStage.getViewport().setCamera(uiCamera);

        uiCamera.update();

        // Create status bar
        statusBar = new GUITable(uiStage);
        uiStage.addActor(statusBar);

        gameScene = new GameScene();
        inputProcessor = new SceneInputProcessor(gameScene);

        InputMultiplexer im = new InputMultiplexer(uiStage, inputProcessor, gameScene.getStage());
        Gdx.input.setInputProcessor(im);
        gameScene.create();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!statusBar.getPauseButton().isChecked()) {
            // Update AI time
            float deltaTime = Gdx.graphics.getDeltaTime();
            GdxAI.getTimepiece().update(deltaTime);
            // Update
            gameScene.update(deltaTime);
        }
        // Draw
        gameScene.draw();
        gameScene.getStage().getViewport().apply();
        uiStage.act();
        uiStage.draw();
    }

    @Override
    public void resize(int width, int height) {
        uiStage.getViewport().update(width, height, true);
        gameScene.getStage().getViewport().update(width, height, false);
    }

    @Override
    public void dispose() {
        gameScene.dispose();
        uiStage.dispose();
    }

}

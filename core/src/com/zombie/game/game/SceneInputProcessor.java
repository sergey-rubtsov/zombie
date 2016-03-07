package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.zombie.game.actors.SteeringActor;

public class SceneInputProcessor implements InputProcessor {

    private GameScene scene;

    public SceneInputProcessor(GameScene scene) {
        this.scene = scene;
    }

    @Override
    public boolean keyDown(int keycode) {
        Gdx.app.log("INFO","keycode " + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        Gdx.app.log("INFO","keycode " + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        Gdx.app.log("INFO","character " + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        Vector2 clickPoint = scene.getUnprojectPosition(screenX, screenY);
        Actor a = scene.area.getGroup().hit(clickPoint.x, clickPoint.y, true);
        if (a instanceof SteeringActor) {
            scene.getGreenMob().deleteActors();
            scene.getGreenMob().addActor((SteeringActor) a);
        } else scene.openFrame(scene.getUnprojectPosition(screenX, screenY), button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (scene.getFrame().isOpened())
        scene.closeFrame(scene.getUnprojectPosition(screenX, screenY));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (scene.getFrame().isOpened())
        scene.stretchFrame(scene.getUnprojectPosition(screenX, screenY));
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        scene.scrollCamera(amount);
        return false;
    }
}

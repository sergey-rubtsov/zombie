package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.zombie.game.gui.GUITable;

public class GameInputProcessor implements InputProcessor {

    private GameScene scene;

    public GameInputProcessor(GameScene scene) {
        this.scene = scene;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown:" + keycode);
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        System.out.println("keyUp:" + keycode);
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        System.out.println("keyTyped:" + character);
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        scene.openFrame(scene.getUnprojectPosition(screenX, screenY), button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        scene.closeFrame(scene.getUnprojectPosition(screenX, screenY));
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        scene.stretchFrame(scene.getUnprojectPosition(screenX, screenY));
        return true;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (amount < 0) {
            scene.scrollCamera(amount);
        } else {
            if (scrollIsPossible()) scene.scrollCamera(amount);
        }
        return false;
    }

    public void processMoveCamera(float deltaTime) {
        Vector2 leftBottom = scene.getUnprojectPosition(0, Gdx.graphics.getHeight());
        Vector2 rightUp = scene.getUnprojectPosition(Gdx.graphics.getWidth(), 0);
        if (Gdx.input.getX() < GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (((leftBottom.x < 0) && rightUp.x > scene.map.getMapWidth()) || leftBottom.x > 0)
                scene.moveLeftCamera(deltaTime);
        }
        if (Gdx.input.getY() < GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if ((rightUp.y > scene.map.getMapHeight() && (leftBottom.y < 0)) || rightUp.y < scene.map.getMapHeight())
                scene.moveUpCamera(deltaTime);
        }
        if (Gdx.input.getX() > Gdx.graphics.getWidth() - GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (((leftBottom.x < 0) && rightUp.x > scene.map.getMapWidth()) || rightUp.x < scene.map.getMapWidth())
                scene.moveRightCamera(deltaTime);
        }
        if (Gdx.input.getY() > Gdx.graphics.getHeight() - GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if ((rightUp.y > scene.map.getMapHeight() && (leftBottom.y < 0))  || leftBottom.y > 0)
                scene.moveDownCamera(deltaTime);
        }
    }

    public boolean scrollIsPossible() {
        Vector2 leftBottom = scene.getUnprojectPosition(0, Gdx.graphics.getHeight());
        Vector2 rightUp = scene.getUnprojectPosition(Gdx.graphics.getWidth(), 0);
        if (rightUp.x - leftBottom.x < scene.map.getMapWidth() || rightUp.y - leftBottom.y < scene.map.getMapHeight()) return true;
        return false;
    }
}

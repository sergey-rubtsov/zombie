package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

public class GameInputProcessor implements InputProcessor {

    private int mouseButton = -1;

    final Vector2 start = new Vector2();
    final Vector3 end = new Vector3();

    final Vector3 curr = new Vector3();
    final Vector3 last = new Vector3(-1, -1, -1);
    final Vector3 delta = new Vector3();

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
        this.mouseButton = button;
        scene.openFrame(scene.getUnprojectPosition(screenX, screenY), button);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.mouseButton = -1;
        scene.closeFrame(scene.getUnprojectPosition(screenX, screenY));
        last.set(-1, -1, -1);
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
            if (moveIsPossible()) scene.scrollCamera(amount);
        }
        return false;
    }

    public void processMoveCamera(float deltaTime) {
        Vector2 point;
        if (Gdx.input.getX() < 15 || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            point = scene.getUnprojectPosition(0, 0);
            if (point.x > 0) scene.moveLeftCamera(deltaTime);
        }
        if (Gdx.input.getY() < 15 || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            point = scene.getUnprojectPosition(0, 0);
            if (point.y < scene.map.getMapHeight()) scene.moveUpCamera(deltaTime);
        }
        if (Gdx.input.getX() > Gdx.graphics.getWidth() - 15 || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            point = scene.getUnprojectPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (point.x < scene.map.getMapWidth()) scene.moveRightCamera(deltaTime);
        }
        if (Gdx.input.getY() > Gdx.graphics.getHeight() - 15 || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            point = scene.getUnprojectPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            if (point.y > 0) scene.moveDownCamera(deltaTime);
        }
    }

    public boolean moveIsPossible() {
        Vector2 point = scene.getUnprojectPosition(0, 0);
        if (point.x < 0 || point.y < 0) return false;
        point = scene.getUnprojectPosition(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        if (point.x > scene.map.getMapWidth() || point.y > scene.map.getMapHeight()) return false;
        return true;
    }
}

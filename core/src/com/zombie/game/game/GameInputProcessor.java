package com.zombie.game.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.zombie.game.actors.Pointer;

public class GameInputProcessor implements InputProcessor {

    private int mouseButton = -1;

    final OrthographicCamera camera;
    final Vector2 scenePosition = new Vector2();

    final Vector3 curr = new Vector3();
    final Vector3 last = new Vector3(-1, -1, -1);
    final Vector3 delta = new Vector3();

    private GameScene scene;

    private Pointer pointer;

    public GameInputProcessor(GameScene scene, Pointer pointer) {
        this.scene = scene;
        this.camera = scene.getCamera();
        this.pointer = pointer;
    }

    @Override
    public boolean keyDown(int keycode) {
        System.out.println("keyDown:" + keycode);
        camera.update();
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
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        this.mouseButton = -1;
        last.set(-1, -1, -1);
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        setCurrentPosition(screenX, screenY);
        if (mouseButton > 0) {
            camera.unproject(curr.set(screenX, screenY, 0));
            if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
                camera.unproject(delta.set(last.x, last.y, 0));
                delta.sub(curr);
                camera.position.add(delta.x, delta.y, 0);
            }
            last.set(screenX, screenY, 0);
        } else if (mouseButton == 0) {

        }
        return true;
    }

    public void setCurrentPosition(int screenX, int screenY) {
        Vector3 c = camera.unproject(curr.set(screenX, screenY, 0));
        scenePosition.x = c.x;
        scenePosition.y = c.y;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        setCurrentPosition(screenX, screenY);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        if (camera.zoom + amount >= 1){
            camera.zoom = camera.zoom + amount;
        } else camera.zoom = 1;
        return false;
    }

    protected void setTargetPosition(int screenX, int screenY) {
        Vector2 pos = pointer.getPosition();
        scene.getStage().screenToStageCoordinates(pos.set(screenX, screenY));
        //pointer.getParent().stageToLocalCoordinates(pos);
        pointer.setPosition(pos.x, pos.y, Align.center);
    }
}

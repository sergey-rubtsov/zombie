package com.zombie.game.game;

import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Align;
import com.zombie.game.actors.Pointer;
import com.zombie.game.steering.SteeringActor;

public class GameInputProcessor implements InputProcessor {

    private int touchPressed = -1;

    final OrthographicCamera camera;
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
    public boolean touchDown(int x, int y, int pointer, int button) {
        System.out.println("touchDown x:" + x + " y:" + y + " pointer:" + pointer + " button:" + button);
        //camera.position.x = x;
        //camera.position.y = y;
/*        camera.unproject(curr.set(x, y, 0));
        if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
            camera.unproject(delta.set(last.x, last.y, 0));
            delta.sub(curr);
            camera.position.add(delta.x, delta.y, 0);
        }
        last.set(x, y, 0);*/
        setTargetPosition(x, y);
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("touchUp x:"+ screenX + " y:" + screenY + " pointer:" + pointer + " button:" + button);
        last.set(-1, -1, -1);
        return false;
    }

    @Override
    public boolean touchDragged(int x, int y, int pointer) {
        camera.unproject(curr.set(x, y, 0));
        if (!(last.x == -1 && last.y == -1 && last.z == -1)) {
            camera.unproject(delta.set(last.x, last.y, 0));
            delta.sub(curr);
            camera.position.add(delta.x, delta.y, 0);
        }
        last.set(x, y, 0);
        Vector3 res = camera.unproject(new Vector3(x, y, 0));
        System.out.println("camera ux:" + res.x + " camera x:" + camera.position.x + " camera uy:" + res.y
                + " camera y:" + camera.position.y + " camera uz:" + res.z  + " camera z:" + camera.position.z);
        System.out.println(  );
        return true;
    }

    public void setCurrentPosition(int screenX, int screenY) {

    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Vector2 pos = new Vector2(screenX, screenY);
        pos = scene.getStage().screenToStageCoordinates(pos);
        System.out.println("mouseMoved screenX:" + screenX + " screenY:" + screenY + " x:" + pos.x + " y:" + pos.y);
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.zoom = camera.zoom + amount;
        if (camera.zoom + amount >= 1){
            if (amount > 0) {

            } else {

            }
            camera.zoom = camera.zoom + amount;
        } else camera.zoom = 1;

        camera.update();
        System.out.println("scrolled amount:" + amount + " camera.zoom:" + camera.zoom
                + " camera.position x:" + camera.position.x + " camera.position y:" + camera.position.y);
        return false;
    }

    protected void setTargetPosition(int screenX, int screenY) {
        Vector2 pos = pointer.getPosition();
        scene.getStage().screenToStageCoordinates(pos.set(screenX, screenY));
        //pointer.getParent().stageToLocalCoordinates(pos);
        pointer.setPosition(pos.x, pos.y, Align.center);
    }
}

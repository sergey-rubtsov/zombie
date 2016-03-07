package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.zombie.game.gui.GUITable;

/**
 * Created by Serg on 29.02.2016.
 */
public class GameCamera extends OrthographicCamera {

    GameScene scene;

    public GameCamera(GameScene scene) {
        super();
        this.scene = scene;
        near = -1000f;
        far = 1000f;
        update();
    }

    public void moveUpCamera(float deltaTime) {
        position.y = position.y + zoom * 500 * deltaTime;
    }

    public void moveDownCamera(float deltaTime) {
        position.y = position.y - zoom * 500 * deltaTime;
    }

    public void moveLeftCamera(float deltaTime) {
        position.x = position.x - zoom * 500 * deltaTime;
    }

    public void moveRightCamera(float deltaTime) {
        position.x = position.x + zoom * 500 * deltaTime;
    }

    public void processMoveCamera(float deltaTime) {
        Vector2 leftBottom = scene.getUnprojectPosition(0, Gdx.graphics.getHeight());
        Vector2 rightUp = scene.getUnprojectPosition(Gdx.graphics.getWidth(), 0);
        if (Gdx.input.getX() < GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            if (((leftBottom.x < 0) && rightUp.x > scene.map.getMapWidth()) || leftBottom.x > 0)
                moveLeftCamera(deltaTime);
        }
        if (Gdx.input.getY() < GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.UP)) {
            if ((rightUp.y > scene.map.getMapHeight() && (leftBottom.y < 0)) || rightUp.y < scene.map.getMapHeight())
                moveUpCamera(deltaTime);
        }
        if (Gdx.input.getX() > Gdx.graphics.getWidth() - GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            if (((leftBottom.x < 0) && rightUp.x > scene.map.getMapWidth()) || rightUp.x < scene.map.getMapWidth())
                moveRightCamera(deltaTime);
        }
        if (Gdx.input.getY() > Gdx.graphics.getHeight() - GUITable.padding || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            if ((rightUp.y > scene.map.getMapHeight() && (leftBottom.y < 0))  || leftBottom.y > 0)
                moveDownCamera(deltaTime);
        }
    }

    public boolean scrollIsPossible() {
        Vector2 leftBottom = scene.getUnprojectPosition(0, Gdx.graphics.getHeight());
        Vector2 rightUp = scene.getUnprojectPosition(Gdx.graphics.getWidth(), 0);
        if (rightUp.x - leftBottom.x < scene.map.getMapWidth() || rightUp.y - leftBottom.y < scene.map.getMapHeight()) return true;
        return false;
    }

    public void scrollCamera(int amount) {
        //Gdx.app.log("INFO","this.position.z " + this.position.z);
        if (amount < 0) {
            changeZoom(amount);
        } else {
            if (scrollIsPossible()) changeZoom(amount);
        }
    }

    private void changeZoom(int amount) {
        if (zoom + amount >= 1){
            zoom = zoom + amount;
            update();
            //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }
    }

}

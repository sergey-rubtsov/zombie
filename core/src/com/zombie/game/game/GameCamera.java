package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.PerspectiveCamera;

/**
 * Created by Serg on 29.02.2016.
 */
public class GameCamera extends OrthographicCamera {

    //it will render 3D models
    PerspectiveCamera camera;

    public GameCamera() {
        super();
        zoom = 5;
        //Initialize the camera
        camera = new PerspectiveCamera(67, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        camera.position.set(10f, 10f, 10f);
        camera.lookAt(0,0,0);
        camera.near = 1f;
        camera.far = 300f;
        camera.update();
    }
}

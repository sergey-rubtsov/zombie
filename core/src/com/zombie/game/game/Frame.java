package com.zombie.game.game;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Serg on 12.02.2016.
 */
public class Frame {

    private boolean opened;

    private Color color;

    private final Vector2 pointA;

    private final Vector2 pointC;

    public Frame() {
        opened = false;
        pointA = new Vector2();
        pointC = new Vector2();
    }

    public boolean isOpened() {
        return opened;
    }

    public void open() {
        opened = true;
        toPoint();
    }

    public void close() {
        opened = false;
        toPoint();
    }

    public void toPoint() {
        this.pointC.x = pointA.x;
        this.pointC.y = pointA.y;
    }

    public Color getColor() {
        return color;
    }

    public Vector2 getPointA() {
        return pointA;
    }

    public Vector2 getPointC() {
        return pointC;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setColor(int button) {
        if (button == 0) {
            setColor(Color.GREEN);
        } else setColor(Color.RED);
    }

    public void setPointA(Vector2 pointA) {
        this.pointA.x = pointA.x;
        this.pointA.y = pointA.y;
    }

    public void setPointC(Vector2 pointC) {
        this.pointC.x = pointC.x;
        this.pointC.y = pointC.y;
    }
}

package com.zombie.game.actors;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.utils.Array;

/**
 * Created by Serg on 16.02.2016.
 */
public class Mob {

    private Color color;

    private final Array<SteeringActor> actors;

    public Mob() {
        this(Color.CLEAR);
    }

    public Mob(Color color) {
        this.color = color;
        this.actors = new Array<SteeringActor>();
    }

    public void addActor(SteeringActor actor) {
        actor.setMob(this);
        actors.add(actor);
    }

    public void deleteActors() {
        for (SteeringActor actor: actors) {
            actor.deselect();
        }
        actors.clear();
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Array<SteeringActor> getActors() {
        return actors;
    }
}

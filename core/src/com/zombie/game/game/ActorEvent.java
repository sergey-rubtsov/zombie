package com.zombie.game.game;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.zombie.game.actors.SteeringActor;

/**
 * Created by Serg on 15.02.2016.
 */
public class ActorEvent extends InputEvent {

    private SteeringActor steeringActor;

    @Override
    public void reset() {
        steeringActor = null;
        super.reset();
    }

    public void setSteeringActor(SteeringActor steeringActor) {
        this.steeringActor = steeringActor;
    }

    public SteeringActor getSteeringActor() {
        return steeringActor;
    }
}

package com.zombie.game.actors;

import com.badlogic.gdx.ai.steer.behaviors.Arrive;
import com.badlogic.gdx.ai.utils.Location;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.zombie.game.game.Assets;
import com.zombie.game.steering.SteeringActor;

import java.util.Random;

/**
 * Created by Serg on 30.01.2016.
 */
public class Survivor extends SteeringActor {

    float maxLinearSpeed = 200;

    float maxLinearAcceleration = 200;

    public Survivor() {
        super(Assets.characters[new Random().nextInt(Assets.characters.length)][new Random().nextInt(Assets.characters[0].length)]);
        setMaxLinearSpeed(maxLinearSpeed);
        setMaxLinearAcceleration(maxLinearAcceleration);
    }

    public void setArriveSB(Location<Vector2> target) {
        final Arrive<Vector2> arriveSB = new Arrive<Vector2>(this, target)
                .setTimeToTarget(0.1f) //
                .setArrivalTolerance(0.001f) //
                .setDecelerationRadius(80);
        setSteeringBehavior(arriveSB);
    }

}

package com.zombie.game.actors;

import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.proximities.FieldOfViewProximity;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.zombie.game.game.Assets;
import com.zombie.game.game.InteractionArea;

import java.util.Random;

/**
 * Created by Serg on 10.02.2016.
 */
public class ActorFactory {

    static {
        initAssets();
    }

    public static AssetManager assetManager;

    public static void initAssets() {
        assetManager = new AssetManager();
        assetManager.load("robot1/robot1.g3db", Model.class);
        assetManager.load("human.g3db", Model.class);
        assetManager.load("walking_3.g3db", Model.class);
    }

    public static Zombie getRandomZombie(Array<FieldOfViewProximity<Vector2>> proximities,
                                         Array<SteeringActor> characters, Array<BlendedSteering<Vector2>> blendedSteerings,
                                        Group group) {
        TextureRegion tr = Assets.zombies[new Random().nextInt(Assets.zombies.length)][new Random().nextInt(Assets.zombies[0].length)];
        Zombie zombie = new Zombie(tr);

        zombie.setPosition(MathUtils.random(group.getWidth()), MathUtils.random(group.getHeight()), Align.center);
        zombie.setMaxLinearSpeed(40);
        zombie.setMaxLinearAcceleration(400); //
        zombie.setMaxAngularAcceleration(0);
        zombie.setMaxAngularSpeed(5);

        while (true) {
            if (assetManager.update()) {
                ModelInstance modelInstance = new ModelInstance(assetManager.get("walking_3.g3db", Model.class));
                zombie.setModelInstance(modelInstance);
                break;
            }
        }

        FieldOfViewProximity<Vector2> proximity = new FieldOfViewProximity<Vector2>(zombie, characters, 140,
                270 * MathUtils.degreesToRadians);
        proximities.add(proximity);
        //if (i == 0) char0Proximity = proximity;
        Alignment<Vector2> groupAlignmentSB = new Alignment<Vector2>(zombie, proximity);
        Cohesion<Vector2> groupCohesionSB = new Cohesion<Vector2>(zombie, proximity);
        final float separationDecayCoefficient = InteractionArea.separationDecayCoefficient;
        Separation<Vector2> groupSeparationSB = new Separation<Vector2>(zombie, proximity) {
            @Override
            public float getDecayCoefficient () {
                // We want all the agents to use the same decay coefficient
                return separationDecayCoefficient;
            }

            @Override
            public Separation<Vector2> setDecayCoefficient (float decayCoefficient) {
                InteractionArea.separationDecayCoefficient = decayCoefficient;
                return this;
            }
        };

        BlendedSteering<Vector2> blendedSteering = new BlendedSteering<Vector2>(zombie) //
                .add(groupAlignmentSB, .2f) //
                .add(groupCohesionSB, .06f) //
                .add(groupSeparationSB, 1.7f);
        blendedSteerings.add(blendedSteering);

        // TODO set more proper values
        Wander<Vector2> wanderSB = new Wander<Vector2>(zombie) //
                // Don't use Face internally because independent facing is off
                .setFaceEnabled(false) //
                // No need to call setAlignTolerance, setDecelerationRadius and setTimeToTarget because we don't use internal Face
                .setWanderOffset(60) //
                .setWanderOrientation(10) //
                .setWanderRadius(40) //
                .setWanderRate(MathUtils.PI2 * 4);

        PrioritySteering<Vector2> prioritySteeringSB = new PrioritySteering<Vector2>(zombie, 0.0001f) //
                .add(blendedSteering) //
                .add(wanderSB);

        zombie.setSteeringBehavior(prioritySteeringSB);

        group.addActor(zombie);

        characters.add(zombie);
        return zombie;
    }

    public static Zombie getRandomZombie(InteractionArea area) {
        return getRandomZombie(area.getProximities(), area.getCharacters(), area.getBlendedSteerings(), area.getGroup());
    }

    public static SteeringActor getRandomSurvivor(InteractionArea area) {
        Survivor s = new Survivor();
        //s.setArriveSB(target);

        area.getGroup().addActor(s);
        //area.getGroup().addActor(pointer);

        //s.setPosition(getStage().getWidth() / 2, getStage().getHeight() / 2, Align.center);
        //pointer.setPosition(MathUtils.random(getStage().getWidth()), MathUtils.random(getStage().getHeight()), Align.center);

        return s;
    }

}

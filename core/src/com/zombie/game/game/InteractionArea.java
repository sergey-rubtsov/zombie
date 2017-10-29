package com.zombie.game.game;

import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.proximities.FieldOfViewProximity;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.zombie.game.actors.SteeringActor;

public class InteractionArea {

    Array<BlendedSteering<Vector2>> blendedSteerings;
    FieldOfViewProximity<Vector2> char0Proximity;
    Array<FieldOfViewProximity<Vector2>> proximities;

    static public float separationDecayCoefficient = 500;

    private Array<SteeringActor> characters;

    GameScene scene;

    private Group group;

    public InteractionArea(GameScene scene, Group group) {
        this.characters = scene.characters;
        this.scene = scene;
        this.group = group;
        this.blendedSteerings = new Array<BlendedSteering<Vector2>>();
        this.proximities = new Array<FieldOfViewProximity<Vector2>>();
    }

    public Array<BlendedSteering<Vector2>> getBlendedSteerings() {
        return blendedSteerings;
    }

    public FieldOfViewProximity<Vector2> getChar0Proximity() {
        return char0Proximity;
    }

    public Array<FieldOfViewProximity<Vector2>> getProximities() {
        return proximities;
    }

    public float getSeparationDecayCoefficient() {
        return separationDecayCoefficient;
    }

    public Array<SteeringActor> getCharacters() {
        return characters;
    }

    public Group getGroup() {
        return group;
    }

    public GameScene getScene() {
        return scene;
    }
}

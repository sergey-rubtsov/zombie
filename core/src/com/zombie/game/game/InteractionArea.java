package com.zombie.game.game;

import com.badlogic.gdx.ai.steer.behaviors.BlendedSteering;
import com.badlogic.gdx.ai.steer.proximities.FieldOfViewProximity;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.utils.Array;
import com.zombie.game.steering.SteeringActor;

public class InteractionArea {

    Array<BlendedSteering<Vector2>> blendedSteerings;
    FieldOfViewProximity<Vector2> char0Proximity;
    Array<FieldOfViewProximity<Vector2>> proximities;

    static public float separationDecayCoefficient = 500;

    private Array<SteeringActor> characters;

    private Group group;

    public InteractionArea(Array<SteeringActor> characters, Group group) {
        this.characters = characters;
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

    public Array<SteeringActor> selectCharacters(Vector2 a, Vector2 c, Array<SteeringActor> selected, Color color) {
        selected.clear();
        for (SteeringActor actor : characters) {
            float x = actor.getPosition().x;
            float y = actor.getPosition().y;
            if ((a.x < x && x < c.x) || (c.x < x && x < a.x)) {
                if ((a.y < y && y < c.y) || (c.y < y && y < a.y)) {
                    actor.setSelectionColor(color);
                    selected.add(actor);
                }
            }
        }
        return selected;
    }

    public Group getGroup() {
        return group;
    }
}

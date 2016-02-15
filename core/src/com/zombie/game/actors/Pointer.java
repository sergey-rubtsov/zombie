package com.zombie.game.actors;

import com.zombie.game.game.Assets;

public class Pointer extends SteeringActor {

    public Pointer() {
        super(Assets.target);
        setIndependentFacing(true);
    }
}

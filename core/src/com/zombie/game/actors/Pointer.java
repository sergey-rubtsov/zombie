package com.zombie.game.actors;

import com.zombie.game.game.Assets;
import com.zombie.game.game.InteractionArea;
import com.zombie.game.steering.SteeringActor;

public class Pointer extends SteeringActor {

    public Pointer() {
        super(Assets.target);
        setIndependentFacing(true);
    }
}

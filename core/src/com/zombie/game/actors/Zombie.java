package com.zombie.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zombie.game.steering.SteeringActor;

public class Zombie extends SteeringActor {

    public Zombie(TextureRegion region) {
        super(region, false);
    }


}

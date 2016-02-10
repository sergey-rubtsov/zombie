package com.zombie.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.zombie.game.steering.SteeringActor;

/**
 * Created by Serg on 30.01.2016.
 */
public class Wall extends SteeringActor {
    public Wall(TextureRegion region) {
        super(region);
    }
}

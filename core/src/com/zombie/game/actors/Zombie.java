package com.zombie.game.actors;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Zombie extends SteeringActor {

    public Zombie(TextureRegion region) {
        super(region, false);
        this.setBounds(0, 0, region.getRegionWidth(), region.getRegionHeight());
    }

}

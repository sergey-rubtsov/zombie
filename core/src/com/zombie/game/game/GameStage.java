package com.zombie.game.game;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.zombie.game.actors.SteeringActor;

/**
 * Created by Serg on 14.02.2016.
 */
public class GameStage extends Stage {

    private SteeringActor selectedActor;

    public GameStage(Viewport viewport) {
        super(viewport);
    }

    public SteeringActor getSelectedActor() {
        return selectedActor;
    }

    public void setSelectedActor(SteeringActor selectedActor) {
        this.selectedActor = selectedActor;
    }
}

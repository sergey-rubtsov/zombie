package com.zombie.game.gui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.zombie.game.game.Assets;

/**
 * Created by Serg on 19.02.2016.
 */
public class GameLabelStyle extends Label.LabelStyle {
    public GameLabelStyle() {
        super();
        super.font = Assets.font;
        super.fontColor = Color.BLUE;
    }
}

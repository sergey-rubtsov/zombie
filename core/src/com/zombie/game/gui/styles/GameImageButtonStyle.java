package com.zombie.game.gui.styles;

import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.zombie.game.game.Assets;

/**
 * Created by Serg on 19.02.2016.
 */
public class GameImageButtonStyle extends ImageButton.ImageButtonStyle {
    public GameImageButtonStyle() {
        super();
        super.imageUp = Assets.buttonAttack[2];
        super.imageDown = Assets.buttonAttack[2];
        super.imageOver = Assets.buttonAttack[0];
        super.imageChecked = Assets.buttonAttack[1];
    }
}

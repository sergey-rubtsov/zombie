package com.zombie.game.gui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.zombie.game.game.Assets;

/**
 * Created by Serg on 19.02.2016.
 */
public class GameImageTextButtonStyle extends ImageTextButton.ImageTextButtonStyle {
    public GameImageTextButtonStyle() {
        super();
        super.imageUp = Assets.buttonAttack[2];
        super.imageDown = Assets.buttonAttack[2];
        super.imageOver = Assets.buttonAttack[0];
        super.imageChecked = Assets.buttonAttack[1];
        super.font = Assets.font;
        super.fontColor = Color.DARK_GRAY;
        super.downFontColor = Color.RED;
        super.overFontColor = Color.BROWN;
        super.checkedFontColor = Color.BLACK;
        super.checkedOverFontColor = Color.BLACK;
        super.disabledFontColor = Color.CLEAR;
    }
}

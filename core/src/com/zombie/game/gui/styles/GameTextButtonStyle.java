package com.zombie.game.gui.styles;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.zombie.game.game.Assets;

/**
 * Created by Serg on 19.02.2016.
 */
public class GameTextButtonStyle extends TextButton.TextButtonStyle {
    public GameTextButtonStyle() {
        super();
        super.font = Assets.font;
        super.fontColor = Color.DARK_GRAY;
        super.downFontColor = Color.RED;
        super.overFontColor = Color.BROWN;
        super.checkedFontColor = Color.BLACK;
        super.checkedOverFontColor = Color.BLACK;
        super.disabledFontColor = Color.CLEAR;
    }
}

package com.zombie.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.ImageTextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.zombie.game.gui.styles.GameImageTextButtonStyle;

/**
 * Created by Serg on 16.02.2016.
 */
public class WarButton extends ImageTextButton {
    public WarButton(String text, Skin skin) {
        super(text, skin);
    }

    public WarButton(String text, Skin skin, String styleName) {
        super(text, skin, styleName);
    }

    public WarButton() {
        super("war", new GameImageTextButtonStyle());
    }
}

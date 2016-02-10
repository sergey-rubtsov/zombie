package com.zombie.game.gui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.StringBuilder;
import com.zombie.game.game.GameInputProcessor;

/**
 * Created by Serg on 07.02.2016.
 */
public class DebugValueLabel extends Label {

    public DebugValueLabel(CharSequence text, Skin skin) {
        super(text, skin);
    }

    @Override
    public StringBuilder getText() {
        StringBuilder sb = new StringBuilder();
        sb.append("x:").
                append(GameInputProcessor.curX).
                append(" y:").
                append(GameInputProcessor.curY).
                append(" zoom:").
                append(GameInputProcessor.curZoom);
        return sb;
    }
}

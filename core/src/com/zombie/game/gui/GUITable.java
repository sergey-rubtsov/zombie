package com.zombie.game.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;

/**
 * Created by Serg on 08.02.2016.
 */
public class GUITable extends Table {

    TextButton pauseButton;
    Label testHelpLabel;
    public static float padding = 15;

    public GUITable() {
        super(new Skin(Gdx.files.internal("uiskin.json")));
        debug(Debug.all);
        debugAll();
        pad(padding);
        left().bottom();
        row().height(26);
        //statusBar.add(pauseButton = new PauseButton(translucentPanel, skin)).width(90).left();
        add(pauseButton = new PauseButton(getSkin())).width(90).left();
        add(new FpsLabel("FPS: ", getSkin())).padLeft(15);
        add(testHelpLabel = new Label("", getSkin())).padLeft(15);
        // Add translucent panel (it's only visible when AI is paused)
        /*final Image translucentPanel = new Image(skin, "translucent");
        translucentPanel.setSize(stageWidth, stageHeight);
        translucentPanel.setVisible(false);
        stage.addActor(translucentPanel);*/
    }

    public TextButton getPauseButton() {
        return pauseButton;
    }

    /*    public void dispose() {
        getSkin().dispose();
    }*/
}

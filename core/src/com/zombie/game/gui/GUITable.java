package com.zombie.game.gui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.zombie.game.gui.styles.GameLabelStyle;
import com.zombie.game.gui.styles.GameTextButtonStyle;

/**
 * Created by Serg on 08.02.2016.
 */
public class GUITable extends Table {

    TextButton pauseButton;
    Label testHelpLabel;
    WarButton attackButton;
    public static float padding = 10;

    public GUITable(Stage uiStage) {
        super();
        super.setStage(uiStage);
        debug(Debug.all);
        debugAll();
        pad(padding * 2);
        setWidth(uiStage.getWidth());
        setHeight(uiStage.getHeight());
        Table upContainer = new Table();
        Table middleContainer = new Table();
        Table bottomContainer = new Table();
        row().height(25);
        add(upContainer).right();
        row();
        add(middleContainer).expand();
        row().height(25);
        add(bottomContainer).left();

        upContainer.add(new FpsLabel("FPS: ", new GameLabelStyle())).padRight(20);
        // Add translucent panel (it's only visible when AI is paused)
        Pixmap pixmap = new Pixmap(5, 5, Pixmap.Format.RGBA8888);
        pixmap.setColor(new Color(0.1f, 0.1f, 0.1f, 0.5f));
        pixmap.fill();
        final Image translucentPanel = new Image(new Texture(pixmap));
        translucentPanel.setSize(uiStage.getWidth(), uiStage.getHeight());
        translucentPanel.setVisible(false);
        upContainer.add(pauseButton = new PauseButton(translucentPanel, new GameTextButtonStyle())).width(90).left();
        upContainer.add(testHelpLabel = new Label("test", new GameLabelStyle())).padLeft(15);
        bottomContainer.add(attackButton = new WarButton()).padLeft(5);
        bottomContainer.add(testHelpLabel = new Label("test", new GameLabelStyle())).padLeft(15);
        bottomContainer.add(testHelpLabel = new Label("test", new GameLabelStyle())).padLeft(15);

        uiStage.addActor(translucentPanel);
    }

    public TextButton getPauseButton() {
        return pauseButton;
    }

    /*    public void dispose() {
        getSkin().dispose();
    }*/
}

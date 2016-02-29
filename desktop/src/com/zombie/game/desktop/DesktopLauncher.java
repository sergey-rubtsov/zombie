package com.zombie.game.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.zombie.game.LibgdxModelViewer;
import com.zombie.game.ZombieGame;

public class DesktopLauncher {
    public static void main(String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        //new LwjglApplication(new LibgdxModelViewer(), config);
        new LwjglApplication(new ZombieGame(), config);
    }
}

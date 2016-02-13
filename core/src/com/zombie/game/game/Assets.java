package com.zombie.game.game;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Assets {

    static {
        load();
    }

    public static Texture charactersTexture;
    public static Texture zombiesTexture;
    public static Texture hexture;
    public static Texture grass;
    public static TextureRegion[][] characters;
    public static TextureRegion[][] zombies;
    public static TextureRegion[][] hexes;

    public static TextureRegion target;

    public static void load() {
        charactersTexture = new Texture("data/npc/characters.png");
        zombiesTexture = new Texture("data/npc/zombies.png");
        hexture = new Texture("data/hexes.png");
        grass = new Texture("data/grass.png");
        characters = new TextureRegion[4][12];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 12; j++) {
                characters[i][j] = new TextureRegion(charactersTexture, 15 + i * 151, 15 + j * 145, 80, 100);
            }
        }
        zombies = new TextureRegion[3][4];
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 4; j++) {
                zombies[i][j] = new TextureRegion(zombiesTexture, i * 101 + 3, j * 95, 75, 90);
            }
        }
        target = new TextureRegion(new Texture("data/target.png"));
        //hexes = TextureRegion.split(hexture, 112, 97);
        hexes = TextureRegion.split(grass, 112, 97);
    }

}

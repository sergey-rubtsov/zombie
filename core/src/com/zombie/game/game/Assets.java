package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

public class Assets {

    static {
        load();
    }

    public static TextureRegionDrawable[] buttonAttack;

    public static Texture charactersTexture;
    public static Texture zombiesTexture;
    public static Texture hexture;
    public static Texture grass;
    public static TextureRegion[][] characters;
    public static TextureRegion[][] zombies;
    public static TextureRegion[][] hexes;

    public static TextureRegion target;

    public static BitmapFont font;

    public static void load() {
        font = new BitmapFont(Gdx.files.internal("black_molot.fnt"), Gdx.files.internal("black_molot_0.png"), false);
        font.getData().setScale(0.8f);
        charactersTexture = new Texture(Gdx.files.internal("characters.png"));
        zombiesTexture = new Texture(Gdx.files.internal("zombies.png"));
        hexture = new Texture(Gdx.files.internal("hexes.png"));
        grass = new Texture(Gdx.files.internal("grass.png"));
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
        target = new TextureRegion(new Texture(Gdx.files.internal("target.png")));
        hexes = TextureRegion.split(grass, 112, 97);
        TextureRegion daUp = new TextureRegion(new Texture(Gdx.files.internal("daUp50.png")));
        TextureRegion daChecked = new TextureRegion( new Texture(Gdx.files.internal("daChecked50.png")));
        TextureRegion daDown = new TextureRegion( new Texture(Gdx.files.internal("daDown50.png")));
        buttonAttack = new TextureRegionDrawable[]{
            new TextureRegionDrawable(daUp),
            new TextureRegionDrawable(daChecked),
            new TextureRegionDrawable(daDown)
        };
    }

}

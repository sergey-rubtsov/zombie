package com.zombie.game.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapLayers;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTile;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.tiles.StaticTiledMapTile;

public class TiledBackground extends TiledMap {

    final public static int tileWidth = 112;

    final public static int tileHeight = 97;

    int width;

    int height;

    int mapWidth;

    int mapHeight;

    public TiledBackground(float size) {
        this(Math.round(tileWidth * size), Math.round(tileHeight * size));
    }

    public TiledBackground(int width, int height) {
        super();
        this.width = width;
        this.height = height;

        for (int i = 0; i < width; i++) {
            if (i % 2 == 0) {
                mapWidth = mapWidth + tileWidth;
            } else {
                mapWidth = mapWidth + tileWidth / 2;
            }
        }
        mapHeight = height * tileHeight;
        mapHeight = mapHeight + tileHeight / 2;
        if (width % 2 == 0) mapWidth = mapWidth + tileWidth / 4;
        MapLayers layers = getLayers();
        TiledMapTile[] tiles = new TiledMapTile[3];
        tiles[0] = new StaticTiledMapTile(new TextureRegion(Assets.hexes[0][0]));
        tiles[1] = new StaticTiledMapTile(new TextureRegion(Assets.hexes[0][1]));
        tiles[2] = new StaticTiledMapTile(new TextureRegion(Assets.hexes[1][0]));

        for (int l = 0; l < 1; l++) {
            TiledMapTileLayer layer = new TiledMapTileLayer(width, height, tileWidth, tileHeight);
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int id = (int)(Math.random() * 3);
                    TiledMapTileLayer.Cell cell = new TiledMapTileLayer.Cell();
                    cell.setTile(tiles[id]);
                    layer.setCell(x, y, cell);
                }
            }
            layers.add(layer);
        }
    }

    public int getTileWidth() {
        return tileWidth;
    }

    public int getTileHeight() {
        return tileHeight;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getMapWidth() {
        return mapWidth;
    }

    public int getMapHeight() {
        return mapHeight;
    }
}



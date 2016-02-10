package com.zombie.game.game;

import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.ai.steer.behaviors.*;
import com.badlogic.gdx.ai.steer.proximities.FieldOfViewProximity;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.*;
import com.zombie.game.ZombieGame;
import com.zombie.game.actors.ActorFactory;
import com.zombie.game.actors.Pointer;
import com.zombie.game.steering.SteeringActor;

public class GameScene {

    private static final boolean DEBUG_STAGE = true;

    protected ZombieGame game;

    private World world;

    private Body[] walls;

    public Stage stage;

    protected Group group;

    private Pointer pointer;

    Array<SteeringActor> characters;

    boolean drawDebug;

    GameInputProcessor inputProcessor;

    TiledBackground map;
    HexagonalTiledMapRenderer hexRenderer;
    ShapeRenderer shapeRenderer;

    private float lastUpdateTime;
    OrthographicCamera camera;
    InteractionArea area;

    public GameScene(ZombieGame game) {
        this.game = game;
        camera = new OrthographicCamera();
        this.map = new TiledBackground(1);
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        stage.setDebugAll(DEBUG_STAGE);
        this.pointer = new Pointer();
        this.inputProcessor = new GameInputProcessor(this, pointer);
    }

    public void create() {
        buildGroup(map.getMapWidth(), map.getMapHeight());
        buildCharacters();
        hexRenderer = new HexagonalTiledMapRenderer(map);
        shapeRenderer = new ShapeRenderer();
        drawDebug = true;
        camera.zoom = 5;
        camera.position.x = getMap().getMapWidth() / 2;
        camera.position.y = getMap().getMapHeight() / 2;
    }

    public void buildGroup(int mapWidth, int mapHeight) {
        lastUpdateTime = 0;
        group = new Group() {
            @Override
            public void act(float delta) {
                float time = GdxAI.getTimepiece().getTime();
                if (lastUpdateTime != time) {
                    lastUpdateTime = time;
                    super.act(GdxAI.getTimepiece().getDeltaTime());
                }
            }
        };
        getStage().getRoot().addActorAt(0, group);
        group.setSize(mapWidth, mapHeight);
        group.addActor(pointer);
    }

    private void buildCharacters() {
        this.characters = new Array<SteeringActor>();
        this.area = new InteractionArea(this.characters, group);



        //buildTarget();
        //buildZombies();
        //characters.add(character);
        characters.add(pointer);
    }

    public void draw() {
        hexRenderer.setView(getCamera());
        hexRenderer.render();
        if (drawDebug) {
            drawShapes(characters);
        }
        stage.act();
        stage.draw();
    }

    public void drawShapes(Array<SteeringActor> objects) {
        for (SteeringActor obj : objects) {
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(0, 1, 0, 1);
            shapeRenderer.setProjectionMatrix(getCamera().combined);
            /*float angle = char0Proximity.getAngle() * MathUtils.radiansToDegrees;
            float radius = obj.getBoundingRadius() / getCamera().zoom;
            shapeRenderer.arc(obj.getPosition().x, obj.getPosition().y, radius,
                    obj.getOrientation() * MathUtils.radiansToDegrees, 180);*/
            shapeRenderer.circle(obj.getPosition().x, obj.getPosition().y, 20);
            shapeRenderer.end();
        }

    }

    public Vector2 screenToStageCoordinates(float screenX, float screenY) {
        Vector2 pos = new Vector2(screenX, screenY);
        return getStage().screenToStageCoordinates(pos);
    }

    public Vector2 stageToLocalCoordinates(float screenX, float screenY) {
        Vector2 pos = new Vector2(screenX, screenY);
        pos = getStage().getViewport().project(pos);
        return pos;
    }

    protected void setRandomNonOverlappingPosition(SteeringActor character, Array<SteeringActor> others, float minDistanceFromBoundary) {
        int maxTries = Math.max(100, others.size * others.size);
        SET_NEW_POS:
        while (--maxTries >= 0) {
            character.setPosition(MathUtils.random(getStage().getWidth()), MathUtils.random(getStage().getHeight()), Align.center);
            character.getPosition().set(character.getX(Align.center), character.getY(Align.center));
            for (int i = 0; i < others.size; i++) {
                SteeringActor other = (SteeringActor) others.get(i);
                if (character.getPosition().dst(other.getPosition()) <= character.getBoundingRadius() + other.getBoundingRadius()
                        + minDistanceFromBoundary) continue SET_NEW_POS;
            }
            return;
        }
        throw new GdxRuntimeException("Probable infinite loop detected");
    }

    public void dispose() {
        group.remove();
        group = null;
        map.dispose();
        shapeRenderer.dispose();
        hexRenderer.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    public GameInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public TiledBackground getMap() {
        return map;
    }
}

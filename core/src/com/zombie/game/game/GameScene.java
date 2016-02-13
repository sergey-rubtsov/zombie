package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.maps.tiled.renderers.HexagonalTiledMapRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.badlogic.gdx.utils.viewport.*;
import com.zombie.game.actors.ActorFactory;
import com.zombie.game.actors.Pointer;
import com.zombie.game.steering.SteeringActor;

public class GameScene {

    private static final boolean DEBUG_STAGE = false;

    GameInputProcessor inputProcessor;

    private Pointer pointer;

    Array<SteeringActor> characters;

    boolean drawDebug;

    TiledBackground map;
    HexagonalTiledMapRenderer hexRenderer;
    ShapeRenderer shapeRenderer;

    private World world;
    private Body[] walls;

    public Stage stage;

    protected Group group;

    InteractionArea area;

    private float lastUpdateTime;
    OrthographicCamera camera;

    private final Frame frame;

    private final Array<SteeringActor> selected;

    public GameScene() {
        this.inputProcessor = new GameInputProcessor(this);
        frame = new Frame();
        selected = new Array<SteeringActor>();

        camera = new OrthographicCamera();
        world = createWorld();

        this.map = new TiledBackground(100, 100);
        Viewport viewport = new ScreenViewport(camera);
        stage = new Stage(viewport);
        stage.setDebugAll(DEBUG_STAGE);
        this.pointer = new Pointer();
        this.pointer.getPosition().x = map.getMapWidth() / 2;
        this.pointer.getPosition().y = map.getMapHeight() / 2;

    }

    public void create() {
        world = createWorld();
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
        for (int i = 0; i < 400; i++) {
            ActorFactory.getRandomZombie(area);
        }
        characters.add(pointer);
    }

    public void draw() {
        hexRenderer.setView(getCamera());
        hexRenderer.render();
        drawFrame();
        drawShapes(selected);
        stage.act();
        stage.draw();
    }

    public void drawFrame() {
        if (frame.isOpened()) {
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(frame.getColor());
            shapeRenderer.setProjectionMatrix(getCamera().combined);
            Gdx.gl.glLineWidth(2);
            shapeRenderer.rect(frame.getPointA().x, frame.getPointA().y, frame.getPointC().x - frame.getPointA().x, frame.getPointC().y -  frame.getPointA().y);
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
        }
        for (SteeringActor obj : selected) {
            shapeRenderer.begin(ShapeType.Line);
            shapeRenderer.setColor(obj.getSelectionColor());
            shapeRenderer.setProjectionMatrix(getCamera().combined);
            Gdx.gl.glLineWidth(4);
            shapeRenderer.circle(obj.getPosition().x, obj.getPosition().y, 40);
            shapeRenderer.end();
            Gdx.gl.glLineWidth(1);
        }
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
        world.dispose();
        shapeRenderer.dispose();
        hexRenderer.dispose();
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public Stage getStage() {
        return stage;
    }

    public TiledBackground getMap() {
        return map;
    }

    public Vector2 getUnprojectPosition(int screenX, int screenY) {
        Vector3 temp = camera.unproject(new Vector3(screenX, screenY, 0));
        return new Vector2(temp.x, temp.y);
    }

    public void scrollCamera(int amount) {
        if (camera.zoom + amount >= 1){
            camera.zoom = camera.zoom + amount;
        }
    }

    /** Instantiate a new World with no gravity and tell it to sleep when possible. */
    public World createWorld() {
        return createWorld(0);
    }

    /** Instantiate a new World with the given gravity and tell it to sleep when possible. */
    public World createWorld(float y) {
        return new World(new Vector2(0, y), true);
    }

    public void update(float deltaTime) {
        // Update box2d world
        world.step(deltaTime, 8, 3);
        inputProcessor.processMoveCamera(deltaTime);
    }

    public void moveUpCamera(float deltaTime) {
        camera.position.y = camera.position.y + camera.zoom * 500 * deltaTime;
    }

    public void moveDownCamera(float deltaTime) {
        camera.position.y = camera.position.y - camera.zoom * 500 * deltaTime;
    }

    public void moveLeftCamera(float deltaTime) {
        camera.position.x = camera.position.x - camera.zoom * 500 * deltaTime;
    }

    public void moveRightCamera(float deltaTime) {
        camera.position.x = camera.position.x + camera.zoom * 500 * deltaTime;
    }

    public GameInputProcessor getInputProcessor() {
        return inputProcessor;
    }

    public void openFrame(Vector2 unprojectPosition, int button) {
        frame.setPointA(unprojectPosition);
        frame.setColor(button);
        frame.open();
    }

    public void closeFrame(Vector2 unprojectPosition) {
        frame.setPointC(unprojectPosition);
        area.selectCharacters(frame.getPointA(), frame.getPointC(), selected, frame.getColor());
        frame.close();
    }

    public void stretchFrame(Vector2 unprojectPosition) {
        frame.setPointC(unprojectPosition);
    }
}

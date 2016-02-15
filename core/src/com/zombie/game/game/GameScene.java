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
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.zombie.game.actors.ActorFactory;
import com.zombie.game.actors.Pointer;
import com.zombie.game.actors.SteeringActor;

public class GameScene implements EventListener {

    private static final boolean DEBUG_STAGE = false;

    SceneInputProcessor inputProcessor;

    private Pointer pointer;

    Array<SteeringActor> characters;

    boolean drawDebug;

    TiledBackground map;
    HexagonalTiledMapRenderer hexRenderer;
    ShapeRenderer shapeRenderer;

    private World world;
    private Body[] walls;

    public GameStage stage;

    protected Group group;

    InteractionArea area;

    private float lastUpdateTime;
    OrthographicCamera camera;

    private final Frame frame;

    private final Array<SteeringActor> selectedGreen;

    private final Array<SteeringActor> selectedRed;

    private SteeringActor chosen;

    public GameScene() {
        this.inputProcessor = new SceneInputProcessor(this);
        frame = new Frame();
        selectedGreen = new Array<SteeringActor>();
        selectedRed = new Array<SteeringActor>();

        camera = new OrthographicCamera();
        world = createWorld();

        this.map = new TiledBackground(1);
        Viewport viewport = new ScreenViewport(camera);
        stage = new GameStage(viewport);
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
        group.addListener(this);
    }

    private void buildCharacters() {
        this.characters = new Array<SteeringActor>();
        this.area = new InteractionArea(this.characters, group);
        for (int i = 0; i < 40; i++) {
            ActorFactory.getRandomZombie(area);
        }
        characters.add(pointer);
    }

    public void draw() {
        hexRenderer.setView(getCamera());
        hexRenderer.render();
        drawShapes();
        stage.act();
        stage.draw();
    }

    public void drawShapes() {
        shapeRenderer.setProjectionMatrix(getCamera().combined);
        if (frame.isOpened()) {
            shapeRenderer.begin(ShapeType.Line);
            Gdx.gl.glLineWidth(4);
            shapeRenderer.setColor(frame.getColor());
            shapeRenderer.rect(frame.getPointA().x, frame.getPointA().y, frame.getPointC().x - frame.getPointA().x, frame.getPointC().y -  frame.getPointA().y);
            shapeRenderer.end();
        }
        shapeRenderer.begin(ShapeType.Line);
        Gdx.gl.glLineWidth(2);
        if (chosen != null) {
            if (chosen.isActive()) {
                shapeRenderer.setColor(Color.GOLD);
                shapeRenderer.circle(chosen.getPosition().x, chosen.getPosition().y, chosen.getBoundingRadius() * 2f);
            } else {
                //shapeRenderer.setColor(chosen.getGroupColor());
                //shapeRenderer.circle(chosen.getPosition().x, chosen.getPosition().y, chosen.getBoundingRadius());
            }
        } else {
            for (SteeringActor obj : area.getCharacters()) {
                if (Color.CLEAR.equals(obj.getGroupColor())) continue;
                shapeRenderer.setColor(obj.getGroupColor());
                shapeRenderer.circle(obj.getPosition().x, obj.getPosition().y, obj.getBoundingRadius());
            }
        }
        shapeRenderer.end();
        Gdx.gl.glLineWidth(1);
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

    public GameStage getStage() {
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
            camera.update();
            //Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
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

    public SceneInputProcessor getSceneInputProcessor() {
        return inputProcessor;
    }

    public void openFrame(Vector2 unprojectPosition, int button) {
        frame.setPointA(unprojectPosition);
        frame.setColor(button);
        frame.open();
    }

    public void closeFrame(Vector2 unprojectPosition) {
        frame.setPointC(unprojectPosition);
        Array<SteeringActor> arr;
        if (Color.RED == frame.getColor()) {
            arr = selectedRed;
        } else {
            arr = selectedGreen;
        }
        area.selectCharacters(frame.getPointA(), frame.getPointC(), arr, frame.getColor());
        frame.close();
    }

    public void stretchFrame(Vector2 unprojectPosition) {
        frame.setPointC(unprojectPosition);
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

    public Frame getFrame() {
        return frame;
    }

    public Array<SteeringActor> getSelectedGreen() {
        return selectedGreen;
    }

    public Array<SteeringActor> getSelectedRed() {
        return selectedRed;
    }

    public SteeringActor getChosen() {
        return chosen;
    }

    public void setChosen(SteeringActor chosen) {
        this.chosen = chosen;
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof ActorEvent) {
            chosen = ((ActorEvent) event).getSteeringActor();
        }
        return true;
    }
}

package com.zombie.game.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ai.GdxAI;
import com.badlogic.gdx.graphics.Camera;
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
import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.viewport.*;
import com.zombie.game.actors.ActorFactory;
import com.zombie.game.actors.Mob;
import com.zombie.game.actors.Pointer;
import com.zombie.game.actors.SteeringActor;
import com.zombie.game.events.ActorHighlightedEvent;

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
    GameCamera camera;

    private final Frame frame;

    private final Mob greenMob;

    private final Mob redMob;

    private SteeringActor highlighted;

    public GameScene() {
        this.inputProcessor = new SceneInputProcessor(this);
        frame = new Frame();
        greenMob = new Mob(Color.GREEN);
        redMob = new Mob(Color.RED);

        camera = new GameCamera();

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
        camera.position.x = getMap().getMapWidth() / 2;
        camera.position.y = getMap().getMapHeight() / 2;
        Gdx.input.setCursorPosition(Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() / 2);
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
        float width = camera.viewportWidth * camera.zoom;
        float height = camera.viewportHeight * camera.zoom;
        hexRenderer.setView(camera.combined, camera.position.x - width / 2, camera.position.y - height / 2, width, height);
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
            shapeRenderer.rect(frame.getPointA().x, frame.getPointA().y, frame.getPointC().x - frame.getPointA().x, frame.getPointC().y - frame.getPointA().y);
            shapeRenderer.end();
        }
        shapeRenderer.begin(ShapeType.Line);
        Gdx.gl.glLineWidth(2);
        if (highlighted != null && highlighted.isActive()) {
            shapeRenderer.setColor(Color.GOLD);
            shapeRenderer.circle(highlighted.getPosition().x, highlighted.getPosition().y, highlighted.getBoundingRadius() * 2f);
        }
        for (SteeringActor obj : greenMob.getActors()) {
            shapeRenderer.setColor(greenMob.getColor());
            shapeRenderer.circle(obj.getPosition().x, obj.getPosition().y, obj.getBoundingRadius());
        }
        for (SteeringActor obj : redMob.getActors()) {
            shapeRenderer.setColor(redMob.getColor());
            shapeRenderer.circle(obj.getPosition().x, obj.getPosition().y, obj.getBoundingRadius());
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

    public Camera getCamera() {
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
        redMob.deleteActors();
        greenMob.deleteActors();
    }

    public void closeFrame(Vector2 unprojectPosition) {
        frame.setPointC(unprojectPosition);
        Mob mob;
        if (Color.RED == frame.getColor()) {
            mob = redMob;
        } else {
            mob = greenMob;
        }
        enrollCharacters(frame.getPointA(), frame.getPointC(), area.getCharacters(), mob);
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

    public SteeringActor getHighlighted() {
        return highlighted;
    }

    public void setHighlighted(SteeringActor highlighted) {
        this.highlighted = highlighted;
    }

    @Override
    public boolean handle(Event event) {
        if (event instanceof ActorHighlightedEvent) {
            highlighted = ((ActorHighlightedEvent) event).getSteeringActor();
        }
        return true;
    }

    public Mob getGreenMob() {
        return greenMob;
    }

    public Mob addCharactersToMob(Vector2 a, Vector2 c, Array<SteeringActor> characters, Mob mob) {
        for (SteeringActor actor : characters) {
            actor.deselect();
            float x = actor.getPosition().x;
            float y = actor.getPosition().y;
            if ((a.x < x && x < c.x) || (c.x < x && x < a.x)) {
                if ((a.y < y && y < c.y) || (c.y < y && y < a.y)) {
                    mob.addActor(actor);
                    continue;
                }
            }
        }
        return mob;
    }

    public Mob enrollCharacters(Vector2 a, Vector2 c, Array<SteeringActor> characters, Mob mob) {
        mob.deleteActors();
        return addCharactersToMob(a, c, characters, mob);
    }
}

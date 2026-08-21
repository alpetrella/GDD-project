package com.gdd.game;

import android.graphics.Canvas;
import android.graphics.Color;

import com.badlogic.androidgames.framework.Input;
import com.gdd.game.framework.Camera;
import com.gdd.game.framework.actors.Actor;
import com.gdd.game.framework.actors.ActorTag;
import com.gdd.game.framework.actors.Transform;
import com.gdd.game.framework.components.BoxRenderComp;
import com.gdd.game.framework.components.ComponentType;
import com.gdd.game.framework.components.InputComponent;
import com.gdd.game.framework.factories.AntFactory;
import com.gdd.game.framework.factories.FoodFactory;
import com.gdd.game.framework.factories.WildInsectFactory;
import com.gdd.game.framework.factories.NestFactory;
import com.gdd.game.framework.systems.AudioSystem;
import com.gdd.game.framework.systems.InputSystem;
import com.gdd.game.framework.systems.PhysicsSystem;
import com.gdd.game.framework.systems.RenderSystem;

import java.util.ArrayList;
import java.util.List;

/*
 * Gestisce gli actor nella scena.
 */
public class GameWorld {

    private GameScreen game;
    private Camera camera;

    // MANAGERS
    private InputSystem sInput;
    private RenderSystem sGraphics;
    private AudioSystem sAudio;
    private PhysicsSystem sPhysics;

    // FACTORIES
    private final AntFactory fAnt;
    private final WildInsectFactory fInsect;
    private final FoodFactory fFood;
    private final NestFactory fNest;

    // ACTORS
    private long nextId = 0;
    private List<Actor> actors;
    private Actor cardArea;


    // ------------------------------------------------------------------
    // Init
    // ------------------------------------------------------------------

    public GameWorld(GameScreen game) {

        this.game = game;

        // SCENE
        camera = new Camera(game.cameraView,
                Settings.worldWidth, Settings.worldHeight, // worldWidth, worldHeight in metri
                Settings.fbufferWidth, Settings.fbufferHeight // pixel, fisso, lo conosci già
        );

        // SYSTEMS
        sInput = new InputSystem(camera);
        sInput.gw = this;
        sGraphics = new RenderSystem();
        sAudio = new AudioSystem();
        sPhysics = new PhysicsSystem(Settings.worldWidth, Settings.worldHeight);

        // FACTORIES
        fAnt = new AntFactory(sPhysics);
        fInsect = new WildInsectFactory(sPhysics);
        fFood = new FoodFactory(sPhysics);
        fNest = new NestFactory(sPhysics);

        // TEST
        actors = new ArrayList<>();
        initActors();
        buildArea();
    }

    public void initActors() {

        Actor nest;
        for (int i = 1; i <= 2; i++) {
            nest = new Actor(nextId++);
            fNest.makeNest(nest,0, 0,0);
            actors.add(nest);
        }

        Actor ant;
        for (int i = 1; i <= 4; i++) {
            ant = new Actor(nextId++);
            fAnt.makeAnt(ant, 2.5f * i, 0, 15f * i);
            actors.add(ant);
        }

        Actor wasp;
        for (int i = 1; i <= 2; i++) {
            wasp = new Actor(nextId++);
            fInsect.makeWasp(wasp,-3.5f * i, 0, 10f * i);
            actors.add(wasp);
        }

        Actor food;
        for (int i = 1; i <= 2; i++) {
            food = new Actor(nextId++);
            fFood.makeFood(food,-3.5f * i, 5, 35f * i);
            actors.add(food);
        }
    }


    // ------------------------------------------------------------------
    // Game Loop
    // ------------------------------------------------------------------

    public synchronized void processInput(Input.TouchEvent event)  {
        sInput.processInput(event);
    }

    public synchronized void update(float deltaTime)  {
        // update physics
        sPhysics.step(deltaTime);
        sPhysics.sync(actors); // sync transform
    }

    public synchronized void render(Canvas canvas)
    {
        // draw actors
        sGraphics.render(canvas, camera, actors);
    }


    // ------------------------------------------------------------------
    // Gameplay
    // ------------------------------------------------------------------

    public void showCardArea(boolean flag) {


    }

    private void buildArea() {
        cardArea = new Actor(nextId++);
        cardArea.tag = ActorTag.EMPTY;
        cardArea.transform.x = -5;
        cardArea.transform.halfWidth = 2.5f;
        cardArea.transform.halfHeight = 2.5f;
        cardArea.addComponent(new InputComponent());
        cardArea.addComponent(new BoxRenderComp(Color.BLACK, false));
        actors.add(cardArea);
    }

    public Actor hit(float worldX, float worldY) {
        int n = actors.size();
        for(int i=0; i<n; i++) {

            Actor actor = actors.get(i);
            Transform t = actor.getTransform();

            if(actor.getComponent(ComponentType.INPUT) == null)
                continue;

            if(worldX >= t.x - t.halfWidth && worldX <= t.x + t.halfWidth &&
                    worldY >= t.y - t.halfHeight && worldY <= t.y + t.halfHeight) {
                return actor;
            }
        }
        return null;
    }
}

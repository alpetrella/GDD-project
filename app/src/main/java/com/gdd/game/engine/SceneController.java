package com.gdd.game.engine;

import android.graphics.Canvas;

import com.badlogic.androidgames.framework.Input;
import com.gdd.game.Game;
import com.gdd.game.Settings;
import com.gdd.game.engine.actors.Actor;
import com.gdd.game.engine.factories.AntFactory;
import com.gdd.game.engine.factories.FoodFactory;
import com.gdd.game.engine.factories.InsectFactory;
import com.gdd.game.engine.factories.NestFactory;
import com.gdd.game.engine.factories.ObstacleFactory;
import com.gdd.game.engine.managers.AudioManager;
import com.gdd.game.engine.managers.InputManager;
import com.gdd.game.engine.managers.PhysicsManager;
import com.gdd.game.engine.managers.RenderManager;

import java.util.ArrayList;
import java.util.List;

public class SceneController {

    private Game game;
    private Camera camera;

    // MANAGERS
    private InputManager sInput;
    private RenderManager sGraphics;
    private AudioManager sAudio;
    private PhysicsManager sPhysics;

    // FACTORIES
    private final AntFactory antFactory;
    private final InsectFactory insectFactory;
    private final FoodFactory foodFactory;
    private final NestFactory nestFactory;
    private final ObstacleFactory obstacleFactory;

    // ACTORS
    private long nextId = 0;
    private List<Actor> actors;


    // ------------------------------------------------------------------
    // Init
    // ------------------------------------------------------------------

    public SceneController(Game game) {
        this.game = game;

        // SCENE
        camera = new Camera(game.cameraView,
                Settings.worldWidth, Settings.worldHeight, // worldWidth, worldHeight in metri
                Settings.fbufferWidth, Settings.fbufferHeight // pixel, fisso, lo conosci già
        );

        // MANAGERS
        sInput = new InputManager(camera);
        sGraphics = new RenderManager();
        sAudio = new AudioManager();
        sPhysics = new PhysicsManager(Settings.worldWidth, Settings.worldHeight);
        //sPhysics.setGravity(0, 5);

        // FACTORIES
        antFactory = new AntFactory(sPhysics);
        insectFactory = new InsectFactory(sPhysics);
        foodFactory = new FoodFactory(sPhysics);
        nestFactory = new NestFactory(sPhysics);
        obstacleFactory = new ObstacleFactory(sPhysics);

        // TEST
        actors = new ArrayList<>();
        initActors();
    }

    public void initActors() {

        Actor nest;
        for (int i = 1; i <= 2; i++) {
            nest = new Actor(nextId++);
            nestFactory.makeNest(nest,0, 0,0);
            actors.add(nest);
        }

        Actor ant;
        for (int i = 1; i <= 4; i++) {
            ant = new Actor(nextId++);
            antFactory.makeAnt(ant, 2.5f * i, 0, 15f * i);
            actors.add(ant);
        }

        Actor wasp;
        for (int i = 1; i <= 2; i++) {
            wasp = new Actor(nextId++);
            insectFactory.makeWasp(wasp,-3.5f * i, 0, 10f * i);
            actors.add(wasp);
        }

        Actor food;
        for (int i = 1; i <= 2; i++) {
            food = new Actor(nextId++);
            foodFactory.makeFood(food,-3.5f * i, 5, 35f * i);
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
        sPhysics.syncTransform(actors);
    }

    public synchronized void render(Canvas canvas)
    {
        // draw actors
        sGraphics.render(canvas, camera, actors);
    }

}

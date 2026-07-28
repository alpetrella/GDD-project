package com.gdd.game.engine;

import android.graphics.Canvas;

import com.badlogic.androidgames.framework.Input;
import com.gdd.game.Game;
import com.gdd.game.Settings;
import com.gdd.game.engine.actors.Actor;
import com.gdd.game.engine.factories.InsectFactory;
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
    private InsectFactory insectFactory;

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

        // FACTORIES
        insectFactory = new InsectFactory(sPhysics);

        // TEST
        actors = new ArrayList<>();
        initActors();
    }

    public void initActors() {

        Actor ant;
        for (int i = 1; i <= 5; i++) {
            ant = new Actor(nextId++);
            ant = insectFactory.makeAnt(ant, 1.5f * i, -5f, 15f * i);
            actors.add(ant);
        }

        Actor wasp;
        for (int i = 1; i <= 5; i++) {
            wasp = new Actor(nextId++);
            wasp = insectFactory.makeWasp(wasp,-1.5f * i, -8f, 10f * i);
            actors.add(wasp);
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

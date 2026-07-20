package com.gdd.game.engine.scene;

import android.graphics.Canvas;

import com.badlogic.androidgames.framework.Input;
import com.gdd.game.Game;
import com.gdd.game.Settings;
import com.gdd.game.engine.Actor;
import com.gdd.game.engine.ComponentType;
import com.gdd.game.engine.PhysicsSystem;
import com.gdd.game.engine.components.PhysicsComponent;
import com.gdd.game.engine.components.RectDrawableComponent;
import com.gdd.game.engine.components.TransformComponent;

import java.util.ArrayList;
import java.util.List;

public class SceneController {

    private Game game;
    private Camera camera;

    private SceneInput sInput;
    private SceneGraphics sGraphics;
    private SceneAudio sAudio;
    private PhysicsSystem sPhysics;

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

        sInput = new SceneInput(camera);
        sGraphics = new SceneGraphics();
        sAudio = new SceneAudio();
        sPhysics = new PhysicsSystem();
        sPhysics.setGravity(0, 1);

        actors = new ArrayList<>();
        initActors();
    }

    public void initActors() {
        Actor a;
        TransformComponent tc;

        // TEST: physic actors
        for (int i = 0; i < 10; i++) {
            a = new Actor();

            float x = 0.5f * i;
            float y = -5f;
            float angle = 15f * i;

            a.addComponent(sPhysics.createComponent(x, y, angle));
            a.addComponent(new RectDrawableComponent());
            tc = a.getTransformComponent();
            actors.add(a);
        }

        // basic actor
        a = new Actor();
        a.addComponent( new RectDrawableComponent() );
        tc = a.getTransformComponent();
        tc.y = 5;
        tc.angle = 50;
        actors.add(a);
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
        // sync transform-physics
        for (Actor a : actors) {
            PhysicsComponent pc = (PhysicsComponent) a.getComponent(ComponentType.PHYSICS);
            TransformComponent t = a.getTransformComponent();
            if(pc != null) {
                t.x = pc.getX();
                t.y = pc.getY();
                t.angle = pc.getAngle();
            }
        }
    }


    public synchronized void render(Canvas canvas)
    {
        // draw actors
        sGraphics.render(canvas, camera, actors);
    }

}

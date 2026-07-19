package com.gdd.game.engine;

import android.graphics.Canvas;

import com.badlogic.androidgames.framework.Input;
import com.gdd.game.Game;
import com.gdd.game.Settings;

public class SceneController {

    private Game game;
    private Camera camera;

    private SceneInput sInput;
    private SceneGraphics sGraphics;
    private SceneAudio sAudio;
    private ScenePhysics sPhysics;



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
    }


    // ------------------------------------------------------------------
    // Game Loop
    // ------------------------------------------------------------------

    public synchronized void processInput(Input.TouchEvent event)  {

    }

    public synchronized void update(float deltaTime)  {

    }


    public synchronized void render(Canvas canvas)
    {
        // background (clear the screen with black)
        canvas.drawARGB(255, 0, 0, 0);

    }


}

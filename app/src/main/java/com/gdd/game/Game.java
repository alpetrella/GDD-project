package com.gdd.game;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.TouchHandler;
import com.gdd.game.engine.Box;
import com.gdd.game.ui.Button;
import com.gdd.game.ui.Panel;
import com.gdd.game.ui.UIController;
import com.gdd.game.ui.Widget;
import com.gdd.game.ui.WidgetGroup;

public class Game {

    public enum Screen { START, GAMEPLAY }
    public enum State { PLAYING, PAUSED }
    public final Activity activity;
    public State state;
    public Screen screen;

    // Rendering
    public static final int fbufferWidth = Settings.fbufferWidth,
            fbufferHeight = Settings.fbufferHeight;
    public Bitmap frameBuffer;
    public final Canvas canvas;

    // Controller
    private final UIController uiController;
    private GameWorld gameWorld;

    public final Box worldSize, // physics world's size (in meters)
            screenSize, // smartphone's screen size (in pixel)
            cameraView; // camera position and size (in meters)

    // Input
    private TouchHandler touchHandler;
    private boolean consumed;

    // Menu
    private WidgetGroup mainMenu, gameMenu;
    private WidgetGroup pausePopup;
    private Panel cardPanel;


    /*
     * Constructor.
     */
    public Game(Activity activity, Bitmap frameBuffer, Box worldSize, Box screenSize) {

        screen = Screen.GAMEPLAY;
        state = State.PLAYING;

        this.worldSize = worldSize;
        this.screenSize = screenSize;
        this.activity = activity;
        this.frameBuffer = frameBuffer;

        cameraView = new Box(worldSize); // di default vede l'intero mondo
        canvas = new Canvas(frameBuffer);

        gameWorld = new GameWorld(this);

        uiController = new UIController();
        initUI();
    }


    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------

    private void initUI() {

        // ***** GAMEPLAY MENU *****
        gameMenu = new Panel(0, 0, fbufferWidth, fbufferHeight);

        Button pauseButton = new Button(1100, 30, 100, 50, "PAUSE");
        gameMenu.addChild(pauseButton);

        uiController.setRoot(gameMenu);

        initUIGameplay();

        // ***** PAUSE POPUP *****
        pausePopup = new Panel(0, 0, fbufferWidth, fbufferHeight);

        Button resumeButton = new Button(500, 150, 250, 200, "RESUME");
        resumeButton.setTextSize(0.3f);
        pausePopup.addChild(resumeButton);

        // ***** CLICKS *****
        pauseButton.setOnClickListener(b -> {
            uiController.showPopup(pausePopup);
            state = State.PAUSED;
        });

        resumeButton.setOnClickListener(b -> {
            uiController.hideTopPopup();
            state = State.PLAYING;
        });
    }

    private void initUIGameplay() {

        cardPanel = new Panel(10, 360, 150, 360);
        cardPanel.setBorder(true, Color.LTGRAY);
        cardPanel.setTouchable(Widget.Touchable.ENABLED);
        gameMenu.addChild(cardPanel);

        Button buttonCard = new Button(30, 100, 100, 200, "CARD");
        buttonCard.setTextSize(0.2f);
        cardPanel.addChild(buttonCard);

        Button confirmCard = new Button(10, 10, 50, 50, "Y");
        confirmCard.setColor(Color.GREEN);
        confirmCard.setVisible(false);
        cardPanel.addChild(confirmCard);

        Button cancelCard = new Button(80, 10, 50, 50, "X");
        cancelCard.setColor(Color.RED);
        cancelCard.setVisible(false);
        cardPanel.addChild(cancelCard);

        // ***** ON CLICK *****
        buttonCard.setOnClickListener(b -> {
            confirmCard.setVisible(true);
            cancelCard.setVisible(true);
            buttonCard.disable();

            gameWorld.showCardArea(true);
        });

        confirmCard.setOnClickListener(b -> {
            confirmCard.setVisible(false);
            cancelCard.setVisible(false);
            buttonCard.enable();

            gameWorld.showCardArea(false);
        });

        cancelCard.setOnClickListener(b -> {
            confirmCard.setVisible(false);
            cancelCard.setVisible(false);
            buttonCard.enable();

            gameWorld.showCardArea(false);
        });

    }


    // ------------------------------------------------------------------
    // Getter / Setter
    // ------------------------------------------------------------------

    public void setTouchHandler(TouchHandler touchHandler) {
        this.touchHandler = touchHandler;
    }


    // ------------------------------------------------------------------
    // Game Loop
    // ------------------------------------------------------------------

    public synchronized void update(float deltaTime)  {

        // Handle touch events
        for (Input.TouchEvent event: touchHandler.getTouchEvents()) {
            consumed = uiController.processInput(event);

             if(!consumed) {
                 if(screen == Screen.GAMEPLAY && state == State.PLAYING) {
                     gameWorld.processInput(event);
                 }
             }
        }

        // update scene state
        if(screen == Screen.GAMEPLAY && state == State.PLAYING) {
            gameWorld.update(deltaTime);
        }
    }


    public synchronized void render()
    {
        // clear the screen with white
        canvas.drawARGB(255, 255, 255, 255);
        // render scene
        if(screen == Screen.GAMEPLAY) {
            gameWorld.render(canvas);
        }
        // render ui
        uiController.draw(canvas);
    }


    /*
    @Override
    protected void finalize() throws Throwable
    {
        try {
            world.delete();
        } finally {
            super.finalize();
        }
    }
    */

}

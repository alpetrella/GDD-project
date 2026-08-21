package com.gdd.game.screen;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.badlogic.androidgames.framework.Input;
import com.badlogic.androidgames.framework.impl.TouchHandler;
import com.gdd.game.GameWorld;
import com.gdd.game.Settings;
import com.gdd.game.framework.Box;
import com.gdd.game.framework.Game;
import com.gdd.game.framework.Screen;
import com.gdd.game.ui.Panel;
import com.gdd.game.ui.TextButton;
import com.gdd.game.ui.UIController;
import com.gdd.game.ui.Widget;
import com.gdd.game.ui.WidgetGroup;

public class GameScreen extends Screen {

    public enum State { PLAYING, PAUSED }
    public Game game;
    public State state;

    // Rendering
    public static final int fbufferWidth = Settings.fbufferWidth,
            fbufferHeight = Settings.fbufferHeight;
    public Bitmap frameBuffer;
    public Canvas canvas;

    // Controller
    private UIController uiController;
    private GameWorld gameWorld;

    public Box worldSize, // physics world's size (in meters)
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
    public GameScreen(Game game) {
        super(game);

        // World: physical simulation
        float halfWorldWidth = Settings.worldWidth / 2;
        float halfWorldHeight = Settings.worldHeight / 2;
        worldSize = new Box(-halfWorldWidth, -halfWorldHeight, halfWorldWidth, halfWorldHeight);
        screenSize = game.getScreensize();
        frameBuffer = game.getFramebuffer();

        touchHandler = game.getTouchHandler();

        //Activity activity, Bitmap frameBuffer, Box worldSize, Box screenSize)
        state = State.PLAYING;

        cameraView = new Box(worldSize); // di default vede l'intero mondo
        canvas = new Canvas(frameBuffer);

        gameWorld = new GameWorld(this);

        uiController = game.getUIController();
        initUI();
    }


    // ------------------------------------------------------------------
    // Initialize
    // ------------------------------------------------------------------

    private void initUI() {

        uiController.reset();

        // ***** GAMEPLAY MENU *****
        gameMenu = new Panel(0, 0, fbufferWidth, fbufferHeight);

        TextButton pauseButton = new TextButton(1100, 30, 100, 50);
        pauseButton.setText("PAUSE");
        gameMenu.addChild(pauseButton);

        initUIGameplay();

        // ***** PAUSE POPUP *****
        pausePopup = new Panel(0, 0, fbufferWidth, fbufferHeight);

        TextButton resumeButton = new TextButton(500, 150, 250, 200);
        resumeButton.setText("RESUME");
        resumeButton.setTextSize(0.3f);
        pausePopup.addChild(resumeButton);

        // ***** CLICKS *****
        pauseButton.setOnClickListener(b -> {
            // TODO: chiamare il reset di input system
            uiController.showPopup(pausePopup);
            state = State.PAUSED;
        });

        resumeButton.setOnClickListener(b -> {
            uiController.hideTopPopup();
            state = State.PLAYING;
        });

        uiController.setRoot(gameMenu);
        uiController.updateLayout();
    }

    private void initUIGameplay() {

        cardPanel = new Panel(10, 360, 150, 360);
        cardPanel.setBorder(true, Color.LTGRAY);
        cardPanel.setTouchable(Widget.Touchable.ENABLED);
        gameMenu.addChild(cardPanel);

        TextButton buttonCard = new TextButton(30, 100, 100, 200);
        buttonCard.setText("CARD");
        buttonCard.setTextSize(0.2f);
        cardPanel.addChild(buttonCard);

        TextButton confirmCard = new TextButton(10, 10, 50, 50);
        confirmCard.setText("Y");
        confirmCard.setColor(Color.GREEN);
        confirmCard.setVisible(false);
        cardPanel.addChild(confirmCard);

        TextButton cancelCard = new TextButton(80, 10, 50, 50);
        cancelCard.setText("X");
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

    @Override
    public void update(float deltaTime)  {

        // Handle touch events
        for (Input.TouchEvent event: touchHandler.getTouchEvents()) {
            consumed = uiController.processInput(event);

             if(!consumed) {
                 if(state == State.PLAYING) {
                     gameWorld.processInput(event);
                 }
             }
        }

        // update scene state
        if(state == State.PLAYING) {
            gameWorld.update(deltaTime);
        }
    }


    @Override
    public void render()
    {
        // clear the screen with white
        canvas.drawARGB(255, 255, 255, 255);
        // render scene
        gameWorld.render(canvas);
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


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}

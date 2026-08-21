package com.gdd.game;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import com.badlogic.androidgames.framework.Audio;
import com.badlogic.androidgames.framework.impl.AndroidAudio;
import com.badlogic.androidgames.framework.impl.MultiTouchHandler;
import com.badlogic.androidgames.framework.impl.TouchHandler;
import com.gdd.game.framework.AndroidFastRenderView;
import com.gdd.game.framework.Box;
import com.gdd.game.framework.Game;
import com.gdd.game.framework.Screen;

public class MainActivity extends Activity implements Game {

    private AndroidFastRenderView renderView;
    private MultiTouchHandler touchHandler;
    public Screen screen;

    // the tag used for logging
    public static String TAG;


    public Box screenSize;
    public Bitmap frameBuffer;


    // ********************************
    //  Android callbacks
    // ********************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        System.loadLibrary("liquidfun");
        System.loadLibrary("liquidfun_jni");

        TAG = Assets.APPNAME;

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        var manager = getAssets();
        Assets.load(manager);

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        screenSize = new Box(0, 0, metrics.widthPixels, metrics.heightPixels);
        frameBuffer = Bitmap.createBitmap(Settings.fbufferWidth, Settings.fbufferHeight,
                Bitmap.Config.ARGB_8888);

        float scaleX = (float) Settings.fbufferWidth / metrics.widthPixels;
        float scaleY = (float) Settings.fbufferHeight / metrics.heightPixels;

        renderView = new AndroidFastRenderView(this, frameBuffer);
        touchHandler = new MultiTouchHandler(renderView, scaleX, scaleY); // scale inputs from screen to framebuffer coordinates;
        screen = getStartScreen();
        setContentView(renderView);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i("Main thread", "pause");
        renderView.pause(); // stops the main loop
        screen.pause();

        if (isFinishing())
            screen.dispose();
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i("Main thread", "stop");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i("Main thread", "resume");
        screen.resume();
        renderView.resume(); // starts game loop in a separate thread

        // persistence example
        SharedPreferences pref = getPreferences(Context.MODE_PRIVATE);
        int counter = pref.getInt("INFO", -1); // default value
        Log.i("Main thread", "read counter " + counter);
    }

    @Override
    public void setScreen(Screen screen) {
        if (screen == null)
            throw new IllegalArgumentException("Screen must not be null");

        this.screen.pause();
        this.screen.dispose();
        screen.resume();
        screen.update(0);
        this.screen = screen;
    }

    // ********************************
    //  Screen methods
    // ********************************

    public Screen getCurrentScreen() {
        return screen;
    }

    public Screen getStartScreen() {
        return new GameScreen(this);
    }

    public TouchHandler getTouchHandler() {
        return touchHandler;
    }

    public Bitmap getFramebuffer() {
        return frameBuffer;
    }

    public Box getScreensize() {
        return screenSize;
    }
}

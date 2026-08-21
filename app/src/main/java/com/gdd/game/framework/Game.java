package com.gdd.game.framework;

import android.graphics.Bitmap;

import com.badlogic.androidgames.framework.impl.TouchHandler;

public interface Game {

    public void setScreen(Screen screen);

    public Screen getCurrentScreen();

    public Screen getStartScreen();

    public TouchHandler getTouchHandler();

    public Bitmap getFramebuffer();

    public Box getScreensize();
}

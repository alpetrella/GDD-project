package com.gdd.game.engine.factories;

import android.graphics.Color;

public class ObstacleFactory {

    private final IPhysicsFactory physicsFactory;
    private final float width = 0.5f, height = 0.5f;
    private final int foodColor = Color.BLACK;


    public ObstacleFactory(IPhysicsFactory physicsFactory) {
        this.physicsFactory = physicsFactory;
    }
}

package com.gdd.game.engine.components;

import com.google.fpl.liquidfun.Body;

public class LiquidFunComponent extends PhysicsComponent {

    public Body body;

    public LiquidFunComponent(Body body) {
        this.body = body;
    }

    @Override
    public float getX() { return body.getPositionX(); }
    @Override
    public float getY() { return body.getPositionY(); }
    @Override
    public float getAngle() { return body.getAngle(); }
}

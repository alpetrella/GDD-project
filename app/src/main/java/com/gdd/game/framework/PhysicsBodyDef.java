package com.gdd.game.framework;

import com.google.fpl.liquidfun.BodyType;

public class PhysicsBodyDef {

    public enum ShapeType { CIRCLE, BOX }

    public BodyType bodyType = BodyType.dynamicBody;
    public float x, y;
    public float direction;

    public ShapeType shapeType = ShapeType.BOX;
    public float radius;       // usato se shapeType == CIRCLE
    public float width, height; // usati se shapeType == BOX
}

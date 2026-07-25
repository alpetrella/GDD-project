package com.gdd.game.engine.factories;

import android.graphics.Color;

import com.gdd.game.engine.PhysicsBodyDef;
import com.gdd.game.engine.components.PrimitiveDrawable;
import com.gdd.game.engine.core.Actor;
import com.gdd.game.engine.core.Shape;
import com.google.fpl.liquidfun.BodyType;

public class EnemyFactory {

    private final IPhysicsFactory physicsFactory;
    private final float width = 0.5f, height = 0.5f;

    private final int color = Color.RED;

    public EnemyFactory(IPhysicsFactory physicsFactory) {
        this.physicsFactory = physicsFactory;
    }

    public Actor makeWasp(Actor a, float x, float y, float direction) {

        if(physicsFactory == null || a == null)
            return null;

        a.setShape(new Shape.Box(width, height));
        a.addComponent(new PrimitiveDrawable(
                PrimitiveDrawable.Kind.BOX, color, true));

        PhysicsBodyDef physicsBodyDef = new PhysicsBodyDef();
        physicsBodyDef.bodyType = BodyType.dynamicBody;
        physicsBodyDef.shapeType = PhysicsBodyDef.ShapeType.BOX;
        physicsBodyDef.x = x;
        physicsBodyDef.y = y;
        physicsBodyDef.direction = direction;
        physicsBodyDef.width = width;
        physicsBodyDef.height = height;
        a.addComponent(physicsFactory.createComponent(physicsBodyDef));

        return a;
    }
}

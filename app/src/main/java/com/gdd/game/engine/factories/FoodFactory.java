package com.gdd.game.engine.factories;

import android.graphics.Color;

import com.gdd.game.engine.PhysicsBodyDef;
import com.gdd.game.engine.actors.Actor;
import com.gdd.game.engine.actors.ActorTag;
import com.gdd.game.engine.components.BoxRenderComp;
import com.google.fpl.liquidfun.BodyType;

public class FoodFactory {

    private final IPhysicsFactory physicsFactory;
    private final float width = 0.8f, height = 0.8f;
    private final int color = Color.MAGENTA;


    public FoodFactory(IPhysicsFactory physicsFactory) {
        this.physicsFactory = physicsFactory;
    }


    public void makeFood(Actor a, float x, float y, float direction) {

        if(physicsFactory == null || a == null)
            return;

        a.tag = ActorTag.FOOD;

        a.transform.halfWidth = width/2;
        a.transform.halfHeight = height/2;

        a.addComponent(new BoxRenderComp(color, true));

        addPhysics(a, x, y, direction);
    }



    private void addPhysics(Actor a, float x, float y, float direction) {

        PhysicsBodyDef physicsBodyDef = new PhysicsBodyDef();
        physicsBodyDef.bodyType = BodyType.dynamicBody;
        physicsBodyDef.shapeType = PhysicsBodyDef.ShapeType.BOX;
        physicsBodyDef.x = x;
        physicsBodyDef.y = y;
        physicsBodyDef.direction = direction;
        physicsBodyDef.width = width;
        physicsBodyDef.height = height;
        a.addComponent(physicsFactory.createComponent(physicsBodyDef));
    }
}

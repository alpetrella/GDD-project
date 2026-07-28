package com.gdd.game.engine.factories;

import android.graphics.Color;

import com.gdd.game.engine.PhysicsBodyDef;
import com.gdd.game.engine.actors.Actor;
import com.gdd.game.engine.actors.ActorTag;
import com.gdd.game.engine.actors.Faction;
import com.gdd.game.engine.actors.Species;
import com.gdd.game.engine.components.AliveComponent;
import com.gdd.game.engine.components.PrimitiveDrawable;
import com.google.fpl.liquidfun.BodyType;

public class NestFactory {

    private final IPhysicsFactory physicsFactory;
    private final float width = 2.0f, height = 2.0f;
    private final int color = Color.DKGRAY;


    public NestFactory(IPhysicsFactory physicsFactory) {
        this.physicsFactory = physicsFactory;
    }


    public void makeNest(Actor a, float x, float y, float direction) {

        if(physicsFactory == null || a == null)
            return;

        a.tag = ActorTag.NEST;

        a.transform.halfWidth = width/2;
        a.transform.halfHeight = height/2;

        a.addComponent(new PrimitiveDrawable(
                PrimitiveDrawable.Kind.BOX, color, true));

        addPhysics(a, x, y, direction);
    }

    private void addPhysics(Actor a, float x, float y, float direction) {

        PhysicsBodyDef physicsBodyDef = new PhysicsBodyDef();
        physicsBodyDef.bodyType = BodyType.staticBody;
        physicsBodyDef.shapeType = PhysicsBodyDef.ShapeType.BOX;
        physicsBodyDef.x = x;
        physicsBodyDef.y = y;
        physicsBodyDef.direction = direction;
        physicsBodyDef.width = width;
        physicsBodyDef.height = height;
        a.addComponent(physicsFactory.createComponent(physicsBodyDef));
    }
}

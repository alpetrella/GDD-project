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

public class WildInsectFactory {

    private final IPhysicsFactory physicsFactory;
    private final float width = 0.5f, height = 0.5f;
    private final int waspColor = Color.RED;

    public WildInsectFactory(IPhysicsFactory physicsFactory) {

        this.physicsFactory = physicsFactory;
    }


    public void makeWasp(Actor a, float x, float y, float direction) {

        if(physicsFactory == null || a == null)
            return;

        a.tag = ActorTag.INSECT;

        a.transform.halfWidth = width/2;
        a.transform.halfHeight = height/2;

        AliveComponent alive = new AliveComponent(Species.WASP, Faction.HOSTILE);
        a.addComponent(alive);

        a.addComponent(new PrimitiveDrawable(
                PrimitiveDrawable.Kind.BOX, waspColor, true));

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

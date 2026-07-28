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

public class InsectFactory {

    private final IPhysicsFactory physicsFactory;
    private final float width = 0.5f, height = 0.5f;
    private final int antColor = Color.BLUE;
    private final int waspColor = Color.RED;

    public InsectFactory(IPhysicsFactory physicsFactory) {

        this.physicsFactory = physicsFactory;
    }


    public Actor makeAnt(Actor a, float x, float y, float direction) {

        if(physicsFactory == null || a == null)
            return null;

        a.tag = ActorTag.INSECT;

        a.transform.halfWidth = width/2;
        a.transform.halfHeight = height/2;

        AliveComponent alive = new AliveComponent(Species.ANT, Faction.ALLY);
        a.addComponent(alive);

        a.addComponent(new PrimitiveDrawable(
                PrimitiveDrawable.Kind.BOX, antColor, true));

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


    public Actor makeWasp(Actor a, float x, float y, float direction) {

        if(physicsFactory == null || a == null)
            return null;

        a.tag = ActorTag.INSECT;

        a.transform.halfWidth = width/2;
        a.transform.halfHeight = height/2;

        AliveComponent alive = new AliveComponent(Species.WASP, Faction.ENEMY);
        a.addComponent(alive);

        a.addComponent(new PrimitiveDrawable(
                PrimitiveDrawable.Kind.BOX, waspColor, true));

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

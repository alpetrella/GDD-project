package com.gdd.game.engine.components;

import com.google.fpl.liquidfun.Body;

public class PhysicsComponent extends Component {

    public Body body;

    @Override
    public final ComponentType type() {
        return ComponentType.PHYSICS;
    }

    public PhysicsComponent(Body body) {
        this.body = body;
    }

    public void syncTransform() {
        owner.transform.x = body.getPositionX();
        owner.transform.y = body.getPositionY();
        owner.transform.angle = body.getAngle();
    }

    public float getX() { return body.getPositionX(); }

    public float getY() { return body.getPositionY(); }

    public float getAngle() { return body.getAngle(); }
}

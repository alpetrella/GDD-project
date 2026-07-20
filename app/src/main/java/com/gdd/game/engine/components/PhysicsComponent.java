package com.gdd.game.engine.components;

import com.gdd.game.engine.Component;
import com.gdd.game.engine.ComponentType;

public abstract class PhysicsComponent extends Component {

    public abstract float getX();
    public abstract float getY();
    public abstract float getAngle();

    @Override
    public final ComponentType type() {
        return ComponentType.PHYSICS;
    }
}

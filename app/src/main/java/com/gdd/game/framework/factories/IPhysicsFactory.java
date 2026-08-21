package com.gdd.game.framework.factories;

import com.gdd.game.framework.PhysicsBodyDef;
import com.gdd.game.framework.components.PhysicsComponent;

public interface IPhysicsFactory {

    public PhysicsComponent createComponent(PhysicsBodyDef def);
}

package com.gdd.game.engine.factories;

import com.gdd.game.engine.PhysicsBodyDef;
import com.gdd.game.engine.components.PhysicsComponent;

public interface IPhysicsFactory {

    public PhysicsComponent createComponent(PhysicsBodyDef def);
}

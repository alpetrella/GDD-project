package com.gdd.game.engine.components;

import com.gdd.game.engine.Component;
import com.gdd.game.engine.ComponentType;

public class TransformComponent extends Component {

    public float x, y, width, height, angle;

    @Override
    public final ComponentType type() {
        return ComponentType.TRANSFORM;
    }
}

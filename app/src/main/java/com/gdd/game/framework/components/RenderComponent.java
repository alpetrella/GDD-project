package com.gdd.game.framework.components;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.gdd.game.framework.ScreenParams;

public abstract class RenderComponent extends Component {

    protected float visualAngleOffsetDeg = 0f;

    @Override
    public final ComponentType type() {
        return ComponentType.RENDER;
    }

    /** Offset visivo in gradi, sommato alla rotazione fisica dal RenderManager */
    public float getVisualAngleOffsetDeg() {
        return visualAngleOffsetDeg;
    }

    public abstract void draw(Canvas canvas, ScreenParams st, RectF dst);
}

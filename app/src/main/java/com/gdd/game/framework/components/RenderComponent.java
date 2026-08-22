package com.gdd.game.framework.components;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.gdd.game.framework.ScreenParams;

public abstract class RenderComponent extends Component {


    @Override
    public final ComponentType type() {
        return ComponentType.RENDER;
    }

    public abstract void draw(Canvas canvas, ScreenParams st, RectF dst);
}

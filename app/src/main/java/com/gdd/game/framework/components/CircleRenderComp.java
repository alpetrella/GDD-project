package com.gdd.game.framework.components;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.gdd.game.framework.ScreenParams;

public class CircleRenderComp extends RenderComponent {

    private final Paint paint;

    public CircleRenderComp(int color, boolean filled) {
        paint = new Paint();
        paint.setColor(color);
        paint.setStyle(filled ? Paint.Style.FILL : Paint.Style.STROKE);
        paint.setAntiAlias(true);
    }

    @Override
    public void draw(Canvas canvas, ScreenParams t, RectF dst) {

        canvas.save();
        canvas.translate(t.screenX, t.screenY);
        canvas.drawCircle(0, 0, t.radius, paint);
        canvas.restore();
    }

}

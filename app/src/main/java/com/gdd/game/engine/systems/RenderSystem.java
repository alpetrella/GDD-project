package com.gdd.game.engine.systems;

import android.graphics.Canvas;
import android.graphics.RectF;

import com.gdd.game.engine.actors.Actor;
import com.gdd.game.engine.Camera;
import com.gdd.game.engine.ScreenParams;
import com.gdd.game.engine.components.ComponentType;
import com.gdd.game.engine.actors.Transform;
import com.gdd.game.engine.components.RenderComponent;

import java.util.List;

public class RenderSystem {

    // allocate once and used for every draw
    private final ScreenParams scratchTransform = new ScreenParams();
    private final RectF scratchDst = new RectF();


    public RenderSystem() {
    }


    public void render(Canvas canvas, Camera camera, List<Actor> actors) {

        if(canvas == null || actors == null)
            return;

        int n = actors.size();
        for(int i=0; i<n; i++)  {

            Actor actor = actors.get(i);
            RenderComponent dc = (RenderComponent) actor.getComponent(ComponentType.RENDER);
            if(dc == null)
                continue;

            Transform transform = actor.transform;

            // 1. CULLING
            if (!camera.isVisible(transform.x, transform.y, transform.halfWidth, transform.halfHeight)) {
                continue;
            }

            // 2. CONVERSIONE WORLD->SCREEN
            float xPixel = camera.toPixelsX(transform.x);
            float yPixel = camera.toPixelsY(transform.y);
            float hWidthPixel = camera.toPixelsXLength(transform.halfWidth);
            float hHeightPixel = camera.toPixelsYLength(transform.halfHeight);
            float rotationDeg = (float) Math.toDegrees(transform.angle)
                    + dc.getVisualAngleOffsetDeg(); // serve +90f?

            scratchTransform.set(xPixel, yPixel, hWidthPixel, hHeightPixel,
                    rotationDeg, hWidthPixel);

            // 3. DRAW ACTOR
            dc.draw(canvas, scratchTransform, scratchDst);
        }
    }

}

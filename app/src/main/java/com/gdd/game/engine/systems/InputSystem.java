package com.gdd.game.engine.systems;

import com.badlogic.androidgames.framework.Input;
import com.gdd.game.GameWorld;
import com.gdd.game.engine.Camera;
import com.gdd.game.engine.PointerTracker;
import com.gdd.game.engine.actors.Actor;
import com.gdd.game.engine.components.ComponentType;
import com.gdd.game.engine.components.InputComponent;

/*
 * Gestisce l'input nella scena.
 * Riconosce al più 2 touch contemporaneamente.
 */
public class InputSystem {

    public GameWorld gw;

    public enum GestureState { IDLE, PENDING, PANNING, PINCH_ZOOM, DRAG }
    private GestureState state = GestureState.IDLE;
    private final Camera camera;

    public static final float PAN_THRESHOLD = 20f;
    private PointerTracker pointers = new PointerTracker();

    // ********************************
    //  Object interaction
    // ********************************

    private Actor actorTarget;
    private InputComponent inputTarget;


    /*
     * Constructor.
     */
    public InputSystem(Camera camera) {
        this.camera = camera;
    }

    // ********************************
    //  Input
    // ********************************

    public void processInput(Input.TouchEvent event) {

        if(event == null) return;

        switch (event.type) {
            case Input.TouchEvent.TOUCH_DOWN:
                handleDown(event);
                break;
            case Input.TouchEvent.TOUCH_DRAGGED:
                handleDragged(event);
                break;
            case Input.TouchEvent.TOUCH_UP:
                handleUp(event);
                break;
        }
    }

    private void handleDown(Input.TouchEvent event) {

        if(state == GestureState.IDLE) {
            pointers.addPointer(event.pointer, event.x, event.y);

            float worldX = camera.toMetersX(event.x);
            float worldY = camera.toMetersY(event.y);
            actorTarget = gw.hit(worldX, worldY);
            if(actorTarget != null) {
                inputTarget = (InputComponent) actorTarget.getComponent(ComponentType.INPUT);
            }
            state = GestureState.PENDING;
        }
        else if(state == GestureState.PENDING) {
            pointers.addPointer(event.pointer, event.x, event.y);
            state = GestureState.PINCH_ZOOM;

            camera.beginPinch(pointers.pinchMidX(), pointers.pinchMidY(), pointers.pinchDistance());
        }
        else if(state == GestureState.PANNING) {
            pointers.addPointer(event.pointer, event.x, event.y);
            state = GestureState.PINCH_ZOOM;

            camera.beginPinch(pointers.pinchMidX(), pointers.pinchMidY(), pointers.pinchDistance());
        }
    }

    private void handleDragged(Input.TouchEvent event) {

        if(!pointers.hasPointer(event.pointer))
            return;

        if (state == GestureState.PENDING) {
            // PENDING -> DRAG
            if(inputTarget != null && inputTarget.isDraggable()) {
                inputTarget.onDragStart(camera.toMetersX(event.x), camera.toMetersY(event.y));
                state = GestureState.DRAG;
            }
            // PENDING -> PANNING
            else {
                float totalDx = pointers.totalDeltaX(event.pointer, event.x);
                float totalDy = pointers.totalDeltaY(event.pointer, event.y);
                pointers.updatePointer(event.pointer, event.x, event.y);
                // passa al panning se superata una certa soglia con il dito
                if (totalDx * totalDx + totalDy * totalDy > PAN_THRESHOLD * PAN_THRESHOLD) {
                    state = GestureState.PANNING;
                }
            }
        } else if (state == GestureState.PANNING) {
            float dx = pointers.deltaX(event.pointer, event.x);
            float dy = pointers.deltaY(event.pointer, event.y);
            pointers.updatePointer(event.pointer, event.x, event.y);
            camera.pan(dx, dy);
        } else if (state == GestureState.PINCH_ZOOM) {
            pointers.updatePointer(event.pointer, event.x, event.y);
            camera.updatePinch(pointers.pinchMidX(), pointers.pinchMidY(), pointers.pinchDistance());
        } else if(state == GestureState.DRAG) {
            if(inputTarget != null) {
                float dx = pointers.deltaX(event.pointer, event.x);
                float dy = pointers.deltaY(event.pointer, event.y);
                pointers.updatePointer(event.pointer, event.x, event.y);
                inputTarget.onDrag(camera.toMetersXLength(dx), camera.toMetersYLength(dy));
            }
        }
    }

    private void handleUp(Input.TouchEvent event) {

        if(!pointers.hasPointer(event.pointer))
            return;

        if(state == GestureState.PENDING) {
            if(inputTarget != null) {
                inputTarget.onTap();
            }
            inputTarget = null;
            actorTarget = null;
            state = GestureState.IDLE;
        } else if (state == GestureState.PANNING) {
            state = GestureState.IDLE;
        } else if (state == GestureState.PINCH_ZOOM) {
            camera.endPinch();
            state = GestureState.PANNING;
        } else if (state == GestureState.DRAG) {
            inputTarget.onDragEnd(camera.toMetersX(event.x), camera.toMetersY(event.y));
            inputTarget = null;
            actorTarget = null;
            state = GestureState.IDLE;
        }

        pointers.removePointer(event.pointer);
    }

    // ********************************
    //  Getter / Setter
    // ********************************

    public GestureState getState() {
        return state;
    }

    public Camera getCamera() {
        return camera;
    }

    // ********************************
    //  Misc
    // ********************************

    public void reset() {
        if (state == GestureState.PINCH_ZOOM) {
            camera.endPinch();
        } else if (state == GestureState.DRAG) {
            inputTarget.onDragCancel();
        }
        inputTarget = null;
        actorTarget = null;
        pointers.removePointers();
        state = GestureState.IDLE;
    }
}

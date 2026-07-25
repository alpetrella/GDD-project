package com.gdd.game.engine.core;

import com.gdd.game.engine.components.Component;
import com.gdd.game.engine.components.ComponentType;

import java.util.EnumMap;
import java.util.Map;

public class Actor {

    public long id;
    public final Transform transform;
    public Shape shape;
    private Map<ComponentType, Component> components = new EnumMap<>(ComponentType.class);

    public Actor(long id) {
        this.id = id;
        transform = new Transform();
    }

    public Actor(long id, float x, float y) {
        this(id);
        transform.x = x;
        transform.y = y;
    }

    public Actor(long id, float x, float y, float angle) {
        this(id, x,y);
        transform.angle = angle;
    }


    public void addComponent(Component c) {
        c.setOwner(this);
        components.put(c.type(), c);
    }

    public Component getComponent(ComponentType type) {
        return components.get(type);
    }

    public Transform getTransform() {
        return transform;
    }

    public Shape getShape() { return shape; }

    public void setShape(Shape shape) { this.shape = shape; }
}

package com.gdd.game.framework.actors;

import static com.gdd.game.framework.actors.ActorTag.EMPTY;

import com.gdd.game.framework.components.Component;
import com.gdd.game.framework.components.ComponentType;

import java.util.EnumMap;
import java.util.Map;

public class Actor {

    public final long id;
    public ActorTag tag = EMPTY;

    public final Transform transform;
    private Map<ComponentType, Component> components = new EnumMap<>(ComponentType.class);

    /*
     * Constructor.
     */
    public Actor(long id) {
        this.id = id;
        transform = new Transform();
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
}

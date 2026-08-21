package com.gdd.game.framework.components;

import com.gdd.game.framework.actors.Actor;

public abstract class Component {

    public Actor owner;

    public void setOwner ( Actor owner ) { this . owner = owner ; }
    public Actor getOwner () { return owner ; }

    public abstract ComponentType type();
}

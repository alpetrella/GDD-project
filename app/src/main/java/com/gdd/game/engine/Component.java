package com.gdd.game.engine;

public abstract class Component {

    public Actor owner;

    public void setOwner ( Actor owner ) { this . owner = owner ; }
    public Actor getOwner () { return owner ; }

    public abstract ComponentType type();
}

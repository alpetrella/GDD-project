package com.gdd.game.engine.components;

import com.gdd.game.engine.actors.Faction;
import com.gdd.game.engine.actors.Species;

public class AliveComponent extends Component {

    public Species species; // ANT, WASP, MIDGE
    public Faction faction; // PLAYER, NEUTRAL, HOSTILE
    public float hp;
    public float damage; // attack

    public AliveComponent(Species species, Faction faction) {
        this.species = species;
        this.faction = faction;
    }

    @Override
    public ComponentType type() {
        return ComponentType.ALIVE;
    }
}
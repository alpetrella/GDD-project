package com.gdd.game.framework.components;

import com.gdd.game.framework.actors.Faction;
import com.gdd.game.framework.actors.Species;

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
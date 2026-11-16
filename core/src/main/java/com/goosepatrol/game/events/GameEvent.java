package com.goosepatrol.game.events;

import com.goosepatrol.game.ecs.GameWorld;

/**Hidden event is invisible until the player triggers it, then it does something once.*/
public abstract class GameEvent {

    private final String name;
    private final boolean helpful;

    protected GameEvent(String name, boolean helpful) {
        this.name = name;
        this.helpful = helpful;
    }

    /**What happens when the player triggers this event.
     *x and y are where the player is when it happens.
     */
    public abstract void apply(GameWorld world, float x, float y);

    public String getName() {
        return name;
    }

    /**true for events that help the player, false for ones that make it harder */
    public boolean isHelpful() {
        return helpful;
    }
}
package com.goosepatrol.game.components;

import com.goosepatrol.game.ecs.Component;
import com.goosepatrol.game.events.GameEvent;

/**Data for a hidden event placed on the map. Any entity with this component is a hidden event.
 * It has no sprite, so it stays invisible until EventTriggerSystem triggers it.
 */
public class HiddenEventComponent implements Component {

    /**What happens when event is triggered.*/
    private final GameEvent event;

    /**How close the player must get to trigger it.*/
    private final float triggerRadius;

    /**How many seconds into the game before it can be triggered. */
    private final float activeAfter;

    /**true once it has happened so it only happens once. */
    private boolean triggered = false;

    public HiddenEventComponent(GameEvent event, float triggerRadius, float activeAfter) {
        this.event = event;
        this.triggerRadius = triggerRadius;
        this.activeAfter = activeAfter;
    }

    public GameEvent getEvent() {
        return event;
    }

    public float getTriggerRadius() {
        return triggerRadius;
    }

    public float getActiveAfter() {
        return activeAfter;
    }

    public boolean isTriggered() {
        return triggered;
    }

    /**Called once the event has occured.*/
    public void markTriggered() {
        triggered = true;
    }
}
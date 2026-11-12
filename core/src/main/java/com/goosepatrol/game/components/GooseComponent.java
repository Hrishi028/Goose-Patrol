package com.goosepatrol.game.components;

import com.goosepatrol.game.ecs.Component;

/**Data (characteristics) that only the geese have. Any entity with this component (characteristics) is a goose.
 * GooseBehaviourSystem reads it every frame to decide what the goose does.
 */
public class GooseComponent implements Component {

    private GooseState state = GooseState.CHASING;

    /**Hunger level of the goose. 0 = not hungry and will leave. */
    private int hunger;

    /**Seconds left before a distracted goose goes back to what it was doing. */
    private float distractTimer = 0f;

    /** How fast this goose moves, in pixels per second. */
    private float speed;

    public GooseComponent(int hunger, float speed) {
        this.hunger = hunger;
        this.speed = speed;
    }

    public GooseState getState() {
        return state;
    }

    public void setState(GooseState state) {
        this.state = state;
    }

    public int getHunger() {
        return hunger;
    }

    public boolean isFull() {
        return hunger <= 0;
    }

    public float getDistractTimer() {
        return distractTimer;
    }

    public float getSpeed() {
        return speed;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    /** Distract the goose for a number of seconds. This is used by items and events.
     * A goose that is already distracted/leaving will ignore this.
     */
    public void distract(float seconds) {
        if (state == GooseState.LEAVING) {
            return;
        }
        state = GooseState.DISTRACTED;
        distractTimer = Math.max(distractTimer, seconds);
    }

    /** Feeding the goose. Feeding lLowers its hunger but never below 0. */
    public void feed(int amount) {
        hunger = Math.max(0, hunger - amount);
    }

    /** Counts down the distraction timer by the time since the last frame. */
    public void tickDistraction(float deltaTime) {
        distractTimer = Math.max(0f, distractTimer - deltaTime);
    }
}

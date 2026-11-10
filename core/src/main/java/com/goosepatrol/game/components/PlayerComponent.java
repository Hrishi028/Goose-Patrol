package com.goosepatrol.game.components;

import com.goosepatrol.game.ecs.Component;

public class PlayerComponent implements Component {

    public enum Direction {
        FRONT,
        LEFT,
        RIGHT,
        BACK
    }

    private Direction direction = Direction.FRONT;
    private float animationTime = 0f;
    private boolean moving = false;

    public Direction getDirection() {
        return direction;
    }

    public void setDirection(Direction direction) {
        this.direction = direction;
    }

    public float getAnimationTime() {
        return animationTime;
    }

    public void updateAnimation(float deltaTime) {
        animationTime += deltaTime;
    }

    public void resetAnimation() {
        animationTime = 0f;
    }

    public boolean isMoving() {
        return moving;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }
}

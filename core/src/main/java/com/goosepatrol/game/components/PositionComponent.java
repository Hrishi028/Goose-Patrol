package com.goosepatrol.game.components;

import com.goosepatrol.game.ecs.Component;

public class PositionComponent implements Component {

    private float x;
    private float y;

    public PositionComponent(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setPosition(float x, float y) {
        this.x = x;
        this.y = y;
    }
}

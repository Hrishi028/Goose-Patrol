package com.goosepatrol.game.components;

import com.goosepatrol.game.ecs.Component;

public class VelocityComponent implements Component {

    private float velocityX;
    private float velocityY;

    public VelocityComponent(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }

    public float getVelocityX() {
        return velocityX;
    }

    public float getVelocityY() {
        return velocityY;
    }

    public void setVelocity(float velocityX, float velocityY) {
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
}

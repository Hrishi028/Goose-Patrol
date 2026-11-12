package com.goosepatrol.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;

/** Builds goose entities. */  
public class GooseFactory {

    // Geese should be a bit slower than the player (220) so the player can get away.
    private static final float MIN_SPEED = 100f;
    private static final float MAX_SPEED = 135f;

    private static final int MIN_HUNGER = 2;
    private static final int MAX_HUNGER = 4;

    public static Entity createGoose(EntityFactory entityFactory, float x, float y) {

        Entity goose = entityFactory.createEntity();

        int hunger = MathUtils.random(MIN_HUNGER, MAX_HUNGER);
        float speed = MathUtils.random(MIN_SPEED, MAX_SPEED);

        goose.addComponent(new GooseComponent(hunger, speed));
        goose.addComponent(new PositionComponent(x, y));
        goose.addComponent(new VelocityComponent(0, 0));

        return goose;
    }
}
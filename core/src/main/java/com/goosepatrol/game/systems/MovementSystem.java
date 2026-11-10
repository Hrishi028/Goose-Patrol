package com.goosepatrol.game.systems;

import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class MovementSystem implements GameSystem {

    @Override
    public void update(GameWorld world, float deltaTime) {

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(PositionComponent.class)
                || !entity.hasComponent(VelocityComponent.class)) {
                continue;
            }

            PositionComponent position =
                entity.getComponent(PositionComponent.class);

            VelocityComponent velocity =
                entity.getComponent(VelocityComponent.class);

            float newX = position.getX()
                + velocity.getVelocityX() * deltaTime;

            float newY = position.getY()
                + velocity.getVelocityY() * deltaTime;

            position.setPosition(newX, newY);
        }
    }
}

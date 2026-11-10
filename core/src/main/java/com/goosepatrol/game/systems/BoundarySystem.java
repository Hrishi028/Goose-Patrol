package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class BoundarySystem implements GameSystem {

    private static final float PLAYER_HALF_WIDTH = 24f;
    private static final float PLAYER_HALF_HEIGHT = 34f;

    @Override
    public void update(GameWorld world, float deltaTime) {

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(PlayerComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            PositionComponent position =
                entity.getComponent(PositionComponent.class);

            float x = position.getX();
            float y = position.getY();

            x = Math.max(
                PLAYER_HALF_WIDTH,
                Math.min(x, screenWidth - PLAYER_HALF_WIDTH)
            );

            y = Math.max(
                PLAYER_HALF_HEIGHT,
                Math.min(y, screenHeight - PLAYER_HALF_HEIGHT)
            );

            position.setPosition(x, y);
        }
    }
}

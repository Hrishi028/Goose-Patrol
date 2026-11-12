
package com.goosepatrol.game.systems;

import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class BoundarySystem implements GameSystem {

    private static final float PLAYER_HALF_WIDTH = 24f;
    private static final float PLAYER_HALF_HEIGHT = 34f;

    private final GameViewport gameViewport;

    public BoundarySystem(GameViewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        float worldWidth = gameViewport.getWorldWidth();
        float worldHeight = gameViewport.getWorldHeight();

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(PlayerComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            PositionComponent position =
                entity.getComponent(PositionComponent.class);

            float x = position.getX();
            float y = position.getY();

            // Keep player inside horizontal boundaries.
            x = Math.max(
                PLAYER_HALF_WIDTH,
                Math.min(x, worldWidth - PLAYER_HALF_WIDTH)
            );

            // Keep player inside vertical boundaries.
            y = Math.max(
                PLAYER_HALF_HEIGHT,
                Math.min(y, worldHeight - PLAYER_HALF_HEIGHT)
            );

            position.setPosition(x, y);
        }
    }
}

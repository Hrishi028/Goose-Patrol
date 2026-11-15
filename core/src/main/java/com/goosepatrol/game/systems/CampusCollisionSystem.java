
package com.goosepatrol.game.systems;

import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

import java.util.HashMap;
import java.util.Map;

public class CampusCollisionSystem implements GameSystem {

    private static final float PLAYER_RADIUS = 24f;

    private final GameViewport gameViewport;

    // Remember the player's last safe position.
    private final Map<Integer, float[]> safePositions =
        new HashMap<>();

    public CampusCollisionSystem(GameViewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        float w = gameViewport.getWorldWidth();
        float h = gameViewport.getWorldHeight();

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(PlayerComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            PositionComponent position =
                entity.getComponent(PositionComponent.class);

            int id = entity.getId();

            float currentX = position.getX();
            float currentY = position.getY();

            float[] previous = safePositions.get(id);

            if (previous == null) {
                previous = new float[]{currentX, currentY};
                safePositions.put(id, previous);
            }

            float safeX = previous[0];
            float safeY = previous[1];

            // Try moving horizontally.
            if (!isBlocked(currentX, safeY, w, h)) {
                safeX = currentX;
            }

            // Try moving vertically.
            if (!isBlocked(safeX, currentY, w, h)) {
                safeY = currentY;
            }

            position.setPosition(safeX, safeY);

            safePositions.put(
                id,
                new float[]{safeX, safeY}
            );
        }
    }

    private boolean isBlocked(
        float x, float y, float w, float h) {

        // =====================================
        // BUILDING COLLISIONS
        // Match BuildingRenderSystem positions.
        // =====================================

        // Upper-left building
        if (insideRectangle(
            x, y,
            w * 0.025f,
            h * 0.69f,
            w * 0.25f,
            h * 0.22f)) {
            return true;
        }

        // Upper-right building
        if (insideRectangle(
            x, y,
            w * 0.735f,
            h * 0.70f,
            w * 0.24f,
            h * 0.22f)) {
            return true;
        }

        // Lower-left building
        if (insideRectangle(
            x, y,
            w * 0.025f,
            h * 0.055f,
            w * 0.25f,
            h * 0.22f)) {
            return true;
        }

        // Lower-right building
        if (insideRectangle(
            x, y,
            w * 0.725f,
            h * 0.055f,
            w * 0.25f,
            h * 0.22f)) {
            return true;
        }

        // =====================================
        // LAKE COLLISION
        // Match the water ellipse in MapRenderSystem.
        // =====================================

        float lakeX = w * 0.60f;
        float lakeY = h * 0.58f;

        float lakeWidth = w * 0.23f;
        float lakeHeight = h * 0.17f;

        float lakeCenterX = lakeX + lakeWidth / 2f;
        float lakeCenterY = lakeY + lakeHeight / 2f;

        float radiusX = lakeWidth / 2f + PLAYER_RADIUS;
        float radiusY = lakeHeight / 2f + PLAYER_RADIUS;

        float dx = (x - lakeCenterX) / radiusX;
        float dy = (y - lakeCenterY) / radiusY;

        return dx * dx + dy * dy < 1f;
    }

    private boolean insideRectangle(
        float x, float y,
        float rectangleX, float rectangleY,
        float rectangleWidth, float rectangleHeight) {

        return x + PLAYER_RADIUS > rectangleX
            && x - PLAYER_RADIUS < rectangleX + rectangleWidth
            && y + PLAYER_RADIUS > rectangleY
            && y - PLAYER_RADIUS < rectangleY + rectangleHeight;
    }

    // Call this whenever the player is repositioned.
    public void resetPlayerPosition(int entityId) {
        safePositions.remove(entityId);
    }
}


package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

import java.util.HashMap;
import java.util.Map;

public class CampusCollisionSystem implements GameSystem {

    private static final float PLAYER_RADIUS = 24f;

    // Last position that was not inside an obstacle.
    private final Map<Integer, float[]> safePositions = new HashMap<>();

    @Override
    public void update(GameWorld world, float deltaTime) {

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(PlayerComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            PositionComponent position =
                entity.getComponent(PositionComponent.class);

            int id = entity.getId();

            float[] previous = safePositions.get(id);

            float currentX = position.getX();
            float currentY = position.getY();

            // Remember the starting position if this is the first update.
            if (previous == null) {
                previous = new float[] {currentX, currentY};
                safePositions.put(id, previous);
            }

            float safeX = previous[0];
            float safeY = previous[1];

            // Try horizontal movement first.
            if (!isBlocked(currentX, safeY, w, h)) {
                safeX = currentX;
            }

            // Then try vertical movement.
            if (!isBlocked(safeX, currentY, w, h)) {
                safeY = currentY;
            }

            position.setPosition(safeX, safeY);

            safePositions.put(id, new float[] {safeX, safeY});
        }
    }

    private boolean isBlocked(
        float x, float y, float screenWidth, float screenHeight) {

        // Four building collision rectangles.
        // These match the building positions in BuildingRenderSystem.

        if (insideRectangle(
            x, y,
            screenWidth * 0.05f,
            screenHeight * 0.68f,
            screenWidth * 0.22f,
            screenHeight * 0.22f)) {
            return true;
        }

        if (insideRectangle(
            x, y,
            screenWidth * 0.73f,
            screenHeight * 0.68f,
            screenWidth * 0.22f,
            screenHeight * 0.22f)) {
            return true;
        }

        if (insideRectangle(
            x, y,
            screenWidth * 0.05f,
            screenHeight * 0.08f,
            screenWidth * 0.22f,
            screenHeight * 0.22f)) {
            return true;
        }

        if (insideRectangle(
            x, y,
            screenWidth * 0.73f,
            screenHeight * 0.08f,
            screenWidth * 0.22f,
            screenHeight * 0.22f)) {
            return true;
        }

        // Lake collision: elliptical shape.
        float lakeCenterX = screenWidth * 0.60f
            + screenWidth * 0.23f / 2f;

        float lakeCenterY = screenHeight * 0.58f
            + screenHeight * 0.17f / 2f;

        float radiusX = screenWidth * 0.23f / 2f
            + PLAYER_RADIUS;

        float radiusY = screenHeight * 0.17f / 2f
            + PLAYER_RADIUS;

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
}


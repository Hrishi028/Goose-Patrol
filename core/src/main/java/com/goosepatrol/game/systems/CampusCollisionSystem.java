
package com.goosepatrol.game.systems;

import com.goosepatrol.game.world.CampusMap;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

import java.util.HashMap;
import java.util.Map;

public class CampusCollisionSystem implements GameSystem {

    private static final float PLAYER_RADIUS = 24f;
    private final CampusMap campusMap;


    // Remember the player's last safe position.
    private final Map<Integer, float[]> safePositions =
        new HashMap<>();

    public CampusCollisionSystem(CampusMap campusMap) {
        this.campusMap = campusMap;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

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
            if (!isBlocked(currentX, safeY)) {
                safeX = currentX;
            }

            // Try moving vertically.
            if (!isBlocked(safeX, currentY)) {
                safeY = currentY;
            }

            position.setPosition(safeX, safeY);

            safePositions.put(
                id,
                new float[]{safeX, safeY}
            );
        }
    }

    private boolean isBlocked(float x , float y){
        return campusMap.isBlocked(x, y, PLAYER_RADIUS);
    }


    // Call this whenever the player is repositioned.
    public void resetPlayerPosition(int entityId) {
        safePositions.remove(entityId);
    }
}

package com.goosepatrol.game.systems;

import com.badlogic.gdx.math.MathUtils;
import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.entities.GooseFactory;
import com.goosepatrol.game.GameViewport;

import java.util.ArrayList;
import java.util.List;

/**Creates new geese over time. The gap between spawns gets shorter as the game goes on.
 *The more time that has passed, the more often new geese arrive.
 *Longboi, the boss goose, also arrives once at a random time.
 */
public class GooseSpawnSystem implements GameSystem {

    private static final float START_INTERVAL = 6f;    //seconds between geese at the start
    private static final float MIN_INTERVAL = 2f;    //fastest it will ever get
    private static final float SPEED_UP_RATE = 0.015f;    //how quickly spawning speeds up
    private static final float FIRST_SPAWN_DELAY = 1f;    //first goose arrives after 1 second
    private static final int MAX_GEESE = 20;    //stops the screen filling up completely

    private final EntityFactory entityFactory;

    private float timeUntilNextSpawn = FIRST_SPAWN_DELAY;
    private float elapsed = 0f;

    //Longboi arrives once at a random time between these two (in seconds).
    private static final float LONGBOI_EARLIEST = 60f;
    private static final float LONGBOI_LATEST = 120f;

    private float longboiSpawnTime = MathUtils.random(LONGBOI_EARLIEST, LONGBOI_LATEST);
    private boolean longboiSpawned = false;

    public GooseSpawnSystem(EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        //deltaTime is 0 while paused or after the game ends so nothing spawns.
        if (deltaTime <= 0f) {
            return;
        }

        elapsed += deltaTime;
        timeUntilNextSpawn -= deltaTime;

        //Longboi turns up once at his random time.
        if (!longboiSpawned && elapsed >= longboiSpawnTime) {
            float[] point = randomSpawnPoint();
            world.addEntity(GooseFactory.createLongboi(entityFactory, point[0], point[1]));
            longboiSpawned = true;
        }

        if (timeUntilNextSpawn > 0f) {
            return;
        }

        if (countGeese(world) < MAX_GEESE) {
            float[] point = randomSpawnPoint();
            world.addEntity(GooseFactory.createGoose(entityFactory, point[0], point[1]));
        }

        timeUntilNextSpawn = Math.max(MIN_INTERVAL, START_INTERVAL - elapsed * SPEED_UP_RATE);
    }

    /**Removes every goose and starts the spawn timer again.*/
    public void reset(GameWorld world) {
        List<Entity> geese = new ArrayList<>();
        for (Entity entity : world.getEntities()) {
            if (entity.hasComponent(GooseComponent.class)) {
                geese.add(entity);
            }
        }
        for (Entity goose : geese) {
            world.removeEntity(goose);
        }
        elapsed = 0f;
        timeUntilNextSpawn = FIRST_SPAWN_DELAY;
        longboiSpawned = false;
        longboiSpawnTime = MathUtils.random(LONGBOI_EARLIEST, LONGBOI_LATEST);
    }

    private int countGeese(GameWorld world) {
        int count = 0;
        for (Entity entity : world.getEntities()) {
            if (entity.hasComponent(GooseComponent.class)) {
                count++;
            }
        }
        return count;
    }

    /**
     * Geese arrive from the lake or from the middle of one of the screen edges.
     * Positions are worked out from the game world size.
     */
    private float[] randomSpawnPoint() {
        float w = GameViewport.WORLD_WIDTH;
        float h = GameViewport.WORLD_HEIGHT;

        float[][] points = {
            {w * 0.715f, h * 0.665f},    //the lake (matches CampusCollisionSystem)
            {0f, h * 0.5f},    //left edge
            {w, h * 0.5f},    //right edge
            {w * 0.5f, 0f},    //bottom edge
            {w * 0.5f, h}    //top edge
        };

        return points[MathUtils.random(points.length - 1)];
    }
}
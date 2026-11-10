package com.goosepatrol.game.ecs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GameWorld {

    private final List<Entity> entities;
    private final List<GameSystem> systems;

    public GameWorld() {
        entities = new ArrayList<>();
        systems = new ArrayList<>();
    }

    public void addEntity(Entity entity) {
        entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    public List<Entity> getEntities() {
        return Collections.unmodifiableList(entities);
    }

    public void addSystem(GameSystem system) {
        systems.add(system);
    }

    public void update(float deltaTime) {
        for (GameSystem system : systems) {
            system.update(this, deltaTime);
        }
    }
}

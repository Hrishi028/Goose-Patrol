package com.goosepatrol.game.ecs;
import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.PositionComponent;
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
    public List<Entity> geeseNear(float x, float y, float radius) {
        List<Entity> result = new ArrayList<>();
        for (Entity entity : entities) {
            if (!entity.hasComponent(GooseComponent.class) || !entity.hasComponent(PositionComponent.class)) continue;

            PositionComponent p = entity.getComponent(PositionComponent.class);
            float dx = p.getX() - x;
            float dy = p.getY() - y;
            if (dx * dx + dy * dy <= radius * radius) result.add(entity);
        } 
        return result;
    }

    public void removeEntitiesWith(Class <? extends Component> type){
        entities.removeIf(entity -> entity.hasComponent(type));
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

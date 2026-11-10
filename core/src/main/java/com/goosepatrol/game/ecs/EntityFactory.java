package com.goosepatrol.game.ecs;

public class EntityFactory {

    private int nextId = 1;

    public Entity createEntity() {
        Entity entity = new Entity(nextId);
        nextId++;
        return entity;
    }
}

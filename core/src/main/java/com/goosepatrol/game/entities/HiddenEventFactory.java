package com.goosepatrol.game.entities;

import com.goosepatrol.game.components.HiddenEventComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;
import com.goosepatrol.game.events.GameEvent;

/**Builds hidden event entities(invisible spots on the map that trigger an event).*/
public class HiddenEventFactory {

    public static Entity createHiddenEvent(EntityFactory entityFactory, GameEvent event,
                                           float x, float y,
                                           float triggerRadius, float activeAfter) {

        Entity hiddenEvent = entityFactory.createEntity();

        hiddenEvent.addComponent(new PositionComponent(x, y));
        hiddenEvent.addComponent(new HiddenEventComponent(event, triggerRadius, activeAfter));

        return hiddenEvent;
    }
}
package com.goosepatrol.game.events;

import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameWorld;

/**a bread van spills its bread, feeds and distracts nearby geese. */
public class BreadVanEvent extends GameEvent {

    private static final float RADIUS = 800f;    //how far the spilled bread reaches
    private static final float DISTRACT_SECONDS = 3f;    //how long the geese are busy eating
    private static final int FEED_AMOUNT = 4;    //how much hunger it takes away

    public BreadVanEvent() {
        super("Bread Van", true);
    }

    @Override
    public void apply(GameWorld world, float x, float y) {

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(GooseComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            PositionComponent position = entity.getComponent(PositionComponent.class);
            float dx = position.getX() - x;
            float dy = position.getY() - y;

            //when geese are too far away to reach the bread.
            if (dx * dx + dy * dy > RADIUS * RADIUS) {
                continue;
            }

            GooseComponent goose = entity.getComponent(GooseComponent.class);
            goose.feed(FEED_AMOUNT);
            goose.distract(DISTRACT_SECONDS);
        }
    }
}
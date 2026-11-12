package com.goosepatrol.game.items;

import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameWorld;

/** Thrown to nearby geese: it feeds them and keeps them busy for a few seconds */
public class Bread extends Item 
{

    private static final int USES = 5;
    private static final float RADIUS = 160f;          // how far the bread reaches
    private static final int FEED_AMOUNT = 2;          // how much hunger it takes away
    private static final float DISTRACT_SECONDS = 3f;  // how long the geese stop chasing

    public Bread() 
    {
        super("Bread", USES);
    }

    @Override
    protected void apply(GameWorld world, float x, float y) 
    {

        for (Entity entity : world.getEntities())
            {

            if (!entity.hasComponent(GooseComponent.class)
                || !entity.hasComponent(PositionComponent.class)) 
            {
                continue;
            }

            PositionComponent position = entity.getComponent(PositionComponent.class);
            float dx = position.getX() - x;
            float dy = position.getY() - y;

            // Too far away to reach.
            if (dx * dx + dy * dy > RADIUS * RADIUS) 
            {
                continue;
            }

            GooseComponent goose = entity.getComponent(GooseComponent.class);
            goose.feed(FEED_AMOUNT);
            goose.distract(DISTRACT_SECONDS);
        }
    }
}
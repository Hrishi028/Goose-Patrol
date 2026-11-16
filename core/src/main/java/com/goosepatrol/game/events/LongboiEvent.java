package com.goosepatrol.game.events;

import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.GooseState;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameWorld;

/**Unhelpful event: the player meets Longboi.
 * Every goose near the player stops what it is doing and joins the chase.
 */
public class LongboiEvent extends GameEvent {

    private static final float RALLY_RADIUS = 500f;    //how far from the player geese join the chase

    public LongboiEvent() {
        super("Longboi", false);
    }

    @Override
    public void apply(GameWorld world, float x, float y) {

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(GooseComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            GooseComponent goose = entity.getComponent(GooseComponent.class);

            //Geese that are already full and walking away are left alone.
            if (goose.getState() == GooseState.LEAVING) {
                continue;
            }

            PositionComponent position = entity.getComponent(PositionComponent.class);
            float dx = position.getX() - x;
            float dy = position.getY() - y;

            //Too far away to join the chase.
            if (dx * dx + dy * dy > RALLY_RADIUS * RALLY_RADIUS) {
                continue;
            }

            goose.setState(GooseState.CHASING);
        }
    }
}
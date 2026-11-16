package com.goosepatrol.game.systems;

import com.goosepatrol.game.components.HiddenEventComponent;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.events.GameEvent;
import com.goosepatrol.game.session.GameSession;

import java.util.ArrayList;
import java.util.List;

/**Checks every frame if the player has walked into a hidden event.
 * When they do, it executes the event once and tells GameSession, which updates the counter.
 */
public class EventTriggerSystem implements GameSystem {

    private final GameSession session;

    /**how many seconds the game has been running.*/
    private float elapsed = 0f;

    public EventTriggerSystem(GameSession session) {
        this.session = session;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        //Paused or game over: events can't be triggered and time doesn't count.
        if (deltaTime <= 0f) {
            return;
        }

        elapsed += deltaTime;

        PositionComponent player = findPlayerPosition(world);
        if (player == null) {
            return;
        }

        //Finds every event the player has just walked into.
        List<HiddenEventComponent> found = new ArrayList<>();

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(HiddenEventComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            HiddenEventComponent hidden = entity.getComponent(HiddenEventComponent.class);

            //already happened, or not active yet
            if (hidden.isTriggered() || elapsed < hidden.getActiveAfter()) {
                continue;
            }

            PositionComponent position = entity.getComponent(PositionComponent.class);
            float dx = position.getX() - player.getX();
            float dy = position.getY() - player.getY();

            if (dx * dx + dy * dy <= hidden.getTriggerRadius() * hidden.getTriggerRadius()) {
                found.add(hidden);
            }
        }

        //Trigger them after the loop, in case an event changes the list of entities.
        for (HiddenEventComponent hidden : found) {
            hidden.markTriggered();

            GameEvent event = hidden.getEvent();
            event.apply(world, player.getX(), player.getY());

            session.eventTriggered(event.getName());    
        }
    }


    private PositionComponent findPlayerPosition(GameWorld world) {
        for (Entity entity : world.getEntities()) {
            if (entity.hasComponent(PlayerComponent.class)
                && entity.hasComponent(PositionComponent.class)) {
                return entity.getComponent(PositionComponent.class);
            }
        }
        return null;
    }
}
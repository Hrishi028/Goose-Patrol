package com.goosepatrol.game.systems;

import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.GooseState;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.session.GameSession;

/**Raises the player's stress bar while chasing geese are right next to them.
 * The more geese on the player, the faster it rises.
 */
public class StressSystem implements GameSystem {

    private static final float ATTACK_RANGE = 40f;    //how close counts as "on you"
    private static final float STRESS_PER_GOOSE_PER_SECOND = 8f;

    private final GameSession session;

    public StressSystem(GameSession session) {
        this.session = session;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        //When paused or game over: stress doesn't change.
        if (deltaTime <= 0f) {
            return;
        }

        PositionComponent player = null;
        for (Entity entity : world.getEntities()) {
            if (entity.hasComponent(PlayerComponent.class)
                && entity.hasComponent(PositionComponent.class)) {
                player = entity.getComponent(PositionComponent.class);
                break;
            }
        }
        if (player == null) {
            return;
        }

        int geeseOnPlayer = 0;

        for (Entity entity : world.getEntities()) {
            if (!entity.hasComponent(GooseComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            GooseComponent goose = entity.getComponent(GooseComponent.class);
            if (goose.getState() != GooseState.CHASING) {
                continue;    //distracted or leaving geese won't bother the player
            }

            PositionComponent position = entity.getComponent(PositionComponent.class);
            float dx = position.getX() - player.getX();
            float dy = position.getY() - player.getY();

            if (dx * dx + dy * dy <= ATTACK_RANGE * ATTACK_RANGE) {
                geeseOnPlayer++;
            }
        }

        if (geeseOnPlayer > 0) {
            session.getStress().add(geeseOnPlayer * STRESS_PER_GOOSE_PER_SECOND * deltaTime);
        }
    }
}
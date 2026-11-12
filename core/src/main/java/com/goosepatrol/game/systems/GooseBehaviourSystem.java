package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.GooseState;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.session.GameSession;

import java.util.ArrayList;
import java.util.List;

/**
 * Decides what every goose does each frame.
 * CHASING -> (item/event) -> DISTRACTED -> back to CHASING or LEAVING if full.
 * Only sets each goose's speed. MovementSystem does the moving.
 */
public class GooseBehaviourSystem implements GameSystem {

    private static final float WOBBLE = 0.35f;    //random wiggle while chasing
    private static final float STOP_DISTANCE = 28f;    //stop when right next to the player
    private static final float LEAVE_SPEED_FACTOR = 1.3f;
    private static final float OFF_SCREEN_MARGIN = 40f;    //how far past the edge counts as gone
    private static final float EDGE_PADDING = 16f;    //keeps non-leaving geese on screen

    private final GameSession session;
    private final Vector2 direction = new Vector2();

    public GooseBehaviourSystem(GameSession session) {
        this.session = session;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        //Paused or game over: do nothing, so the geese stay where they are.
        if (deltaTime <= 0f) {
            return;
        }

        PositionComponent player = findPlayerPosition(world);
        if (player == null) {
            return;
        }

        float w = Gdx.graphics.getWidth();
        float h = Gdx.graphics.getHeight();

        //Geese can't be removed while we loop over the list.
        List<Entity> gone = new ArrayList<>();

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(GooseComponent.class)
                || !entity.hasComponent(PositionComponent.class)
                || !entity.hasComponent(VelocityComponent.class)) {
                continue;
            }

            GooseComponent goose = entity.getComponent(GooseComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);
            VelocityComponent velocity = entity.getComponent(VelocityComponent.class);

            switch (goose.getState()) {

                case CHASING:
                    chase(goose, position, velocity, player);
                    keepOnScreen(position, w, h);
                    break;

                case DISTRACTED:
                    velocity.setVelocity(0, 0);
                    goose.tickDistraction(deltaTime);
                    if (goose.getDistractTimer() <= 0f) {
                        goose.setState(goose.isFull() ? GooseState.LEAVING : GooseState.CHASING);
                    }
                    keepOnScreen(position, w, h);
                    break;

                case LEAVING:
                    leave(goose, position, velocity, player);
                    if (isOffScreen(position, w, h)) {
                        gone.add(entity);
                    }
                    break;

                case SETTLED:
                    velocity.setVelocity(0, 0);
                    break;
            }
        }

        for (Entity entity : gone) {
            world.removeEntity(entity);
            session.gooseDispersed();    //updates the HUD count and checks for a win
        }
    }

    private void chase(GooseComponent goose, PositionComponent position,
                       VelocityComponent velocity, PositionComponent player) {

        direction.set(player.getX() - position.getX(), player.getY() - position.getY());

        //Right next to the player: stop, so the goose doesn't jitter on top of them.
        if (direction.len() < STOP_DISTANCE) {
            velocity.setVelocity(0, 0);
            return;
        }

        direction.nor(); //keep only the direction (length 1)
        direction.add(MathUtils.random(-WOBBLE, WOBBLE), MathUtils.random(-WOBBLE, WOBBLE)).nor();

        velocity.setVelocity(direction.x * goose.getSpeed(), direction.y * goose.getSpeed());
    }

    private void leave(GooseComponent goose, PositionComponent position,
                       VelocityComponent velocity, PositionComponent player) {

        //Walk directly away from the player.
        direction.set(position.getX() - player.getX(), position.getY() - player.getY());

        if (direction.isZero()) {
            direction.set(1, 0);    //standing exactly on the player: just pick a way out
        }

        direction.nor();
        float speed = goose.getSpeed() * LEAVE_SPEED_FACTOR;
        velocity.setVelocity(direction.x * speed, direction.y * speed);
    }

    private boolean isOffScreen(PositionComponent position, float w, float h) {
        return position.getX() < -OFF_SCREEN_MARGIN
            || position.getX() > w + OFF_SCREEN_MARGIN
            || position.getY() < -OFF_SCREEN_MARGIN
            || position.getY() > h + OFF_SCREEN_MARGIN;
    }

    private void keepOnScreen(PositionComponent position, float w, float h) {
        float x = MathUtils.clamp(position.getX(), EDGE_PADDING, w - EDGE_PADDING);
        float y = MathUtils.clamp(position.getY(), EDGE_PADDING, h - EDGE_PADDING);
        position.setPosition(x, y);
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
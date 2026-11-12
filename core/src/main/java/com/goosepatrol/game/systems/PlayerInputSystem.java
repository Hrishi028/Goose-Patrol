package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.session.StressMeter;

public class PlayerInputSystem implements GameSystem {

    private static final float PLAYER_SPEED = 220f;

    @Override
    public void update(GameWorld world, float deltaTime) {

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(PlayerComponent.class)
                || !entity.hasComponent(VelocityComponent.class)) {
                continue;
            }

            PlayerComponent player =
                entity.getComponent(PlayerComponent.class);

            VelocityComponent velocity =
                entity.getComponent(VelocityComponent.class);

            float velocityX = 0;
            float velocityY = 0;

            if (Gdx.input.isKeyPressed(Input.Keys.W)
                || Gdx.input.isKeyPressed(Input.Keys.UP)) {
                velocityY++;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.S)
                || Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
                velocityY--;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.A)
                || Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
                velocityX--;
            }

            if (Gdx.input.isKeyPressed(Input.Keys.D)
                || Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                velocityX++;
            }

            float length = (float) Math.sqrt(
                velocityX * velocityX + velocityY * velocityY
            );

            boolean moving = length > 0;

            if (moving) {

                velocityX = velocityX / length * PLAYER_SPEED;
                velocityY = velocityY / length * PLAYER_SPEED;

                // Set the direction for the walking animation.
                if (Math.abs(velocityX) > Math.abs(velocityY)) {

                    player.setDirection(
                        velocityX < 0
                            ? PlayerComponent.Direction.LEFT
                            : PlayerComponent.Direction.RIGHT
                    );

                } else {

                    player.setDirection(
                        velocityY > 0
                            ? PlayerComponent.Direction.BACK
                            : PlayerComponent.Direction.FRONT
                    );
                }
            }

            player.setMoving(moving);

            if (!moving) {
                player.resetAnimation();
            }

            velocity.setVelocity(velocityX, velocityY);
        }
    }
}

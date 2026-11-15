
package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class PlayerRenderSystem implements GameSystem {

    private final SpriteBatch batch;
    private final Texture playerTexture;
    private final TextureRegion[][] frames;
    private final GameViewport gameViewport;

    private float animationTime = 0;

    private static final float DRAW_WIDTH = 96f;
    private static final float DRAW_HEIGHT = 96f;

    public PlayerRenderSystem(GameViewport gameViewport) {

        this.gameViewport = gameViewport;
        batch = new SpriteBatch();

        playerTexture = new Texture(
            Gdx.files.internal("player/player.png")
        );

        playerTexture.setFilter(
            Texture.TextureFilter.Nearest,
            Texture.TextureFilter.Nearest
        );

        int frameWidth = 256;
        int frameHeight = 256;

        TextureRegion[][] allFrames = TextureRegion.split(
            playerTexture,
            frameWidth,
            frameHeight
        );

        // Character animations occupy columns 1–4.
        frames = new TextureRegion[4][4];

        for (int row = 0; row < 4; row++) {
            for (int column = 0; column < 4; column++) {
                frames[row][column] = allFrames[row][column + 1];
            }
        }
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        animationTime += deltaTime;

        batch.setProjectionMatrix(
            gameViewport.getCamera().combined
        );

        batch.begin();

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(PlayerComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            PositionComponent position =
                entity.getComponent(PositionComponent.class);

            VelocityComponent velocity =
                entity.getComponent(VelocityComponent.class);

            float vx = velocity.getVelocityX();
            float vy = velocity.getVelocityY();

            // Default: facing forward.
            int row = 0;

            // Select direction based on movement.
            if (Math.abs(vx) > Math.abs(vy)) {

                if (vx < 0) {
                    row = 1; // Left
                } else {
                    row = 2; // Right
                }

            } else if (vy > 0) {
                row = 3; // Backwards
            } else if (vy < 0) {
                row = 0; // Forward
            }

            // Animate only when moving.
            int column = 0;

            if (Math.abs(vx) > 0 || Math.abs(vy) > 0) {
                column = (int) (animationTime * 8) % 4;
            }

            TextureRegion currentFrame = frames[row][column];

            batch.draw(
                currentFrame,
                position.getX() - DRAW_WIDTH / 2f,
                position.getY() - DRAW_HEIGHT / 2f,
                DRAW_WIDTH,
                DRAW_HEIGHT
            );
        }

        batch.end();
    }

    public void dispose() {
        batch.dispose();
        playerTexture.dispose();
    }
}

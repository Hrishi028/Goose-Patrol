package com.goosepatrol.game.systems;

import com.goosepatrol.game.GameViewport;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

/**Draws every goose as a simple shape.
 * The body colour shows the goose's state, which also helps with testing.
 * Longboi is drawn bigger and light brown so the player can spot him.
 */
public class GooseRenderSystem implements GameSystem {

    private static final float BODY_WIDTH = 34f;
    private static final float BODY_HEIGHT = 22f;
    private static final float HEAD_RADIUS = 7f;

    private static final Color CHASING_COLOUR = Color.WHITE;
    private static final Color DISTRACTED_COLOUR = new Color(1f, 0.95f, 0.6f, 1f); // pale yellow
    private static final Color LEAVING_COLOUR = Color.LIGHT_GRAY;
    private static final Color BEAK_COLOUR = Color.ORANGE;

    private static final float LONGBOI_SCALE = 1.8f;    //Longboi is drawn 1.8 times bigger
    private static final Color LONGBOI_COLOUR = new Color(0.80f, 0.68f, 0.50f, 1f);    //light brown

    private final ShapeRenderer shapes = new ShapeRenderer();

    private final GameViewport gameViewport;

    public GooseRenderSystem(GameViewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        shapes.setProjectionMatrix(gameViewport.getCamera().combined);

        shapes.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(GooseComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            GooseComponent goose = entity.getComponent(GooseComponent.class);
            PositionComponent position = entity.getComponent(PositionComponent.class);

            //Face left or right depending on which way the goose is moving.
            boolean facingLeft = false;
            if (entity.hasComponent(VelocityComponent.class)) {
                facingLeft = entity.getComponent(VelocityComponent.class).getVelocityX() < 0;
            }
            float side = facingLeft ? -1f : 1f;

            float x = position.getX();
            float y = position.getY();

            //Longboi is drawn bigger; ordinary geese at normal size.
            float scale = goose.isBoss() ? LONGBOI_SCALE : 1f;
            float bodyWidth = BODY_WIDTH * scale;
            float bodyHeight = BODY_HEIGHT * scale;
            float headRadius = HEAD_RADIUS * scale;

            //Body
            shapes.setColor(colourFor(goose));
            shapes.ellipse(x - bodyWidth / 2f, y - bodyHeight / 2f, bodyWidth, bodyHeight);

            //Head
            float headX = x + side * (bodyWidth / 2f);
            float headY = y + bodyHeight / 2f;
            shapes.circle(headX, headY, headRadius);

            //Beak
            shapes.setColor(BEAK_COLOUR);
            shapes.triangle(
                headX + side * headRadius, headY + 3f * scale,
                headX + side * headRadius, headY - 3f * scale,
                headX + side * (headRadius + 8f * scale), headY
            );
        }

        shapes.end();
    }

    private Color colourFor(GooseComponent goose) {
        switch (goose.getState()) {
            case DISTRACTED:
                return DISTRACTED_COLOUR;
            case LEAVING:
                return LEAVING_COLOUR;
            default:
                return goose.isBoss() ? LONGBOI_COLOUR : CHASING_COLOUR;
        }
    }

    public void dispose() {
        shapes.dispose();
    }
}
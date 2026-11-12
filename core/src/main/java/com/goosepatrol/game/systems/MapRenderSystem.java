
package com.goosepatrol.game.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class MapRenderSystem implements GameSystem {

    private final ShapeRenderer shapes = new ShapeRenderer();
    private final GameViewport gameViewport;

    private static final float WORLD_WIDTH = 1300f;
    private static final float WORLD_HEIGHT = 1000f;

    private static final Color GRASS =
        new Color(0.25f, 0.43f, 0.22f, 1f);

    private static final Color PATH =
        new Color(0.88f, 0.79f, 0.62f, 1f);

    private static final Color PATH_BORDER =
        new Color(0.70f, 0.61f, 0.47f, 1f);

    private static final Color WATER_BORDER =
        new Color(0.38f, 0.48f, 0.29f, 1f);

    private static final Color WATER =
        new Color(0.02f, 0.40f, 0.62f, 1f);

    public MapRenderSystem(GameViewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        shapes.setProjectionMatrix(
            gameViewport.getCamera().combined
        );

        float w = WORLD_WIDTH;
        float h = WORLD_HEIGHT;

        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Background
        shapes.setColor(GRASS);
        shapes.rect(0, 0, w, h);

        // Subtle grass details
        shapes.setColor(0.32f, 0.50f, 0.27f, 1f);

        for (int i = 0; i < 150; i++) {
            float x = (i * 137f) % w;
            float y = (i * 89f) % h;

            shapes.circle(x, y, 1.5f);
        }

        // Main horizontal pathway
        drawPath(
            w * 0.06f, h * 0.44f,
            w * 0.94f, h * 0.44f,
            38f
        );

        // Main vertical pathway
        drawPath(
            w * 0.50f, h * 0.08f,
            w * 0.50f, h * 0.91f,
            38f
        );

        // Secondary lower pathway
        drawPath(
            w * 0.10f, h * 0.27f,
            w * 0.90f, h * 0.27f,
            30f
        );

        // Roundabout outer border
        shapes.setColor(PATH_BORDER);
        shapes.circle(w * 0.50f, h * 0.44f, 78f);

        // Roundabout interior
        shapes.setColor(PATH);
        shapes.circle(w * 0.50f, h * 0.44f, 72f);

        // Central green island
        shapes.setColor(WATER_BORDER);
        shapes.circle(w * 0.50f, h * 0.44f, 34f);

        shapes.setColor(0.30f, 0.49f, 0.25f, 1f);
        shapes.circle(w * 0.50f, h * 0.44f, 29f);

        // Lake border
        shapes.setColor(WATER_BORDER);
        shapes.ellipse(
            w * 0.59f,
            h * 0.57f,
            w * 0.25f,
            h * 0.19f
        );

        // Lake water
        shapes.setColor(WATER);
        shapes.ellipse(
            w * 0.60f,
            h * 0.58f,
            w * 0.23f,
            h * 0.17f
        );

        // Water reflections
        shapes.setColor(0.04f, 0.58f, 0.75f, 1f);

        shapes.ellipse(
            w * 0.65f,
            h * 0.68f,
            w * 0.07f,
            h * 0.012f
        );

        shapes.ellipse(
            w * 0.70f,
            h * 0.55f,
            w * 0.035f,
            h * 0.009f
        );

        shapes.end();
    }

    private void drawPath(
        float x1, float y1,
        float x2, float y2,
        float thickness
    ) {

        shapes.setColor(PATH_BORDER);

        shapes.rectLine(
            x1, y1, x2, y2,
            thickness + 4f
        );

        shapes.setColor(PATH);

        shapes.rectLine(
            x1, y1, x2, y2,
            thickness
        );
    }

    public void dispose() {
        shapes.dispose();
    }
}

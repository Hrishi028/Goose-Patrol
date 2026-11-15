
package com.goosepatrol.game.systems;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;

public class BuildingRenderSystem implements GameSystem {

    private final SpriteBatch batch = new SpriteBatch();
    private final GameViewport gameViewport;

    private final Texture building1 =
        new Texture(Gdx.files.internal("map/buildings/building1.png"));

    private final Texture building2 =
        new Texture(Gdx.files.internal("map/buildings/building2.png"));

    private final Texture building3 =
        new Texture(Gdx.files.internal("map/buildings/building3.png"));

    private final Texture building4 =
        new Texture(Gdx.files.internal("map/buildings/building4.png"));

    public BuildingRenderSystem(GameViewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        float w = gameViewport.getWorldWidth();
        float h = gameViewport.getWorldHeight();

        batch.setProjectionMatrix(
            gameViewport.getCamera().combined
        );

        batch.begin();

        // Upper-left building
        batch.draw(
            building1,
            w * 0.025f, h * 0.69f,
            w * 0.25f, h * 0.22f
        );

        // Upper-right building
        batch.draw(
            building2,
            w * 0.735f, h * 0.70f,
            w * 0.24f, h * 0.22f
        );

        // Lower-left building
        batch.draw(
            building3,
            w * 0.025f, h * 0.055f,
            w * 0.25f, h * 0.22f
        );

        // Lower-right building
        batch.draw(
            building4,
            w * 0.725f, h * 0.055f,
            w * 0.25f, h * 0.22f
        );

        batch.end();
    }

    public void dispose() {

        batch.dispose();

        building1.dispose();
        building2.dispose();
        building3.dispose();
        building4.dispose();
    }
}


package com.goosepatrol.game;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.graphics.OrthographicCamera;

public class GameViewport {

    public static final float WORLD_WIDTH = 1300f;
    public static final float WORLD_HEIGHT = 1000f;

    private final OrthographicCamera camera;
    private final FitViewport viewport;

    public GameViewport() {
        camera = new OrthographicCamera();

        viewport = new FitViewport(
            WORLD_WIDTH,
            WORLD_HEIGHT,
            camera
        );

        resize(
            Gdx.graphics.getWidth(),
            Gdx.graphics.getHeight()
        );
    }

    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

    public void apply() {
        viewport.apply();
    }

    public FitViewport getViewport() {
        return viewport;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public float getWorldWidth() {
        return WORLD_WIDTH;
    }

    public float getWorldHeight() {
        return WORLD_HEIGHT;
    }
}

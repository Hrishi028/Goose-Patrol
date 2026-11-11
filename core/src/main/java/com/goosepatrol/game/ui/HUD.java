package com.goosepatrol.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.goosepatrol.game.session.GameSession;
import com.goosepatrol.game.session.GameState;

/** Draws the on-screen information: the timer and status messages. */
public class HUD {

    private static final int FONT_SIZE = 25;

    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font;

    public HUD() {
        // How many real pixels there are per screen unit (2 on a Mac Retina screen).
        float density = Gdx.graphics.getBackBufferWidth() / (float) Gdx.graphics.getWidth();

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(Gdx.files.internal("fonts/hud.ttf"));

        FreeTypeFontParameter settings = new FreeTypeFontParameter();
        settings.size = Math.round(FONT_SIZE * density);
        settings.color = Color.WHITE;
        settings.borderWidth = 1.5f * density;
        settings.borderColor = new Color(0.10f, 0.20f, 0.10f, 1f);
        settings.minFilter = Texture.TextureFilter.Linear;
        settings.magFilter = Texture.TextureFilter.Linear;

        font = generator.generateFont(settings);
        generator.dispose();

        font.getData().setScale(1f / density);
        font.setUseIntegerPositions(false);
    }

    public void draw(GameSession session) {
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();

        batch.begin();

        font.draw(batch, "Time: " + session.getTimer().asText(), 20, height - 20);

        if (session.getState() == GameState.PAUSED) {
            font.draw(batch, "PAUSED - press P to play", width / 2f - 170, height / 2f);
        } else if (session.getState() == GameState.TIMED_OUT) {
            font.draw(batch, "Time's up!", width / 2f - 70, height / 2f);
        }

        batch.end();
    }

    public void dispose() {
        batch.dispose();
        font.dispose();
    }
}
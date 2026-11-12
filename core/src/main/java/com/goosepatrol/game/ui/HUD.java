
package com.goosepatrol.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.session.GameSession;
import com.goosepatrol.game.session.GameState;

public class HUD {

    private static final int FONT_SIZE = 25;

    private static final float MARGIN = 20f;
    private static final float BAR_WIDTH = 200f;
    private static final float BAR_HEIGHT = 18f;
    private static final float BAR_BORDER = 3f;
    private static final float BAR_EASE_SPEED = 8f;

    private static final Color BAR_BACK =
        new Color(0.10f, 0.20f, 0.10f, 1f);

    private static final Color BAR_GOOD =
        new Color(0.36f, 0.78f, 0.36f, 1f);

    private static final Color BAR_LOW =
        new Color(0.95f, 0.70f, 0.20f, 1f);

    private static final Color BAR_DANGER =
        new Color(0.86f, 0.25f, 0.22f, 1f);

    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font;
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final GlyphLayout layout = new GlyphLayout();

    private final GameViewport gameViewport;

    private float shownFraction = 0f;

    private final Color barColor = new Color();

    public HUD(GameViewport gameViewport) {

        this.gameViewport = gameViewport;

        FreeTypeFontGenerator generator =
            new FreeTypeFontGenerator(
                Gdx.files.internal("fonts/hud.ttf")
            );

        FreeTypeFontParameter settings =
            new FreeTypeFontParameter();

        settings.size = FONT_SIZE;
        settings.color = Color.WHITE;
        settings.borderWidth = 1.5f;
        settings.borderColor =
            new Color(0.10f, 0.20f, 0.10f, 1f);

        settings.minFilter = Texture.TextureFilter.Linear;
        settings.magFilter = Texture.TextureFilter.Linear;

        font = generator.generateFont(settings);

        generator.dispose();

        font.setUseIntegerPositions(false);
    }

    public void draw(GameSession session) {

        float width = gameViewport.getWorldWidth();
        float height = gameViewport.getWorldHeight();

        float barX = width - MARGIN - BAR_WIDTH;
        float barY = height - MARGIN - BAR_HEIGHT;

        float target = session.getStress().getFraction();

        float step = Math.min(
            1f,
            BAR_EASE_SPEED * Gdx.graphics.getDeltaTime()
        );

        shownFraction += (target - shownFraction) * step;

        drawStressBar(shownFraction, barX, barY);

        batch.setProjectionMatrix(
            gameViewport.getCamera().combined
        );

        batch.begin();

        // Timer
        font.draw(
            batch,
            "Time: " + session.getTimer().asText(),
            MARGIN,
            height - MARGIN
        );

        // Stress label
        layout.setText(font, "Stress");

        font.draw(
            batch,
            layout,
            barX - layout.width - 10f,
            height - MARGIN
        );

        // Goose counter
        font.draw(
            batch,
            "Geese: " + session.getGeeseDispersed()
                + "/" + session.getDispersalTarget(),
            MARGIN,
            height - MARGIN - font.getLineHeight()
        );

        // Pause and game-state messages
        String message = messageFor(session.getState());

        if (message != null) {

            layout.setText(font, message);

            font.draw(
                batch,
                layout,
                (width - layout.width) / 2f,
                height / 2f
            );
        }

        // Restart message
        if (session.hasEnded()) {

            layout.setText(font, "Press R to restart");

            font.draw(
                batch,
                layout,
                (width - layout.width) / 2f,
                height / 2f - 40f
            );
        }

        batch.end();
    }

    private void drawStressBar(
        float fraction,
        float x,
        float y
    ) {

        shapes.setProjectionMatrix(
            gameViewport.getCamera().combined
        );

        shapes.begin(ShapeRenderer.ShapeType.Filled);

        // Background
        shapes.setColor(BAR_BACK);

        shapes.rect(
            x - BAR_BORDER,
            y - BAR_BORDER,
            BAR_WIDTH + 2 * BAR_BORDER,
            BAR_HEIGHT + 2 * BAR_BORDER
        );

        // Colour changes depending on stress level
        if (fraction < 0.5f) {

            barColor.set(BAR_GOOD)
                .lerp(BAR_LOW, fraction / 0.5f);

        } else {

            barColor.set(BAR_LOW)
                .lerp(BAR_DANGER, (fraction - 0.5f) / 0.5f);
        }

        shapes.setColor(barColor);

        shapes.rect(
            x,
            y,
            BAR_WIDTH * fraction,
            BAR_HEIGHT
        );

        shapes.end();
    }

    private String messageFor(GameState state) {

        switch (state) {

            case PAUSED:
                return "PAUSED - Press P To Play";

            case WON:
                return "YOU WIN! The Campus is Calm";

            case TIMED_OUT:
                return "Time's UP!";

            case STRESSED_OUT:
                return "Too Stressed!!! Game Over :(";

            default:
                return null;
        }
    }

    public void dispose() {

        batch.dispose();
        shapes.dispose();
        font.dispose();
    }
}


package com.goosepatrol.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

import com.goosepatrol.game.GoosePatrolGame;
import com.goosepatrol.game.ui.Fonts;

public class TutorialScreen extends ScreenAdapter {

    private static final String[] LINES = {
        "Disperse 10 geese before the time runs out",
        "Don't let your Stress bar fill up!",
        "",
        "Move: W A S D or the arrow keys",
        "Pause: P or Esc",
        "Back to menu (when paused): M"
    };

    private final GoosePatrolGame game;

    private final SpriteBatch batch = new SpriteBatch();
    private final GlyphLayout layout = new GlyphLayout();

    private final BitmapFont titleFont = Fonts.create(40);
    private final BitmapFont textFont = Fonts.create(20);

    public TutorialScreen(GoosePatrolGame game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.showScreen(ScreenType.GAME);
            return;
        }

        ScreenUtils.clear(0.25f, 0.43f, 0.22f, 1f);

        game.getGameViewport().apply();

        float width = game.getGameViewport().getWorldWidth();
        float height = game.getGameViewport().getWorldHeight();

        batch.setProjectionMatrix(
            game.getGameViewport().getCamera().combined
        );

        batch.begin();

        drawCentred(
            titleFont,
            "How to play",
            width,
            height * 0.85f
        );

        float y = height * 0.68f;

        for (String line : LINES) {

            drawCentred(textFont, line, width, y);

            y -= textFont.getLineHeight() * 1.3f;
        }

        drawCentred(
            textFont,
            "Press ENTER to play",
            width,
            height * 0.15f
        );

        batch.end();
    }

    private void drawCentred(
        BitmapFont font,
        String text,
        float width,
        float y
    ) {

        layout.setText(font, text);

        font.draw(
            batch,
            layout,
            (width - layout.width) / 2f,
            y
        );
    }

    @Override
    public void dispose() {

        batch.dispose();
        titleFont.dispose();
        textFont.dispose();
    }
}

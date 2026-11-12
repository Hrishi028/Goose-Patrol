
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

public class MenuScreen extends ScreenAdapter {

    private final GoosePatrolGame game;

    private final SpriteBatch batch = new SpriteBatch();
    private final GlyphLayout layout = new GlyphLayout();

    private final BitmapFont titleFont = Fonts.create(56);
    private final BitmapFont textFont = Fonts.create(25);

    public MenuScreen(GoosePatrolGame game) {
        this.game = game;
    }

    @Override
    public void render(float delta) {

        if (Gdx.input.isKeyJustPressed(Input.Keys.ENTER)) {
            game.showScreen(ScreenType.TUTORIAL);
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

        drawCentred(titleFont, "Goose Patrol", width, height * 0.65f);

        drawCentred(textFont, "Press ENTER to start", width, height * 0.40f);

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

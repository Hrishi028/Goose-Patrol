package com.goosepatrol.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.GoosePatrolGame;
import com.goosepatrol.game.items.Item;
import com.goosepatrol.game.session.GameSession;
import com.goosepatrol.game.session.GameState;
import com.goosepatrol.game.session.StatsTracker;
import com.goosepatrol.game.ui.Fonts;

import java.util.List;

/*the review screen shown when a game finishes showing how it ended and what the player did */
public class EndScreen extends ScreenAdapter
{
    private final GoosePatrolGame game;
    private final GameViewport gameViewport;
    private final GameSession session;

    private final SpriteBatch batch = new SpriteBatch();
    private final GlyphLayout layout = new GlyphLayout();
    private final BitmapFont titleFont = Fonts.create(64);
    private final BitmapFont headingFont = Fonts.create(30);
    private final BitmapFont textFont = Fonts.create(24);

    //where the next line of text will be drawn and it moves down as lines are added
    private float y;

    public EndScreen(GoosePatrolGame game, GameSession session)
    {
        this.game = game;
        this.gameViewport = game.getGameViewport();
        this.session = session;
    }

    @Override
    public void render(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.R))
        {
            game.showScreen(ScreenType.GAME);
            return;
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.M))
        {
            game.showScreen(ScreenType.MENU);
            return;
        }

        //green for a win a darker colour for a loss
        if (session.getState() == GameState.WON)
        {
            ScreenUtils.clear(0.25f, 0.43f, 0.22f, 1f);
        }
        else
        {
            ScreenUtils.clear(0.20f, 0.16f, 0.16f, 1f);
        }

        gameViewport.apply();
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        StatsTracker stats = session.getStats();
        float height = gameViewport.getWorldHeight();

        batch.begin();

        y = height * 0.90f;
        line(titleFont, title());
        line(textFont, subtitle());
        gap();

        line(headingFont, "Geese dispersed:  " + session.getGeeseDispersed()
            + " / " + session.getDispersalTarget());
        line(headingFont, "Time left:  " + session.getTimer().asText());
        gap();

        line(headingFont, "Items used:  " + stats.getItemsUsed());
        for (Item item : session.getInventory().getItems())
        {
            line(textFont, item.getName() + "   x" + stats.getTimesUsed(item.getName()));
        }
        gap();

        List<String> events = stats.getEventsTriggered();
        line(headingFont, "Hidden events triggered:  " + events.size());
        if (events.isEmpty())
        {
            line(textFont, "None this time");
        }
        for (String event : events)
        {
            line(textFont, event);
        }

        //the key hints stay fixed near the bottom of the screen
        y = height * 0.10f;
        line(textFont, "R: play again        M: menu");

        batch.end();
    }

    /*drawing one centred line of text then moving down ready for the next one */
    private void line(BitmapFont font, String text)
    {
        layout.setText(font, text);
        font.draw(batch, layout, (gameViewport.getWorldWidth() - layout.width) / 2f, y);
        y -= font.getLineHeight() * 1.25f;
    }

    /*leaving some empty space between two groups of lines */
    private void gap()
    {
        y -= textFont.getLineHeight() * 0.8f;
    }

    private String title()
    {
        switch (session.getState())
        {
            case WON:
                return "You win!";
            case STRESSED_OUT:
                return "Too stressed!";
            default:
                return "Time's up!";
        }
    }

    private String subtitle()
    {
        switch (session.getState())
        {
            case WON:
                return "The campus is calm again.";
            case STRESSED_OUT:
                return "The geese got the better of you.";
            default:
                return "The geese are still out there.";
        }
    }

    @Override
    public void dispose()
    {
        batch.dispose();
        titleFont.dispose();
        headingFont.dispose();
        textFont.dispose();
    }
}
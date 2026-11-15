package com.goosepatrol.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goosepatrol.game.items.Item;
import com.goosepatrol.game.session.GameSession;
import com.goosepatrol.game.session.GameState;
import com.goosepatrol.game.GameViewport;

import java.util.List;

/* draws the on screen information: the timer, goose count, stress bar, inventory and messages */
public class HUD
{
    private static final int FONT_SIZE = 25;
    private static final int SMALL_FONT_SIZE = 16;

    private static final float MARGIN = 20f;

    //the stress bar sits at the bottom of the screen, just above the inventory
    private static final float BAR_WIDTH = 240f;
    private static final float BAR_HEIGHT = 26f;
    private static final float BAR_BORDER = 3f;
    private static final float BAR_EASE_SPEED = 8f;

    //the inventory is a row of slots along the bottom edge
    private static final float SLOT_WIDTH = 96f;
    private static final float SLOT_HEIGHT = 64f;
    private static final float SLOT_GAP = 8f;
    private static final float SLOT_PADDING = 7f;

    private static final Color BAR_BACK = new Color(0.10f, 0.20f, 0.10f, 1f);
    private static final Color BAR_GOOD = new Color(0.36f, 0.78f, 0.36f, 1f);
    private static final Color BAR_LOW = new Color(0.95f, 0.70f, 0.20f, 1f);
    private static final Color BAR_DANGER = new Color(0.86f, 0.25f, 0.22f, 1f);

    private static final Color SLOT_FILL = new Color(0.17f, 0.30f, 0.17f, 1f);
    private static final Color SLOT_ACTIVE = new Color(0.20f, 0.42f, 0.60f, 1f);
    private static final Color TEXT_EMPTY = new Color(0.60f, 0.60f, 0.60f, 1f);

    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final GlyphLayout layout = new GlyphLayout();
    private final BitmapFont font = Fonts.create(FONT_SIZE);
    private final BitmapFont smallFont = Fonts.create(SMALL_FONT_SIZE);

    //the fraction currently drawn; it glides towards the real one so the bar moves smoothly
    private float shownFraction = 0f;
    private final Color barColor = new Color();


    private final GameViewport gameViewport;

    public HUD(GameViewport gameViewport)
    {
        this.gameViewport = gameViewport;
    }

    public void draw(GameSession session)
    {
        float width = gameViewport.getWorldWidth();
        float height = gameViewport.getWorldHeight();

        shapes.setProjectionMatrix(gameViewport.getCamera().combined);
        batch.setProjectionMatrix(gameViewport.getCamera().combined);

        List<Item> items = session.getInventory().getItems();

        //the bar is centred, sitting just above the row of slots
        float barX = width - MARGIN - BAR_WIDTH;
        float barY = height - MARGIN - BAR_HEIGHT;

        float target = session.getStress().getFraction();
        float step = Math.min(1f, BAR_EASE_SPEED * Gdx.graphics.getDeltaTime());
        shownFraction += (target - shownFraction) * step;

        //shapes first, then all the text on top
        drawStressBar(shownFraction, barX, barY);
        drawInventorySlots(items, width);

        batch.begin();

        font.draw(batch, "Time: " + session.getTimer().asText(), MARGIN, height - MARGIN);

        font.draw(batch, "Geese: " + session.getGeeseDispersed() + "/" + session.getDispersalTarget(),
            MARGIN, height - MARGIN - font.getLineHeight());

        //the percentage is written in the middle of the stress bar
        layout.setText(smallFont, "Stress  " + Math.round(shownFraction * 100f) + "%");
        smallFont.draw(batch, layout,
            barX + (BAR_WIDTH - layout.width) / 2f,
            barY + (BAR_HEIGHT + layout.height) / 2f);

        drawInventoryText(items, width);

        String message = messageFor(session.getState());
        if (message != null)
        {
            layout.setText(font, message);
            font.draw(batch, layout, (width - layout.width) / 2f, height / 2f);
        }
        if (session.hasEnded())
        {
            layout.setText(font, "Press R to restart");
            font.draw(batch, layout, (width - layout.width) / 2f, height / 2f - 40f);
        }

        batch.end();
    }

    /*drawing the stress bar: a dark frame with a coloured fill that grows as stress builds */
    private void drawStressBar(float fraction, float x, float y)
    {
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(BAR_BACK);
        shapes.rect(x - BAR_BORDER, y - BAR_BORDER,
            BAR_WIDTH + 2 * BAR_BORDER, BAR_HEIGHT + 2 * BAR_BORDER);

        //blend green into amber over the first half, then amber into red
        if (fraction < 0.5f)
        {
            barColor.set(BAR_GOOD).lerp(BAR_LOW, fraction / 0.5f);
        }
        else
        {
            barColor.set(BAR_LOW).lerp(BAR_DANGER, (fraction - 0.5f) / 0.5f);
        }
        shapes.setColor(barColor);
        shapes.rect(x, y, BAR_WIDTH * fraction, BAR_HEIGHT);

        shapes.end();
    }

    /*where the first inventory slot starts, so that the row of slots is centred */
    private float firstSlotX(int count, float width)
    {
        float rowWidth = count * SLOT_WIDTH + (count - 1) * SLOT_GAP;
        return (width - rowWidth) / 2f;
    }

    /*drawing one box per item along the bottom of the screen */
    private void drawInventorySlots(List<Item> items, float width)
    {
        float x = firstSlotX(items.size(), width);

        shapes.begin(ShapeRenderer.ShapeType.Filled);
        for (Item item : items)
        {
            //a dark frame, then the inside of the slot on top of it
            shapes.setColor(BAR_BACK);
            shapes.rect(x, MARGIN, SLOT_WIDTH, SLOT_HEIGHT);

            //a slot is highlighted while its item's effect is still going on
            shapes.setColor(item.isActive() ? SLOT_ACTIVE : SLOT_FILL);
            shapes.rect(x + BAR_BORDER, MARGIN + BAR_BORDER,
                SLOT_WIDTH - 2 * BAR_BORDER, SLOT_HEIGHT - 2 * BAR_BORDER);

            x += SLOT_WIDTH + SLOT_GAP;
        }
        shapes.end();
    }

    /*writing each slot's key number (top left), item name (middle) and uses left (bottom right) */
    private void drawInventoryText(List<Item> items, float width)
    {
        float x = firstSlotX(items.size(), width);
        int key = 1;

        for (Item item : items)
        {
            //an item with no uses left is shown in grey
            smallFont.setColor(item.getUsesLeft() > 0 ? Color.WHITE : TEXT_EMPTY);

            smallFont.draw(batch, String.valueOf(key),
                x + SLOT_PADDING, MARGIN + SLOT_HEIGHT - SLOT_PADDING);

            layout.setText(smallFont, item.getName());
            smallFont.draw(batch, layout,
                x + (SLOT_WIDTH - layout.width) / 2f,
                MARGIN + (SLOT_HEIGHT + layout.height) / 2f);

            layout.setText(smallFont, "x" + item.getUsesLeft());
            smallFont.draw(batch, layout,
                x + SLOT_WIDTH - SLOT_PADDING - layout.width,
                MARGIN + SLOT_PADDING + layout.height);

            x += SLOT_WIDTH + SLOT_GAP;
            key++;
        }
        smallFont.setColor(Color.WHITE);
    }

    /*the message for the middle of the screen or null when there is none */
    private String messageFor(GameState state)
    {
        switch (state)
        {
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

    public void dispose()
    {
        batch.dispose();
        shapes.dispose();
        font.dispose();
        smallFont.dispose();
    }
}
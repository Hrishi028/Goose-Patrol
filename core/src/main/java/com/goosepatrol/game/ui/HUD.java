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
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/* draws the on screen information: the timer and status messages */
public class HUD 
{

    private static final int FONT_SIZE = 25;

    private static final float MARGIN = 20f;
    private static final float BAR_WIDTH = 200f;
    private static final float BAR_HEIGHT = 18f;
    private static final float BAR_BORDER = 3f;

    private static final Color BAR_BACK = new Color(0.10f, 0.20f, 0.10f, 1f);
    private static final Color BAR_GOOD = new Color(0.36f, 0.78f, 0.36f, 1f);
    private static final Color BAR_LOW = new Color(0.95f, 0.70f, 0.20f, 1f);
    private static final Color BAR_DANGER = new Color(0.86f, 0.25f, 0.22f, 1f);


    private final SpriteBatch batch = new SpriteBatch();
    private final BitmapFont font;
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final GlyphLayout layout = new GlyphLayout();

    public HUD()
   {
        /*  how many real pixels there are per screen unit */
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

    public void draw(GameSession session) 
    {
        float height = Gdx.graphics.getHeight();
        float width = Gdx.graphics.getWidth();

        float barX = width - MARGIN - BAR_WIDTH;
        float barY = height - MARGIN - BAR_HEIGHT;

        drawStressBar(session.getStress().getFraction(), barX, barY);


        batch.begin();

        font.draw(batch, "Time: " + session.getTimer().asText(), MARGIN, height- MARGIN);
        
        /*the label sits just to the left of the bar */
        layout.setText(font, "Stress");
        font.draw(batch, layout, barX-layout.width - 10f, height-MARGIN);

        String message = messageFor(session.getState());
        if (message !=null)
        {
            layout.setText(font, message);
            font.draw(batch, layout, (width-layout.width)/2f,height/2f);
        }
        batch.end();
    }

    /*drawing the bar */
    private void drawStressBar(float fraction, float x, float y)
    {
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        shapes.setColor(BAR_BACK);
        shapes.rect(x-BAR_BORDER, y-BAR_BORDER,
            BAR_WIDTH + 2 * BAR_BORDER, BAR_HEIGHT + 2 * BAR_BORDER);
        
            if (fraction > 0.5f)
            {
                shapes.setColor(BAR_GOOD);
            }
            else if(fraction>0.25f)
            {
                shapes.setColor(BAR_LOW);
            }
            else 
            {
                shapes.setColor(BAR_DANGER);
            }
            shapes.rect(x,y, BAR_WIDTH * fraction, BAR_HEIGHT);

            shapes.end();
        }

        /*the message for the middle of the screen or null when there is none */
        private String messageFor(GameState state)
        {
            switch(state)
            {
                case PAUSED:
                    return "PAUSED - Press P To Play";
                case TIMED_OUT:
                    return "Time's UP!";
                case STRESSED_OUT:
                    return "Too Stresses!!! Game Over :(";
                default:
                    return null;
            }
        }
        
        public void dispose()
        {
            batch.dispose();
            shapes.dispose();
            font.dispose();
        }


    



     







    
}
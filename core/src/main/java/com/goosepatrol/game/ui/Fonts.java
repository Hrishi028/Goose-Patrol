package com.goosepatrol.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;

/*building the game's font at a given size so every screen's text looks the same */
public final class Fonts
{
    private Fonts()
    {

    }

    /*creating the game font at the given size and whoever calls this must dispose of the font */
    public static BitmapFont create(int size)
    {
        //amount of pixels 
        float density = Gdx.graphics.getBackBufferWidth()/(float) Gdx.graphics.getWidth();

        FreeTypeFontGenerator generator=new FreeTypeFontGenerator(Gdx.files.internal("fonts/hud.ttf"));

        FreeTypeFontParameter settings = new FreeTypeFontParameter();
        settings.size = Math.round(size * density);
        settings.color = Color.WHITE;
        settings.borderWidth = 1.5f * density;
        settings.borderColor = new Color(0.10f, 0.20f, 0.10f, 1f);
        settings.minFilter = Texture.TextureFilter.Linear;
        settings.magFilter = Texture.TextureFilter.Linear;

        BitmapFont font = generator.generateFont(settings);
        generator.dispose();

        font.getData().setScale(1f / density);
        font.setUseIntegerPositions(false);
        return font;

    }
}
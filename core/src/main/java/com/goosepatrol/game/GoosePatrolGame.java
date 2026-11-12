package com.goosepatrol.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.goosepatrol.game.screens.GameScreen;
import com.goosepatrol.game.screens.ScreenType;
import com.goosepatrol.game.screens.MenuScreen;
import com.goosepatrol.game.screens.TutorialScreen;

/*the entry point of the game and its only job is to switch between screens */
public class GoosePatrolGame extends Game
{
    @Override 
    public void create()
    {
        showScreen(ScreenType.MENU);
        
    }

    /*replacing the current screen with a new one of the given type */
    public void showScreen(ScreenType type)
    {
        Screen previous= getScreen();

        switch (type)
        {
            case GAME:
                setScreen(new GameScreen(this));
                break;
            case TUTORIAL:
                setScreen(new TutorialScreen(this));
                break;
            case MENU:
            default:
                setScreen(new MenuScreen(this));
                break;
        }

        //the old screen is no longer required
        if(previous!=null)
        {
            previous.dispose();
        }
    }

    @Override 
    public void dispose()
    {
        if (getScreen()!=null)
        {
            getScreen().dispose();
        }
    }

}
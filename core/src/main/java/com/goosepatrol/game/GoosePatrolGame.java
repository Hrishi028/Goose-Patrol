
package com.goosepatrol.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.goosepatrol.game.screens.GameScreen;
import com.goosepatrol.game.screens.ScreenType;
import com.goosepatrol.game.screens.MenuScreen;
import com.goosepatrol.game.screens.TutorialScreen;
import com.goosepatrol.game.screens.EndScreen;
import com.goosepatrol.game.session.GameSession;

public class GoosePatrolGame extends Game {

    private GameViewport gameViewport;

    //when the game that has finished the review screen can show its result
    private GameSession finishedSession;

    @Override
    public void create() {
        gameViewport = new GameViewport();
        showScreen(ScreenType.MENU);
    }

    public GameViewport getGameViewport() {
        return gameViewport;
    }

    public void showScreen(ScreenType type) {

        Screen previous = getScreen();

        switch (type) {
            case GAME:
                setScreen(new GameScreen(this));
                break;

            case TUTORIAL:
                setScreen(new TutorialScreen(this));
                break;
            case END:
                setScreen(new EndScreen(this, finishedSession));
                break;

            case MENU:
            default:
                setScreen(new MenuScreen(this));
                break;
        }

        if (previous != null) {
            previous.dispose();
        }
    }
    /*showing the review scren for a game that has just finished */
    public void showEndScreen(GameSession session)
    {
        finishedSession=session;
        showScreen(ScreenType.END);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);

        if (gameViewport != null) {
            gameViewport.resize(width, height);
        }
    }

    @Override
    public void dispose() {
        if (getScreen() != null) {
            getScreen().dispose();
        }
    }
}

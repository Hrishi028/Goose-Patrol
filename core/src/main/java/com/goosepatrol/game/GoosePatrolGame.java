
package com.goosepatrol.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import com.goosepatrol.game.screens.GameScreen;
import com.goosepatrol.game.screens.ScreenType;
import com.goosepatrol.game.screens.MenuScreen;
import com.goosepatrol.game.screens.TutorialScreen;

public class GoosePatrolGame extends Game {

    private GameViewport gameViewport;

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

            case MENU:
            default:
                setScreen(new MenuScreen(this));
                break;
        }

        if (previous != null) {
            previous.dispose();
        }
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

 package com.goosepatrol.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goosepatrol.game.GoosePatrolGame;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.entities.PlayerFactory;
import com.goosepatrol.game.session.GameSession;
import com.goosepatrol.game.systems.BoundarySystem;
import com.goosepatrol.game.systems.BuildingRenderSystem;
import com.goosepatrol.game.systems.CampusCollisionSystem;
import com.goosepatrol.game.systems.DecorationRenderSystem;
import com.goosepatrol.game.systems.MapRenderSystem;
import com.goosepatrol.game.systems.MovementSystem;
import com.goosepatrol.game.systems.PlayerInputSystem;
import com.goosepatrol.game.systems.PlayerRenderSystem;
import com.goosepatrol.game.ui.HUD;

/*the screen where the game is played */

public class GameScreen extends ScreenAdapter
{
    private final GoosePatrolGame game;

    private final GameSession session;
    private final GameWorld gameWorld;
    private final Entity player;
    private final HUD hud;

    private final MapRenderSystem mapRenderSystem;
    private final DecorationRenderSystem decorationRenderSystem;
    private final BuildingRenderSystem buildingRenderSystem;
    private final PlayerRenderSystem playerRenderSystem;

    public GameScreen(GoosePatrolGame game)
    {
        this.game = game;

        session = new GameSession();

        gameWorld = new GameWorld();

        mapRenderSystem = new MapRenderSystem();
        gameWorld.addSystem(mapRenderSystem);

        decorationRenderSystem = new DecorationRenderSystem();
        gameWorld.addSystem(decorationRenderSystem);

        buildingRenderSystem = new BuildingRenderSystem();
        gameWorld.addSystem(buildingRenderSystem);

        gameWorld.addSystem(new PlayerInputSystem());
        gameWorld.addSystem(new MovementSystem());
        gameWorld.addSystem(new CampusCollisionSystem());
        gameWorld.addSystem(new BoundarySystem());

        EntityFactory entityFactory = new EntityFactory();
        player = PlayerFactory.createPlayer(entityFactory);
        gameWorld.addEntity(player);

    

        playerRenderSystem = new PlayerRenderSystem();
        gameWorld.addSystem(playerRenderSystem);

        hud = new HUD();
    }

    /*getting called by LibGDX once every frame while this screen is showing */
    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        if(Gdx.input.isKeyJustPressed(Input.Keys.P)
            || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            session.togglePause();
        }

        //from the pause screen or once the game has ended M goes back to the menu
        if(!session.isRunning()&& Gdx.input.isKeyJustPressed(Input.Keys.M))
        {
            game.showScreen(ScreenType.MENU);
            return;
        }
        if (session.hasEnded() && Gdx.input.isKeyJustPressed(Input.Keys.R))
        {
            restart();

        }
        if (session.isRunning() && Gdx.input.isKeyJustPressed(Input.Keys.K))
        {
            session.getStress().add(10f);
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.G))
        {
            session.gooseDispersed();
        }
        session.update(delta);

       
        gameWorld.update(session.isRunning()?delta:0f);

        hud.draw(session);


    }

    public void restart()
    {
        session.restart();

        player.getComponent(PositionComponent.class).setPosition(Gdx.graphics.getWidth()/2,Gdx.graphics.getHeight()/2f);
        player.getComponent(VelocityComponent.class).setVelocity(0, 0);

    }

    @Override 
    public void dispose()
    {
        playerRenderSystem.dispose();
        mapRenderSystem.dispose();
        buildingRenderSystem.dispose();
        decorationRenderSystem.dispose();
        hud.dispose();
    }

}


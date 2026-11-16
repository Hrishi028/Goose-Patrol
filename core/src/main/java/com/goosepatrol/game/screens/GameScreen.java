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
import com.goosepatrol.game.systems.GooseBehaviourSystem;
import com.goosepatrol.game.systems.GooseRenderSystem;
import com.goosepatrol.game.systems.GooseSpawnSystem;
import com.goosepatrol.game.systems.MapRenderSystem;
import com.goosepatrol.game.systems.MovementSystem;
import com.goosepatrol.game.systems.PlayerInputSystem;
import com.goosepatrol.game.systems.PlayerRenderSystem;
import com.goosepatrol.game.systems.StressSystem;
import com.goosepatrol.game.systems.PlayerRenderSystem;
import com.goosepatrol.game.ui.HUD;
import com.goosepatrol.game.systems.GooseBehaviourSystem;
import com.goosepatrol.game.systems.GooseRenderSystem;
import com.goosepatrol.game.systems.GooseSpawnSystem;
import com.goosepatrol.game.systems.StressSystem;
import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.systems.EventTriggerSystem;
import com.goosepatrol.game.entities.HiddenEventFactory;
import com.goosepatrol.game.events.BreadVanEvent;
import com.goosepatrol.game.systems.BreadVanRenderSystem;

/*the screen where the game is played */

public class GameScreen extends ScreenAdapter
{
    private GooseRenderSystem gooseRenderSystem;
    private BreadVanRenderSystem breadVanRenderSystem;
    private final GoosePatrolGame game;
    private final GameViewport gameViewport;

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
        this.gameViewport = game.getGameViewport();

        session = new GameSession();

        gameWorld = new GameWorld();

        EntityFactory entityFactory = new EntityFactory();

        mapRenderSystem = new MapRenderSystem(gameViewport);
        gameWorld.addSystem(mapRenderSystem);

        decorationRenderSystem = new DecorationRenderSystem(gameViewport);
        gameWorld.addSystem(decorationRenderSystem);

        buildingRenderSystem = new BuildingRenderSystem(gameViewport);
        gameWorld.addSystem(buildingRenderSystem);

        gameWorld.addSystem(new PlayerInputSystem());
        gameWorld.addSystem(new GooseSpawnSystem(entityFactory));
        gameWorld.addSystem(new GooseBehaviourSystem(session));
        gameWorld.addSystem(new MovementSystem());
        gameWorld.addSystem(new CampusCollisionSystem(gameViewport));
        gameWorld.addSystem(new BoundarySystem(gameViewport));
        gameWorld.addSystem(new StressSystem(session));
        gameWorld.addSystem(new EventTriggerSystem(session));

        //The bread van is drawn first, so geese and the player appear in front of it.
        breadVanRenderSystem = new BreadVanRenderSystem(gameViewport);
        gameWorld.addSystem(breadVanRenderSystem);

        //Geese are drawn before the player, so the player overlaps them.
        gooseRenderSystem = new GooseRenderSystem(gameViewport);
        gameWorld.addSystem(gooseRenderSystem);

        player = PlayerFactory.createPlayer(entityFactory);
        player.getComponent(PositionComponent.class).setPosition(gameViewport.getWorldWidth()/2f, gameViewport.getWorldHeight()/2f );

        gameWorld.addEntity(player);

        //Hidden event (helpful): an invisible Bread Van spot on the path at the bottom of the campus.
        //It can be triggered after 30 seconds of play, by walking within 60 units of it.
        gameWorld.addEntity(HiddenEventFactory.createHiddenEvent(entityFactory, new BreadVanEvent(),
        gameViewport.getWorldWidth() * 0.50f, gameViewport.getWorldHeight() * 0.15f, 60f, 30f));

        playerRenderSystem = new PlayerRenderSystem(gameViewport);
        gameWorld.addSystem(playerRenderSystem);

        hud = new HUD(gameViewport);
    }

    /*called by LibGDX once every frame while this screen is showing */
    @Override
    public void render(float delta)
    {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
          gameViewport.apply();

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
       
        //the number keys use the item in that inventory slot
        if (session.isRunning())
        {
            PositionComponent position = player.getComponent(PositionComponent.class);

            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1))
            {
                session.useItem(0, gameWorld, position.getX(), position.getY());
            }
            if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2))
            {
                session.useItem(1, gameWorld, position.getX(), position.getY());
            }
        }
        session.update(delta);

       
        gameWorld.update(session.isRunning()?delta:0f);

        hud.draw(session);
        if(session.hasEnded())
        {
            game.showEndScreen(session);
        }


    }

    public void restart()
    {
        session.restart();

        player.getComponent(PositionComponent.class).setPosition(gameViewport.getWorldWidth()/2f,gameViewport.getWorldHeight()/2f);
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
        gooseRenderSystem.dispose();
        breadVanRenderSystem.dispose();
    }

}


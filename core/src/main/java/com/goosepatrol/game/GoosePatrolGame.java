package com.goosepatrol.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.systems.MovementSystem;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;
import com.goosepatrol.game.entities.PlayerFactory;
import com.goosepatrol.game.systems.PlayerInputSystem;
import com.goosepatrol.game.systems.PlayerRenderSystem;
import com.goosepatrol.game.systems.BoundarySystem;
import com.goosepatrol.game.systems.MapRenderSystem;
import com.goosepatrol.game.systems.BuildingRenderSystem;
import com.goosepatrol.game.systems.DecorationRenderSystem;
import com.goosepatrol.game.systems.CampusCollisionSystem;
import com.badlogic.gdx.Input;
import com.goosepatrol.game.session.GameSession;
import com.goosepatrol.game.ui.HUD;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GoosePatrolGame extends ApplicationAdapter {
    private GameWorld gameWorld;
    private Entity player;
    private PlayerRenderSystem playerRenderSystem;
    private MapRenderSystem mapRenderSystem;
    private BuildingRenderSystem buildingRenderSystem;
    private DecorationRenderSystem decorationRenderSystem;
    private GameSession session;
    private HUD hud;

    @Override
    public void create() {
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

        session =new GameSession();
        hud = new HUD();
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
       
        if (Gdx.input.isKeyJustPressed(Input.Keys.P)|| Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE))
        {
            session.togglePause();
        }
        if(session.hasEnded()&& Gdx.input.isKeyJustPressed(Input.Keys.R))
        {
            restart();
        }

        //TEMPORARY: press K to lower bar...to be removed when geese added
        if (session.isRunning()&& Gdx.input.isKeyJustPressed(Input.Keys.K))
        {
            session.getStress().drop(10f);
        }

        float delta=Gdx.graphics.getDeltaTime();
        session.update(delta);

        /*while the game is not running, the world is still drawn but time stands still */
        gameWorld.update(session.isRunning()? delta:0f);

        hud.draw(session);

    }
    /*starting a new game and resetting the session and putting the player back at the start */
    private void restart()
    {
        session.restart();

        player.getComponent(PositionComponent.class).setPosition(Gdx.graphics.getWidth()/2f,Gdx.graphics.getHeight()/2f );
        player.getComponent(VelocityComponent.class).setVelocity(0,0);
    }


    @Override
    public void dispose() {

        if (playerRenderSystem != null) playerRenderSystem.dispose();
        if (mapRenderSystem != null) mapRenderSystem.dispose();
        if (buildingRenderSystem != null) buildingRenderSystem.dispose();
        if (decorationRenderSystem != null) decorationRenderSystem.dispose();
        if (hud!=null) hud.dispose();
    }

}


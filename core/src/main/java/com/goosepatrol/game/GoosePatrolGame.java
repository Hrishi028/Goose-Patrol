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

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
public class GoosePatrolGame extends ApplicationAdapter {
    private GameWorld gameWorld;
    private Entity player;
    private PlayerRenderSystem playerRenderSystem;
    private MapRenderSystem mapRenderSystem;
    private BuildingRenderSystem buildingRenderSystem;
    private DecorationRenderSystem decorationRenderSystem;

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
        gameWorld.addSystem(new BoundarySystem());

        EntityFactory entityFactory = new EntityFactory();
        player = PlayerFactory.createPlayer(entityFactory);
        gameWorld.addEntity(player);

        playerRenderSystem = new PlayerRenderSystem();
        gameWorld.addSystem(playerRenderSystem);
    }

    @Override
    public void render() {
        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);
        gameWorld.update(Gdx.graphics.getDeltaTime());

    }


    @Override
    public void dispose() {

        if (playerRenderSystem != null) playerRenderSystem.dispose();
        if (mapRenderSystem != null) mapRenderSystem.dispose();
        if (buildingRenderSystem != null) buildingRenderSystem.dispose();
        if (decorationRenderSystem != null) decorationRenderSystem.dispose();
    }

}

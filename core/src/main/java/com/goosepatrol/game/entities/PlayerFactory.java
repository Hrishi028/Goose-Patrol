package com.goosepatrol.game.entities;

import com.badlogic.gdx.Gdx;
import com.goosepatrol.game.components.PlayerComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;

public class PlayerFactory {

    public static Entity createPlayer(EntityFactory entityFactory) {

        Entity player = entityFactory.createEntity();

        // Mark this entity as the player.
        player.addComponent(new PlayerComponent());

        // Start the player in the centre of the screen.
        float startX = Gdx.graphics.getWidth() / 2f;
        float startY = Gdx.graphics.getHeight() / 2f;

        player.addComponent(new PositionComponent(startX, startY));
        player.addComponent(new VelocityComponent(0, 0));

        return player;
    }
}

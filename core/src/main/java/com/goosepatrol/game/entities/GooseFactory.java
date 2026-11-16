package com.goosepatrol.game.entities;

import com.badlogic.gdx.math.MathUtils;
import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.GooseSize;
import com.goosepatrol.game.components.HiddenEventComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;
import com.goosepatrol.game.events.LongboiEvent;

/**Builds goose entities.*/
public class GooseFactory {

    //Geese are much slower than the player so the player can get away.
    private static final float MIN_SPEED = 60f;
    private static final float MAX_SPEED = 80f;

    private static final int MIN_HUNGER = 2;
    private static final int MAX_HUNGER = 4;

    public static Entity createGoose(EntityFactory entityFactory, float x, float y) {

        Entity goose = entityFactory.createEntity();

        int hunger = MathUtils.random(MIN_HUNGER, MAX_HUNGER);
        float speed = MathUtils.random(MIN_SPEED, MAX_SPEED);

        goose.addComponent(new GooseComponent(hunger, speed));
        goose.addComponent(new PositionComponent(x, y));
        goose.addComponent(new VelocityComponent(0, 0));

        return goose;
    }

    //Longboi: faster than normal geese, but still slower than the player.
    private static final float LONGBOI_SPEED = 120f;
    private static final int LONGBOI_HUNGER = 99;              // never gets full
    private static final float ENCOUNTER_RADIUS = 150f;        // how close the player gets before the event triggers

    /**Builds Longboi. He is a goose (so he chases like one) and also carries a hidden event,
     * so EventTriggerSystem triggers the Longboi event the first time the player gets close.
     */
    public static Entity createLongboi(EntityFactory entityFactory, float x, float y) {

        Entity longboi = entityFactory.createEntity();

        longboi.addComponent(new GooseComponent(LONGBOI_HUNGER, LONGBOI_SPEED, GooseSize.BOSS));
        longboi.addComponent(new PositionComponent(x, y));
        longboi.addComponent(new VelocityComponent(0, 0));
        longboi.addComponent(new HiddenEventComponent(new LongboiEvent(), ENCOUNTER_RADIUS, 0f));

        return longboi;
    }
}
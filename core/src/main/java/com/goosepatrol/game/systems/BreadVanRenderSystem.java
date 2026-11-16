package com.goosepatrol.game.systems;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.goosepatrol.game.GameViewport;
import com.goosepatrol.game.components.HiddenEventComponent;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.GameSystem;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.events.BreadVanEvent;

/** Draws the bread van once its hidden event has been triggered.
 * Before that it is not drawn at all, so the event stays hidden.
 * The van is made of simple shapes.
 */
public class BreadVanRenderSystem implements GameSystem {

    private static final float VAN_WIDTH = 120f;
    private static final float VAN_HEIGHT = 60f;
    private static final float WHEEL_RADIUS = 11f;

    private static final Color BODY_COLOUR = new Color(0.97f, 0.95f, 0.88f, 1f);    //cream
    private static final Color STRIPE_COLOUR = new Color(0.80f, 0.20f, 0.18f, 1f);  //red
    private static final Color WINDOW_COLOUR = new Color(0.60f, 0.82f, 0.95f, 1f);  //light blue
    private static final Color LOAF_COLOUR = new Color(0.82f, 0.55f, 0.25f, 1f);    //bread brown
    private static final Color WHEEL_COLOUR = new Color(0.15f, 0.15f, 0.17f, 1f);   //dark grey

    private final GameViewport gameViewport;
    private final ShapeRenderer shapes = new ShapeRenderer();

    public BreadVanRenderSystem(GameViewport gameViewport) {
        this.gameViewport = gameViewport;
    }

    @Override
    public void update(GameWorld world, float deltaTime) {

        shapes.setProjectionMatrix(gameViewport.getCamera().combined);
        shapes.begin(ShapeRenderer.ShapeType.Filled);

        for (Entity entity : world.getEntities()) {

            if (!entity.hasComponent(HiddenEventComponent.class)
                || !entity.hasComponent(PositionComponent.class)) {
                continue;
            }

            HiddenEventComponent hidden = entity.getComponent(HiddenEventComponent.class);

            //only the Bread Van, and only after it has been found
            if (!(hidden.getEvent() instanceof BreadVanEvent) || !hidden.isTriggered()) {
                continue;
            }

            PositionComponent position = entity.getComponent(PositionComponent.class);
            drawVan(position.getX(), position.getY());
        }

        shapes.end();
    }

    /**Draws a van.*/
    private void drawVan(float x, float y) {

        float left = x - VAN_WIDTH / 2f;
        float bottom = y - VAN_HEIGHT / 2f;

        //Body 
        shapes.setColor(BODY_COLOUR);
        shapes.rect(left, bottom, VAN_WIDTH * 0.72f, VAN_HEIGHT);
        shapes.rect(left + VAN_WIDTH * 0.72f, bottom, VAN_WIDTH * 0.28f, VAN_HEIGHT * 0.70f);

        //Red stripe along the side
        shapes.setColor(STRIPE_COLOUR);
        shapes.rect(left, bottom + VAN_HEIGHT * 0.22f, VAN_WIDTH, VAN_HEIGHT * 0.10f);

        //Windscreen 
        shapes.setColor(WINDOW_COLOUR);
        shapes.rect(left + VAN_WIDTH * 0.78f, bottom + VAN_HEIGHT * 0.38f,
                    VAN_WIDTH * 0.17f, VAN_HEIGHT * 0.24f);

        //loaf of bread painted on the side
        shapes.setColor(LOAF_COLOUR);
        shapes.ellipse(left + VAN_WIDTH * 0.18f, bottom + VAN_HEIGHT * 0.45f,
                       VAN_WIDTH * 0.34f, VAN_HEIGHT * 0.36f);

        //Wheels
        shapes.setColor(WHEEL_COLOUR);
        shapes.circle(left + VAN_WIDTH * 0.20f, bottom, WHEEL_RADIUS);
        shapes.circle(left + VAN_WIDTH * 0.80f, bottom, WHEEL_RADIUS);
    }

    public void dispose() {
        shapes.dispose();
    }
}
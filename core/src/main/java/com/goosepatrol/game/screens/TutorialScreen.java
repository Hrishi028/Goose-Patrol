package com.goosepatrol.game.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.goosepatrol.game.GoosePatrolGame;
import com.goosepatrol.game.components.GooseComponent;
import com.goosepatrol.game.components.GooseState;
import com.goosepatrol.game.components.PositionComponent;
import com.goosepatrol.game.components.VelocityComponent;
import com.goosepatrol.game.ecs.Entity;
import com.goosepatrol.game.ecs.EntityFactory;
import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.entities.PlayerFactory;
import com.goosepatrol.game.items.Item;
import com.goosepatrol.game.session.GameSession;
import com.goosepatrol.game.session.StressMeter;
import com.goosepatrol.game.systems.BoundarySystem;
import com.goosepatrol.game.systems.BuildingRenderSystem;
import com.goosepatrol.game.systems.CampusCollisionSystem;
import com.goosepatrol.game.systems.DecorationRenderSystem;
import com.goosepatrol.game.systems.GooseBehaviourSystem;
import com.goosepatrol.game.systems.GooseRenderSystem;
import com.goosepatrol.game.systems.MapRenderSystem;
import com.goosepatrol.game.systems.MovementSystem;
import com.goosepatrol.game.systems.PlayerInputSystem;
import com.goosepatrol.game.systems.PlayerRenderSystem;
import com.goosepatrol.game.ui.Fonts;
import com.goosepatrol.game.ui.HUD;

/**
 * A practice round on the real map. The player is given one task at a time and each
 * task is ticked off when they actually do it. Nothing here can be lost.
 */
public class TutorialScreen extends ScreenAdapter {

    /** The tasks, in the order the player meets them. */
    private enum Step { MOVE, STRESS, BREAD, UMBRELLA, PAUSE, DONE }

    private static final float MOVE_DISTANCE = 150f;       // how far to walk to finish the first task
    private static final float GOOSE_START_DISTANCE = 260f; // how far away the practice goose appears
    private static final float GOOSE_SPEED = 110f;
    private static final int GOOSE_HUNGER = 2;              // one bread is enough to send it away
    private static final float GOOSE_REACH = 45f;           // how close counts as "on you"
    private static final float STRESS_PER_SECOND = 12f;
    private static final float STRESS_TO_SHOW = 0.20f;      // the stress task is done at 20%
    private static final float STRESS_CAP = 0.60f;          // practice stress never goes above 60%
    private static final float PRAISE_SECONDS = 0.9f;       // how long "Nice!" shows between tasks

    private static final float PANEL_HEIGHT = 96f;
    private static final float PANEL_MARGIN = 20f;
    private static final Color PANEL_COLOR = new Color(0.05f, 0.12f, 0.05f, 0.85f);
    private static final Color PRAISE_COLOR = new Color(0.55f, 0.95f, 0.55f, 1f);

    private final GoosePatrolGame game;

    private final GameSession session = new GameSession();
    private final GameWorld gameWorld = new GameWorld();
    private final EntityFactory entityFactory = new EntityFactory();
    private final Entity player;
    private final HUD hud;

    private final MapRenderSystem mapRenderSystem;
    private final DecorationRenderSystem decorationRenderSystem;
    private final BuildingRenderSystem buildingRenderSystem;
    private final GooseRenderSystem gooseRenderSystem;
    private final PlayerRenderSystem playerRenderSystem;

    private final SpriteBatch batch = new SpriteBatch();
    private final ShapeRenderer shapes = new ShapeRenderer();
    private final GlyphLayout layout = new GlyphLayout();
    private final BitmapFont textFont = Fonts.create(20);
    private final BitmapFont smallFont = Fonts.create(14);

    private Step step = Step.MOVE;
    private float praiseTimer = 0f;

    // For the MOVE task: where the player was last frame, and how far they have walked.
    private float lastX;
    private float lastY;
    private float distanceWalked = 0f;

    // The practice goose. It is null until the STRESS task begins.
    private Entity goose;

    public TutorialScreen(GoosePatrolGame game) {
        this.game = game;

        // The same world as the real game, but with no goose spawner and no way to lose.
        mapRenderSystem = new MapRenderSystem();
        gameWorld.addSystem(mapRenderSystem);

        decorationRenderSystem = new DecorationRenderSystem();
        gameWorld.addSystem(decorationRenderSystem);

        buildingRenderSystem = new BuildingRenderSystem();
        gameWorld.addSystem(buildingRenderSystem);

        gameWorld.addSystem(new PlayerInputSystem());
        gameWorld.addSystem(new GooseBehaviourSystem(session));
        gameWorld.addSystem(new MovementSystem());
        gameWorld.addSystem(new CampusCollisionSystem());
        gameWorld.addSystem(new BoundarySystem());

        gooseRenderSystem = new GooseRenderSystem();
        gameWorld.addSystem(gooseRenderSystem);

        player = PlayerFactory.createPlayer(entityFactory);
        gameWorld.addEntity(player);

        playerRenderSystem = new PlayerRenderSystem();
        gameWorld.addSystem(playerRenderSystem);

        hud = new HUD();

        // The practice session counts as running, so the HUD shows no pause message.
        session.togglePause();

        PositionComponent position = player.getComponent(PositionComponent.class);
        lastX = position.getX();
        lastY = position.getY();
    }

    @Override
    public void render(float delta) {
        // Esc skips the tutorial, and Enter starts the game once every task is done.
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)
            || (step == Step.DONE && Gdx.input.isKeyJustPressed(Input.Keys.ENTER))) {
            game.showScreen(ScreenType.GAME);
            return;
        }

        ScreenUtils.clear(0.15f, 0.15f, 0.2f, 1f);

        PositionComponent position = player.getComponent(PositionComponent.class);

        // The item keys work exactly as they do in the real game.
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_1)) {
            session.getInventory().use(0, gameWorld, position.getX(), position.getY());
        }
        if (Gdx.input.isKeyJustPressed(Input.Keys.NUM_2)) {
            session.getInventory().use(1, gameWorld, position.getX(), position.getY());
        }

        // This is only practice, so items never run out.
        for (Item item : session.getInventory().getItems()) {
            if (item.getUsesLeft() == 0) {
                session.getInventory().reset();
                break;
            }
        }

        session.getStress().update(delta);
        addPracticeStress(position, delta);

        gameWorld.update(delta);

        updateStep(position, delta);

        hud.draw(session);
        drawPanel();
    }

    /** Check whether the current task has been done, and move on when it has. */
    private void updateStep(PositionComponent position, float delta) {

        // While "Nice!" is showing, wait, then move on to the next task.
        if (praiseTimer > 0f) {
            praiseTimer -= delta;
            if (praiseTimer <= 0f) {
                step = Step.values()[step.ordinal() + 1];
                if (step == Step.STRESS) {
                    spawnGoose(position);
                }
            }
            return;
        }

        boolean done = false;

        switch (step) {
            case MOVE:
                float dx = position.getX() - lastX;
                float dy = position.getY() - lastY;
                distanceWalked += (float) Math.sqrt(dx * dx + dy * dy);
                lastX = position.getX();
                lastY = position.getY();
                done = distanceWalked >= MOVE_DISTANCE;
                break;

            case STRESS:
                // Also done if the player has already fed the goose before it reached them.
                done = session.getStress().getFraction() >= STRESS_TO_SHOW
                    || goose.getComponent(GooseComponent.class).getState() != GooseState.CHASING;
                break;

            case BREAD:
                // Done once the bread has reached the goose and it has stopped chasing.
                done = goose.getComponent(GooseComponent.class).getState() != GooseState.CHASING;
                break;

            case UMBRELLA:
                done = session.getStress().isShielded();
                break;

            case PAUSE:
                done = Gdx.input.isKeyJustPressed(Input.Keys.P);
                break;

            default:
                break;
        }

        if (done) {
            praiseTimer = PRAISE_SECONDS;
        }
    }

    /** Bring in one slow goose a short walk away from the player. */
    private void spawnGoose(PositionComponent position) {
        float width = Gdx.graphics.getWidth();

        // It appears on whichever side of the player has more room.
        float x = position.getX() < width / 2f
            ? position.getX() + GOOSE_START_DISTANCE
            : position.getX() - GOOSE_START_DISTANCE;

        goose = entityFactory.createEntity();
        goose.addComponent(new GooseComponent(GOOSE_HUNGER, GOOSE_SPEED));
        goose.addComponent(new PositionComponent(x, position.getY()));
        goose.addComponent(new VelocityComponent(0, 0));
        gameWorld.addEntity(goose);
    }

    /** A gentle version of the stress rule, capped so that the tutorial cannot be lost. */
    private void addPracticeStress(PositionComponent position, float delta) {
        if (goose == null) {
            return;
        }

        StressMeter stress = session.getStress();
        if (stress.getFraction() >= STRESS_CAP) {
            return;
        }

        if (goose.getComponent(GooseComponent.class).getState() != GooseState.CHASING) {
            return;
        }

        PositionComponent goosePosition = goose.getComponent(PositionComponent.class);
        float dx = goosePosition.getX() - position.getX();
        float dy = goosePosition.getY() - position.getY();

        if (dx * dx + dy * dy <= GOOSE_REACH * GOOSE_REACH) {
            stress.add(STRESS_PER_SECOND * delta);
        }
    }

    /** The two lines of instruction for the current task. */
    private String[] instructions() {
        switch (step) {
            case MOVE:
                return new String[] {"Walk around with  W A S D", "or the arrow keys."};
            case STRESS:
                return new String[] {"A goose! Let it reach you", "and watch your stress bar rise."};
            case BREAD:
                return new String[] {"Press 1 to throw bread.", "Fed geese stop chasing and wander off."};
            case UMBRELLA:
                return new String[] {"Press 2 to open your umbrella.", "It blocks stress for a few seconds."};
            case PAUSE:
                return new String[] {"Need a break?", "Press P to pause at any time."};
            default:
                return new String[] {"You're ready!", "Press ENTER to start the game."};
        }
    }

    /** Draw the instruction panel across the upper part of the screen. */
    private void drawPanel() {
        float width = Gdx.graphics.getWidth();
        float height = Gdx.graphics.getHeight();

        float panelX = PANEL_MARGIN;
        float panelWidth = width - 2 * PANEL_MARGIN;
        float panelY = height * 0.62f;

        // Blending lets the map show faintly through the panel.
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapes.begin(ShapeRenderer.ShapeType.Filled);
        shapes.setColor(PANEL_COLOR);
        shapes.rect(panelX, panelY, panelWidth, PANEL_HEIGHT);
        shapes.end();

        batch.begin();

        // A small header: which task this is, and how to skip.
        int number = Math.min(step.ordinal() + 1, Step.values().length - 1);
        String header = step == Step.DONE
            ? "Tutorial complete"
            : "Tutorial  -  task " + number + " of " + (Step.values().length - 1);
        smallFont.draw(batch, header, panelX + 12f, panelY + PANEL_HEIGHT - 10f);

        layout.setText(smallFont, "Esc: skip");
        smallFont.draw(batch, layout,
            panelX + panelWidth - 12f - layout.width, panelY + PANEL_HEIGHT - 10f);

        if (praiseTimer > 0f) {
            textFont.setColor(PRAISE_COLOR);
            drawCentred("Nice!", panelY + PANEL_HEIGHT * 0.50f);
            textFont.setColor(Color.WHITE);
        } else {
            String[] lines = instructions();
            drawCentred(lines[0], panelY + PANEL_HEIGHT * 0.62f);
            drawCentred(lines[1], panelY + PANEL_HEIGHT * 0.62f - textFont.getLineHeight());
        }

        batch.end();
    }

    /** Draw one line of instruction text in the middle of the screen, at the given height. */
    private void drawCentred(String text, float y) {
        layout.setText(textFont, text);
        textFont.draw(batch, layout, (Gdx.graphics.getWidth() - layout.width) / 2f, y);
    }

    @Override
    public void dispose() {
        playerRenderSystem.dispose();
        gooseRenderSystem.dispose();
        mapRenderSystem.dispose();
        buildingRenderSystem.dispose();
        decorationRenderSystem.dispose();
        hud.dispose();
        batch.dispose();
        shapes.dispose();
        textFont.dispose();
        smallFont.dispose();
    }
}
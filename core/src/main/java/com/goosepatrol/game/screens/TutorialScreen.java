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

/*A practice roiund on the real map, the player is given one task at a time and each task is ticked off when they actually do it */
public class TutorialScreen extends ScreenAdapter
{
    /*the tasks and the order in which the plyer meets them */
    private enum Step { MOVE, STRESS, BREAD, UMBRELLA, PAUSE, DONE }
}
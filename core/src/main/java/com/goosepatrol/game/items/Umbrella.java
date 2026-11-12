package com.goosepatrol.game.items;

import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.session.StressMeter;

/** acts as a shield, for a few seconds the geese cannot raise the player's stress */
public class Umbrella extends Item 
{

    private static final int USES = 2;
    private static final float SHIELD_SECONDS = 5f;

    private final StressMeter stress;

    public Umbrella(StressMeter stress) 
    {
        super("Umbrella", USES);
        this.stress = stress;
    }

    @Override
    protected void apply(GameWorld world, float x, float y)
    {
        stress.shield(SHIELD_SECONDS);
    }

    /** the umbrella counts as active for as long as the shield is up */
    @Override
    public boolean isActive() 
    {
        return stress.isShielded();
    }
}
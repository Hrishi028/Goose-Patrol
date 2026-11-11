package com.goosepatrol.game.session;

/*the players stress bar starts full and the game is lost when its empty */
public class StressMeter 
{
    public static final float MAX_LEVEL = 100f;

    //after some seconds without being attacked the bar recovers a little
    private static final float HEAL_DELAY_SECONDS = 12F;
    private static final float HEAL_AMOUNT = 5f;

    private float level = MAX_LEVEL;
    private float secondsSinceDrop=0f;

    /*calling every frame while the game is running */
    public void update(float delta)
    {
        secondsSinceDrop += delta;

        if(secondsSinceDrop >= HEAL_DELAY_SECONDS)
        {
            level = Math.min(MAX_LEVEL, level + HEAL_AMOUNT);
            secondsSinceDrop=0f;
        }
    }

    /* lowering the bar by the given amount and it never goes below zero */
    public void drop(float amount)
    {
        level = Math.max(0f, level-amount);
        secondsSinceDrop=0f;
    }
    public boolean isEmpty()
    {
        return level <= 0f;
    }
    public float getLevel()
    {
        return level;
    }

    /*how full the bar is, from 0 (empty) to 1 (full) */
    public float getFraction()
    {
        return level/MAX_LEVEL;
    }
}

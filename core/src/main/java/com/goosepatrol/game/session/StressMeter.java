package com.goosepatrol.game.session;

/*the stressbar starts empty and the game is lost if its full */
public class StressMeter
{
    public static final float MAX_LEVEL=100f;

    //after this many seconds without stress ebing added the bar starts to drain
    private static final float RECOVER_DELAY_SECONDS=2F;
    //HOW MUCH THE BAR DRAINS EACH SECOND ONCE IT HAS STARTED
    private static final float RECOVER_PER_SECOND=4F;

    private float level=0f;
    private float secondsSinceRise=0f;

    /*calling every frame while the game is running */
    public void update(float delta)
    {
        secondsSinceRise+=delta;

        if(secondsSinceRise>=RECOVER_DELAY_SECONDS)
        {
            level = Math.max(0f, level-RECOVER_PER_SECOND*delta);
        }
    }

    /*raising the bar by the given amount...it never goes above the max limit */
    public void add(float amount)
    {
        level =Math.min(MAX_LEVEL, level+amount);
        secondsSinceRise=0f;

    }

    /*emptying the bar for a new game */
    public void reset()
    {
        level=0f;
        secondsSinceRise=0f;

    }
    public boolean isFull()
    {
        return level>=MAX_LEVEL;

    }
    public float getLevel()
    {
        return level;
    }
    /*how full the bar is, from 0(empty) to 1(full) */
    public float getFraction()
    {
        return level/MAX_LEVEL;
    }
}
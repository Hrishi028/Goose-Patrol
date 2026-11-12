package com.goosepatrol.game.items;

import com.goosepatrol.game.ecs.GameWorld;

/*something a player carries and can use a limited number of times */
public abstract class Item
{
   private final String name;
    private final int startingUses;
    private int usesLeft;

    protected Item(String name, int startingUses)
    {
        this.name=name;
        this.startingUses=startingUses;
        this.usesLeft=startingUses;
    }

    /* using the item once at a given position  */
   /* returning true if it was used and false if there were no uses left*/
   public boolean use(GameWorld world, float x, float y)
   {
    if (usesLeft<=0)
    {
        return false;
    }
    usesLeft--;
    apply(world, x,y);
    return true;

   }

   /*what this particular item does, each kind of item fills this in */
   protected abstract void apply(GameWorld world, float x, float y);

   /*true while the item's effect is still going on */
   public boolean isActive()
   {
    return false;
   }

   /*putting the uses back to the starting number for a new game */
   public void reset()
   {
    usesLeft= startingUses;
   }

   public String getName()
   {
    return name;
   }

   public int getUsesLeft()
   {
    return usesLeft;
   }
}
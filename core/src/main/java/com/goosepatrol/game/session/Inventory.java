package com.goosepatrol.game.session;

import com.goosepatrol.game.ecs.GameWorld;
import com.goosepatrol.game.items.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*the items the player is carrying, slot 0 is the first item , slot 1 the second and so on */
public class Inventory
{
    private final List<Item> items = new ArrayList<>();

    public void add(Item item)
    {
        items.add(item);

    }

    /*using the item in the given slot and returning true if the item was actually used*/
    public boolean use(int slot, GameWorld world, float x, float y)
    {
        if (slot <0 || slot >= items.size())
        {
            return false;
        }
        return items.get(slot).use(world, x,y);

    }

    /* refill every item for anew game */
    public void reset()
    {
        for(Item item:items)
        {
            item.reset();
        }
    }
     public List<Item> getItems() {
        return Collections.unmodifiableList(items);



}
}
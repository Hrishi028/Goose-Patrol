package com.goosepatrol.game.session;

import com.goosepatrol.game.items.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/*keeping count of what happened in one game */
public class StatsTracker implements GameListener
{
    //how many times each item was used by item name
    private final Map<String, Integer> usesByItem = new LinkedHashMap<>();
    //the names of the hidden events that were triggered in order
    private final List<String> eventsTriggered = new ArrayList<>();

    private int itemsUsed = 0;

    @Override
    public void onItemUsed(Item item)
    {
        itemsUsed++;
        usesByItem.merge(item.getName(), 1, Integer::sum);
    }

    @Override
    public void onEventTriggered(String eventName)
    {
        eventsTriggered.add(eventName);
    }

    /*the total number of items used*/
    public int getItemsUsed()
    {
        return itemsUsed;
    }

    /** how many times the item with this name was used */
    public int getTimesUsed(String itemName)
    {
        return usesByItem.getOrDefault(itemName, 0);
    }

    public List<String> getEventsTriggered()
    {
        return Collections.unmodifiableList(eventsTriggered);
    }

    /* clearing every count for a new game */
    public void reset()
    {
        itemsUsed = 0;
        usesByItem.clear();
        eventsTriggered.clear();
    }
}
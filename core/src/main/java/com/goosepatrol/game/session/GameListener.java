package com.goosepatrol.game.session;
import com.goosepatrol.game.items.Item;

/*anything that wants to be told when something happens in the game */
public interface GameListener 
{
    /*called each time the player successfully uses an item */
    void onItemUsed(Item item);

    /*called each time a hidden event is triggered */
    void onEventTriggered(String eventName);
    


    
}

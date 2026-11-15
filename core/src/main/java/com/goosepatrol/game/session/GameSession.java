package com.goosepatrol.game.session;
import com.goosepatrol.game.items.Bread;
import com.goosepatrol.game.items.Umbrella;
import com.goosepatrol.game.items.Item;
import com.goosepatrol.game.ecs.GameWorld;
import java.util.ArrayList;
import java.util.List;
/*holds the rules of one playthrough, its state and its timer */
public class GameSession 
{
  private static final float GAME_LENGTH_SECONDS = 5*60f;
  private static final int DISPERSAL_TARGET = 10;
  
  private GameState state= GameState.PAUSED;
  private final GameTimer timer = new GameTimer(GAME_LENGTH_SECONDS);
  private final StressMeter stress= new StressMeter();
  
  private int geeseDispersed = 0;
  private final Inventory inventory = new Inventory();
  private final StatsTracker stats = new StatsTracker();
  private final List<GameListener> listeners = new ArrayList<>();
  public GameSession()
  {
    inventory.add(new Bread());
    
    inventory.add(new Umbrella(stress));


    listeners.add(stats);
  }

  /*called once every frame */
  public void update(float delta)
  {
    if (!isRunning())
    {
        return;
    }
    timer.tick(delta);
    stress.update(delta);
    checkEndConditions();
  }

  /*swticthing between paused and running, does nothing when the game has ended */
  public void togglePause()
  {
    if(state==GameState.PAUSED)
    {
        state= GameState.RUNNING;
    }
    else if(state==GameState.RUNNING)
    {
        state= GameState.PAUSED;
    }
  }
  public void gooseDispersed() 
  {
        if (!isRunning())
        {
           return;
        }
        geeseDispersed++;
        checkEndConditions();
  }

   public int getGeeseDispersed()
    {
        return geeseDispersed;
    }

    public int getDispersalTarget() 
    {
        return DISPERSAL_TARGET;
    }
    
  
  /*starts a fresh game and it begins paused same the first time */
  public void restart()
  {
    state=GameState.PAUSED;
    timer.reset();
    stress.reset();
    geeseDispersed=0;
    inventory.reset();
    stats.reset();
  }
  /*true once the game is finished regardless of the outcome */
  public boolean hasEnded()
  {
    return state == GameState.WON || state ==GameState.TIMED_OUT || state== GameState.STRESSED_OUT;
  }

  public boolean isRunning()
  {
    return state == GameState.RUNNING;
  }

  private void checkEndConditions()
  {
    if (stress.isFull())
    {
        state=GameState.STRESSED_OUT;
    }
    else if(geeseDispersed>=DISPERSAL_TARGET)
    {
      state= GameState.WON;
    }
    else if(timer.isFinished())
    {
        state= GameState.TIMED_OUT;
    }
  }

  public GameState getState()
    {
        return state;
    }

  public GameTimer getTimer()
  {
    return timer;
  }
    /*using the item in the given slot and telling every listener if it worked */
  public boolean useItem(int slot, GameWorld world, float x, float y)
  {
    if (!inventory.use(slot, world, x, y))
    {
        return false;
    }
    Item item = inventory.getItems().get(slot);
    for (GameListener listener : listeners)
    {
        listener.onItemUsed(item);
    }
    return true;
  }

  /*called by a hidden event when it is triggered */
  public void eventTriggered(String eventName)
  {
    for (GameListener listener : listeners)
    {
        listener.onEventTriggered(eventName);
    }
  }

  public void addListener(GameListener listener)
  {
    listeners.add(listener);
  }

  public StatsTracker getStats()
  {
    return stats;
  }
  public Inventory getInventory()
  {
    return inventory;
  }
  public StressMeter getStress()
  {
    return stress;
  }
  

}

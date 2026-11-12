package com.goosepatrol.game.session;
import com.goosepatrol.game.items.Bread;

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

  public GameSession()
  {
    inventory.add(new Bread());
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
  public Inventory getInventory()
  {
    return inventory;
  }
  public StressMeter getStress()
  {
    return stress;
  }
  

}

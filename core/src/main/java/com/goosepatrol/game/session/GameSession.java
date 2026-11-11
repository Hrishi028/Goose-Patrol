package com.goosepatrol.game.session;

/*holds the rules of one playthrough, its state and its timer */
public class GameSession 
{
  private static final float GAME_LENGTH_SECONDS = 5*60f;
  
  private GameState state= GameState.PAUSED;
  private final GameTimer timer = new GameTimer(GAME_LENGTH_SECONDS);

  /*called once every frame */
  public void update(float delta)
  {
    if (!isRunning())
    {
        return;
    }
    timer.tick(delta);
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

  public boolean isRunning()
  {
    return state == GameState.RUNNING;
  }

  private void checkEndConditions()
  {
    if (timer.isFinished())
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
  

}

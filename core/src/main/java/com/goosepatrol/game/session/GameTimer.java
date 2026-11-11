package com.goosepatrol.game.session;

/*counts down from a starting number of seconds to zero */
public class GameTimer 
{
   private float secondsLeft;
   
   public GameTimer(float startSeconds)
   {
    this.secondsLeft= startSeconds;
   }

   /*taking the time since the last frame off the clock */

   public void tick(float delta)
   {
    secondsLeft = Math.max(0f, secondsLeft-delta);
   }

   public boolean isFinished()
   {
    return secondsLeft<=0f;
   }

   public float getSecondsLeft()
   {
    return secondsLeft;
   }

   /*the time left as text */
   public String asText()
   {
    int total = (int) Math.ceil(secondsLeft);
    int minutes= total/60;
    int seconds= total% 60;
    return minutes+":"+ (seconds<10 ? "0" : "") + seconds;
   }
}

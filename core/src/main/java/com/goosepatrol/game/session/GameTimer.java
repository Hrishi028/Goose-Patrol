package com.goosepatrol.game.session;

/*counts down from a starting number of seconds to zero */
public class GameTimer 
{
   private float secondsLeft;
   private final float startSeconds;

   public GameTimer(float startSeconds)
   {
    this.secondsLeft= startSeconds;
    this.startSeconds=startSeconds;
   }

   /*taking the time since the last frame off the clock */

   public void tick(float delta)
   {
    secondsLeft = Math.max(0f, secondsLeft-delta);
   }
   /*putting the clock back to its starting time */
   public void reset()
   {
      secondsLeft=startSeconds;
   }

   public boolean isFinished()
   {
    return secondsLeft<=0f;
   }
   public float getElapsedSeconds()
   {
      return startSeconds - secondsLeft;
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

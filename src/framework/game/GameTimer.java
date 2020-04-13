package Framework.game;

public class GameTimer implements Runnable {

    public String gameTime;
    private int gameTimeSec;

    public GameTimer() {
        this.gameTimeSec = 0;
        this.gameTime = "Timer has not started yet" ;
    }

    @Override
    public void run() {
        while (true) {
            try {
                gameTimeSec += 1;
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }


    /**
     * This show the game time.
     */
    public synchronized String getGameTime() {
        String outputTime = "";
        int secs = (int) (gameTimeSec % 60);
        int mins = (int) (gameTimeSec / 60);
        if (secs < 10) outputTime = mins + ":0" + secs;
        else outputTime = mins + ":" + secs;
        return outputTime;
    }


    public void resetTimer() {
        this.gameTimeSec = 0;
    }
}

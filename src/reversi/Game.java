package reversi;

import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.effect.Light;
import reversi.controllers.GameController;

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class Game implements Runnable{

    private ServerConnection sc;
    private GameController gc;

    private boolean gameFound;
    public Player user;
    private int opp;

    private long gameStartTime;
    private long gameTimeSec;

    private Board board;
    private String[] endAnswers = {"GAME WIN","GAME LOSS","GAME DRAW","ERR NOT"};

    public Game(GameController gc, ServerConnection sc){
        this.gc = gc;
        this.gameFound = false;
        this.sc = sc;
        this.gameStartTime = 0;
        this.gameTimeSec = 0;
    }

    @Override
    public void run() {
        while (true){
            try {
                long start = System.nanoTime();
                Thread.sleep(700);
                if(!gameFound) {
                    searchingEnemy();
                    continue;
                }
                else{
                    gameTime();
                    gameRunning();
                    showPlayerTurn();
//                    checkForFinish();
                }
                long stop = System.nanoTime();
                System.out.println("It tooks "+(stop-start)+" ns");
            }catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * This method will rewind if gameFound = false.
     * It will look into the last responses from the server if there is match with the word "MATCH".
     * If there is a match, it will create a new Board object and starts the game.
     */
    private void searchingEnemy() {
        Platform.runLater(()->{
            String matchResponse = sc.lastRespContains("MATCH");
            if (matchResponse != null){
                Map<String,String> tmp = sc.getMap(matchResponse);
                System.out.println(tmp.toString());

                if (tmp.get("PLAYERTOMOVE").equals(tmp.get("OPPONENT"))){
                    gc.lblPlayer1.setText(tmp.get("OPPONENT"));
                    gc.lblPlayer2.setText(sc.getLoginName());
                    user = new Player(Settings.Player2Color,Settings.PLAYER2);
                    opp = Settings.PLAYER1;
                } else {
                    gc.lblPlayer2.setText(tmp.get("OPPONENT"));
                    gc.lblPlayer1.setText(sc.getLoginName());
                    user = new Player(Settings.Player1Color,Settings.PLAYER1);
                    opp = Settings.PLAYER2;
                    user.setPlayersTurn(true);
                }
                gc.lblStatus.setText("Status: In Game(0:00)");
                this.gameStartTime = System.nanoTime();
                gameFound = true;

                board = new Board(gc.gameTable,this,sc);
                return;
            }
            System.out.println(gc.lblStatus.getText());
            if (gc.lblStatus.getText().equals("Status: Searching Oppenent...")){
                gc.lblStatus.setText("Status: Searching Oppenent");
            } else {
                gc.lblStatus.setText(gc.lblStatus.getText()+".");
            }
            gameFound = false;
        });
    }

    /**
     * This function wait for a response from the server if it's not the players turn.
     * If there is a "MOVE" response from server, it will do the move in the GUI.
     */
    private void gameRunning(){
        Platform.runLater(()->{
            if (!user.isPlayersTurn()){
                String moveResponse = sc.lastRespContains("GAME MOVE");
                if (moveResponse!= null){
//                    System.out.println(moveResponse);
                    Map<String,String> tmp = sc.getMap(moveResponse);
                    if (!tmp.get("PLAYER").equals(sc.getLoginName())) {
                        System.out.println(tmp.get("MOVE"));
                        int[] xy = getMoveParameterEnemy(Integer.parseInt(tmp.get("MOVE")));
                        Point point = new Point(xy[0], xy[1]);
                        board.setMove(point, opp, false, false);
                        user.setPlayersTurn(true);
                    }
                }

            }
        });
    }

    public void checkForFinish(){
        Platform.runLater(()->{
            String endResponse = null;
            for (String end : endAnswers){
                if (endResponse==null) endResponse = sc.lastRespContains(end);
            }
            if (endResponse != null) {
                endGame();
            }
        });
    }

    /**
     * This show the game time.
     */
    private void gameTime(){
        Platform.runLater(()->{
            long actTime = System.nanoTime();
            gameTimeSec = (long) ((actTime-gameStartTime)/1e+9);
            String outputTime = "";
            int secs = (int) (gameTimeSec%60);
            int mins = (int) (gameTimeSec/60);
            if (secs<10) outputTime = mins + ":0" + secs;
            else outputTime = mins + ":" + secs;
            gc.lblStatus.setText("Status: In Game("+outputTime+")");
        });
    }

    /**
     * this will calculate the input into this [X,Y]
     * @param move Integer(0-63) cell in the board.
     * @return [X,Y] coordinations
     */
    public int[] getMoveParameterEnemy(int move){
        int rowIndex = (int) move / Settings.tilesY;
        int colIndex = move % Settings.tilesX;
        int[] output = {colIndex, rowIndex};
        return output;
    }

    @FXML
    public void showPlayerScore() {
        int scoreP1 = 0;
        int scoreP2 = 0;
        Iterator it = Settings.SpawnPoints.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            int currentValue = (int) pair.getValue();
            if(currentValue == 1){
                scoreP1 +=1;
            }else
                scoreP2 +=1;
        }
        System.out.println(scoreP1 + "  " + scoreP2);
        gc.scorep1.setText(String.valueOf(scoreP1));
        gc.scorep2.setText(String.valueOf(scoreP2));
    }


    /**
     * It will shows who's turn it is.
     */
    public void showPlayerTurn(){
        Platform.runLater(()->{
            if (gc.lblPlayer1.getText().contains(sc.getLoginName())){
                if (user.isPlayersTurn()) turnToPlayerOne(true);
                else turnToPlayerOne(false);
            } else {
                if (user.isPlayersTurn()) turnToPlayerOne(false);
                else turnToPlayerOne(true);
            }
        });
    }

    public void endGame(){
        board = null;
        gc.gameTable.getChildren().clear();
        gc.lblStatus.setText("Status: Searching Oppenent");
        gameFound = false;
        gc.lblPlayer1.setText("Player ONE");
        gc.lblPlayer2.setText("Player TWO");
    }

    public void turnToPlayerOne(boolean bool){
        gc.lblPlayer1.setUnderline(bool);
        gc.lblPlayer2.setUnderline(!bool);
    }
}

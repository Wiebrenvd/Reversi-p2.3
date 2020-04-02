package reversi;

import framework.GameTimer;
import framework.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import reversi.controllers.GameController;

import java.awt.*;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

public class Game implements Runnable {

    public ServerConnection sc;
    private GameController gc;

    private GameTimer gameTimer;

    private boolean gameFound;
    public ReversiPlayer user;
    private ReversiPlayer opp;

    private Board board;
    private String[] endAnswers = {"GAME WIN", "GAME LOSS", "GAME DRAW", "ERR NOT"};
    private ArrayList<ReversiPlayer> players = new ArrayList<>(); // Kan ook een hashmap worden (met player.id als key ipv in player klasse), of een andere oplossing

    public Game(GameController gc, ServerConnection sc) {
        this.gc = gc;
        this.gameFound = false;
        this.sc = sc;
        this.gameTimer = new GameTimer();
    }

    @Override
    public void run() {
        while (true) {
            try {
                Thread.sleep(700);
                if (!gameFound) {
                    searchingEnemy();
                    continue;
                } else {
                    Platform.runLater(() -> {
                        gc.setStatus(gameTimer.getGameTime());
                    });
                    gameRunning();
                    showPlayerTurn();
//                    checkForFinish();
                }
            } catch (InterruptedException e) {
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
        Platform.runLater(() -> {
            String matchResponse = sc.lastRespContains("MATCH");
            if (matchResponse != null) {
                Map<String, String> tmp = sc.getMap(matchResponse);
                System.out.println(tmp.toString());

                if (tmp.get("PLAYERTOMOVE").equals(tmp.get("OPPONENT"))) {
                    gc.getLblPlayer1().setText(tmp.get("OPPONENT"));
                    gc.getLblPlayer2().setText(sc.getLoginName());
                    user = new ReversiPlayer(Settings.PLAYER2, sc.getLoginName(), Settings.Player2Color);
                    opp = new ReversiPlayer(Settings.PLAYER1, tmp.get("OPPONENT"), Settings.Player1Color);
                    opp.setPlayersTurn(true);
                } else {
                    gc.getLblPlayer2().setText(tmp.get("OPPONENT"));
                    gc.getLblPlayer1().setText(sc.getLoginName());
                    user = new ReversiPlayer(Settings.PLAYER1, sc.getLoginName(), Settings.Player1Color);
                    opp = new ReversiPlayer(Settings.PLAYER2, tmp.get("OPPONENT"), Settings.Player2Color);
                    user.setPlayersTurn(true);
                }
                players.add(user); // Voegt de 2 spelers toe aan speler lijst
                players.add(opp);

                gameFound = true;

                startTimer();

                board = new Board(gc.gameTable, this, players);
                return;
            }

//            if (gc.getStatus().equals("Status: Searching Opponent...")){ // Dit glitchte beetje
//                gc.setStatus("Status: Searching Opponent");
//            } else {
//                gc.setStatus(gc.getStatus()+".");
//            }
            gameFound = false;
        });
    }

    @FXML
    public void showPlayerScore() {
        int scoreP1 = 0;
        int scoreP2 = 0;
        Cell[][] grid = board.grid;
        for(int i =0;i<grid.length-1;i++){
            for(int j =0;j<grid[i].length-1;j++) {
                System.out.println(grid[i][j].getPlayer());
                if(grid[i][j].getPlayer() != null) {
                    if (grid[i][j].getPlayer().equals(user)) {
                        scoreP1 += 1;
                    } else {
                        scoreP2 += 1;
                    }
                }
            }
        }
        System.out.println(scoreP1 + "  " + scoreP2);
        gc.scorep1.setText(String.valueOf(scoreP1));
        gc.scorep2.setText(String.valueOf(scoreP2));
    }

    /**
     * This function wait for a response from the server if it's not the players turn.
     * If there is a "MOVE" response from server, it will do the move in the GUI.
     */
    private void gameRunning() {
        Platform.runLater(() -> {
            if (!user.isPlayersTurn()) {
                String moveResponse = sc.lastRespContains("GAME MOVE");
                if (moveResponse != null) {
//                    System.out.println(moveResponse);
                    Map<String, String> tmp = sc.getMap(moveResponse);
                    if (!tmp.get("PLAYER").equals(sc.getLoginName())) {
                        System.out.println(tmp.get("MOVE"));
                        int[] xy = getMoveParameterEnemy(Integer.parseInt(tmp.get("MOVE")));
                        Point point = new Point(xy[0], xy[1]);
                        board.setMove(point, opp, false, false);
                        showPlayerScore();
                        opp.setPlayersTurn(false);
                        user.setPlayersTurn(true);
                    }
                }

            }
        });
    }

    public void checkForFinish() {
        Platform.runLater(() -> {
            String endResponse = null;
            for (String end : endAnswers) {
                if (endResponse == null) endResponse = sc.lastRespContains(end);
            }
            if (endResponse != null) {
                endGame();
            }
        });
    }


    /**
     * this will calculate the input into this [X,Y]
     *
     * @param move Integer(0-63) cell in the board.
     * @return [X, Y] coordinations
     */
    public int[] getMoveParameterEnemy(int move) {
        int rowIndex = (int) move / Settings.tilesY;
        int colIndex = move % Settings.tilesX;
        int[] output = {colIndex, rowIndex};
        return output;
    }

    /**
     * It will shows who's turn it is.
     */
    public void showPlayerTurn() {
        Platform.runLater(() -> {
            if (gc.getLblPlayer1().getText().contains(sc.getLoginName())) {
                if (user.isPlayersTurn()) gc.turnToPlayerOne(true);
                else gc.turnToPlayerOne(false);
            } else {
                if (user.isPlayersTurn()) gc.turnToPlayerOne(false);
                else gc.turnToPlayerOne(true);
            }
        });
    }

    public void endGame() {
        board = null;
        gc.gameTable.getChildren().clear();
        gc.setStatus("Status: Searching Opponent");
        gameFound = false;
        gc.setStatus("ReversiPlayer ONE");
        gc.setStatus("ReversiPlayer TWO");
    }



    public void startTimer() {
        new Thread(gameTimer).start();
        startGameScore();
    }
    public void startGameScore(){
        gc.scorep1.setText("2");
        gc.scorep2.setText("2");
    }

    // Overbodig als arraylist Players een hashmap wordt.
    public ReversiPlayer getPlayerById(int id) {
        Iterator<ReversiPlayer> it = players.iterator();
        while (it.hasNext()) {
            ReversiPlayer player = it.next();
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }

}

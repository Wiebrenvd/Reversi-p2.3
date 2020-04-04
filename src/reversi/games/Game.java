package reversi.games;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;

import framework.GameTimer;
import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import reversi.boards.Board;
import reversi.cells.Cell;
import reversi.boards.PlayerBoard;
import reversi.Settings;

import reversi.controllers.GameController;

public abstract class Game implements Runnable {

    public ServerConnection sc;
    public GameController gc;
    protected boolean gameFound;
    public Player user;
    public Player opp;

    protected Board board;
    private String[] endAnswers = {"GAME WIN", "GAME LOSS", "GAME DRAW", "ERR NOT"};
    protected ArrayList<Player> players = new ArrayList<>(); // Kan ook een hashmap worden (met player.id als key ipv in player klasse), of een andere oplossing

    public Game(GameController gc, ServerConnection sc) {
        this.gc = gc;
        this.gameFound = false;
        this.sc = sc;
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
    protected void gameRunning() {
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
                        board.setMove(point, (Player) opp, false, false);
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
        int rowIndex = (int) move / Settings.TILESY;
        int colIndex = move % Settings.TILESX;
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
        gc.setStatus("Player ONE");
        gc.setStatus("Player TWO");
    }




    public void startGameScore(){
        gc.scorep1.setText("2");
        gc.scorep2.setText("2");
    }

    protected abstract void searchingEnemy();

    // Overbodig als arraylist Players een hashmap wordt.
    public Player getPlayerById(int id) {
        Iterator<Player> it = players.iterator();
        while (it.hasNext()) {
            Player player = it.next();
            if (player.getId() == id) {
                return player;
            }
        }
        return null;
    }


    public ArrayList<Player> getPlayers() {
    return players;
    }
}

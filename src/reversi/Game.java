package reversi;

import framework.GameTimer;
import framework.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import reversi.controllers.GameController;

import java.awt.*;
import java.util.*;

public class Game implements Runnable {

    public ServerConnection sc;
    private GameController gc;

    private GameTimer gameTimer;
    private Thread gameTimerThread;

    private boolean gameFound;
    public ReversiPlayer user;
    private ReversiPlayer opp;

    private HashMap<ReversiPlayer,ArrayList<Point>> possibleMoves;

    private Board board;
    private String[] endAnswers = {"GAME WIN", "GAME LOSS", "GAME DRAW", "ERR NOT"};
    private ArrayList<ReversiPlayer> players = new ArrayList<>(); // Kan ook een hashmap worden (met player.id als key ipv in player klasse), of een andere oplossing

    private boolean running;

    public Game(GameController gc, ServerConnection sc) {
        this.gc = gc;
        this.gameFound = false;
        this.sc = sc;
        this.possibleMoves = new HashMap<>();
        this.running = true;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (!gameFound) {
                    searchingEnemy();
                } else {
                    Platform.runLater(() -> {
                        gc.setStatus("Status: In Game( "+gameTimer.getGameTime()+" ) Against "+opp.getName());
                    });
                    gameRunning();
                    showPlayerTurn();
                    try {
                        showPlayerScore();
                    }catch (NullPointerException e){
                        System.out.println("Can't read score..");
                    }
                    checkForFinish();
                }
            } catch (Exception e) {
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
        String matchResponse = sc.lastRespContains("MATCH");
        if (matchResponse != null) {
            Map<String, String> tmp = sc.getMap(matchResponse);
            System.out.println(tmp.toString());

            if (tmp.get("PLAYERTOMOVE").equals(tmp.get("OPPONENT"))) {
                Platform.runLater(() -> {
                    gc.getLblPlayer1().setText(tmp.get("OPPONENT"));
                    gc.getLblPlayer2().setText(sc.getLoginName());
                });
                user = new ReversiPlayer(Settings.PLAYER2, sc.getLoginName(), Settings.Player2Color);
                opp = new ReversiPlayer(Settings.PLAYER1, tmp.get("OPPONENT"), Settings.Player1Color);
                setTurnToUser(false);
            } else {
                Platform.runLater(() -> {
                    gc.getLblPlayer2().setText(tmp.get("OPPONENT"));
                    gc.getLblPlayer1().setText(sc.getLoginName());
                });
                user = new ReversiPlayer(Settings.PLAYER1, sc.getLoginName(), Settings.Player1Color);
                opp = new ReversiPlayer(Settings.PLAYER2, tmp.get("OPPONENT"), Settings.Player2Color);
                setTurnToUser(true);
            }
            players.add(user); // Voegt de 2 spelers toe aan speler lijst
            players.add(opp);

            gameFound = true;

            Platform.runLater(() -> {
                this.board = new Board(gc.gameTable, this, players);
                startTimer();
            });

            return;
        }

        Platform.runLater(() -> {
            String statusStr = "Status: Searching Opponent";
            if (gc.getStatus().length() >= statusStr.length()+3){
                gc.setStatus(statusStr);
            } else {
                gc.setStatus(gc.getStatus()+".");
            }
        });
        gameFound = false;

    }

    /**
     * this method switch turns on command.
     * @param isUserTurn boolean, if 'true', it gives the user his/her turn
     */
    public void setTurnToUser(boolean isUserTurn){
        user.setPlayersTurn(isUserTurn);
        opp.setPlayersTurn(!isUserTurn);
    }

    /**
     * This method will show the score of the Players.
     * It will also check if the player, who has the turn, can do a move.
     *
     * @throws NullPointerException when the board = null
     */
    @FXML
    public void showPlayerScore() throws NullPointerException {
        int scoreP1 = 0;
        int scoreP2 = 0;
        Cell[][] grid = board.grid;
        ArrayList<Point> movesUser = new ArrayList<>();
        ArrayList<Point> movesOpp = new ArrayList<>();
        for(int i =0;i<grid.length;i++){
            for(int j =0;j<grid[i].length;j++) {
                ReversiPlayer tmpPlayer = grid[i][j].getPlayer();
                if(tmpPlayer != null) {
                    if (tmpPlayer.equals(user)) {
                        scoreP1 += 1;
                    } else {
                        scoreP2 += 1;
                    }
                } else {
                    Point tmpPoint = new Point(i,j);
                    if (board.setMove(tmpPoint,user,true,false)) movesUser.add(tmpPoint);
                    if (board.setMove(tmpPoint,opp,true,false)) movesOpp.add(tmpPoint);
                }
            }
        }

        if (user.isPlayersTurn() && movesUser.size() == 0) setTurnToUser(false);
        if (opp.isPlayersTurn() && movesOpp.size() == 0) setTurnToUser(true);

        possibleMoves.put(user,movesUser);
        possibleMoves.put(opp,movesOpp);

        int finalScoreP2 = scoreP2;
        int finalScoreP1 = scoreP1;
        Platform.runLater(() -> {
            gc.scorep1.setText(String.valueOf(finalScoreP1));
            gc.scorep2.setText(String.valueOf(finalScoreP2));
        });
    }

    /**
     * This function wait for a response from the server if it's not the players turn.
     * If there is a "MOVE" response from server, it will do the move in the GUI.
     */
    private void gameRunning() {
        if (!user.isPlayersTurn()) {
            String moveResponse = sc.lastRespContains("GAME MOVE");
//            System.out.println(moveResponse);
            if (moveResponse != null) {
                Map<String, String> tmp = sc.getMap(moveResponse);
                if (!tmp.get("PLAYER").equals(sc.getLoginName())) {
//                    System.out.println(tmp.get("MOVE"));
                    int[] xy = getMoveParameterEnemy(Integer.parseInt(tmp.get("MOVE")));
                    Point point = new Point(xy[0], xy[1]);
                    Platform.runLater(() -> {
                        board.setMove(point, opp, false, false);
                    });

                    setTurnToUser(true);

                }
            }
        }
    }


    /**
     * This method will check if the server had finished a game.
     */
    public void checkForFinish() {
        String endResponse = sc.lastRespContains("SVR GAME");
        for (String end : endAnswers) {
            if (endResponse.contains(end)) endGame();
        }
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

    /*NOT FINISHED*/
    /**
     * This method will end the game
     */
    public void endGame() {
        this.gameTimerThread.stop();
        board = null;
        Platform.runLater(() -> {
            gc.gameTable.getChildren().clear();
            gc.gameTable = null;
            gc.gameTable = new GridPane();
            gc.setStatus("Status: Searching Opponent...");
            gc.getLblPlayer1().setText("ReversiPlayer ONE");
            gc.getLblPlayer2().setText("ReversiPlayer TWO");
            gc.scorep1.setText("0");
            gc.scorep2.setText("0");
            gc.btnForfeit.setText("Search New Game");
        });
        players.removeAll(players);
        gameFound = false;
        running = false;

    }

    /**
     * This method will start the Timer.
     */
    public void startTimer() {
        this.gameTimer = null;
        this.gameTimer = new GameTimer();
        this.gameTimerThread = new Thread(this.gameTimer);
        this.gameTimerThread.start();

        startGameScore();
    }

    /**
     * This method start the scores of the players
     */
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

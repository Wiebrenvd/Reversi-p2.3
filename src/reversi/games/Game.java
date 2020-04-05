package reversi.games;

import framework.GameTimer;
import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.layout.GridPane;
import reversi.Settings;
import reversi.boards.Board;
import reversi.cells.Cell;
import reversi.controllers.GameController;
import reversi.players.HardAIPlayer;
import reversi.players.OfflinePlayer;
import reversi.players.OnlinePlayer;
import reversi.players.EasyAIPlayer;

import java.awt.*;
import java.util.*;

public class Game implements Runnable {

    public ServerConnection sc;
    private GameController gc;

    private GameTimer gameTimer;
    private Thread gameTimerThread;

    private boolean startGame;
    public Player user;
    private Player opp;
    public boolean doingTurn;
    public Point tmpPoint;


    private HashMap<Player,ArrayList<Point>> possibleMoves;

    private Board board;
    private String[] endAnswers = {"GAME WIN", "GAME LOSS", "GAME DRAW", "ERR NOT"};
    private ArrayList<Player> players = new ArrayList<>(); // Kan ook een hashmap worden (met player.id als key ipv in player klasse), of een andere oplossing

    private boolean running;

    public Game(GameController gc, ServerConnection sc, Player user, Player opp) {
        this.gc = gc;
        this.startGame = false;
        this.sc = sc;
        this.possibleMoves = new HashMap<>();
        this.running = true;
        this.user = user;
        this.opp = opp;
        this.doingTurn = false;
    }

    @Override
    public void run() {
        while (running) {
            try {
                if (!startGame) {
                    createPlayers();
                } else {
                    Platform.runLater(() -> {
                        gc.setStatus("Status: In Game( "+gameTimer.getGameTime()+" ) Against "+opp.getName());
                    });
                    showPlayerTurn();
                    updateGame();
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
     * This method will rewind if startGame = false.
     * It will look into the last responses from the server if there is match with the word "MATCH".
     * If there is a match, it will create a new Board object and starts the game.
     */
    private void createPlayers() {
        if (opp.getName().length()>0){
            if (!opp.isPlayersTurn()) {
                user.setId(Settings.PLAYER1);
                user.setColor(Settings.PLAYER1COLOR);
                opp.setId(Settings.PLAYER2);
                opp.setColor(Settings.PLAYER2COLOR);
                Platform.runLater(() -> {
                    gc.getLblPlayer1().setText(user.getName());
                    gc.getLblPlayer2().setText(opp.getName());
                });
                setTurnToUser(true);
            } else {
                user.setId(Settings.PLAYER2);
                user.setColor(Settings.PLAYER2COLOR);
                opp.setId(Settings.PLAYER1);
                opp.setColor(Settings.PLAYER1COLOR);
                Platform.runLater(() -> {
                    gc.getLblPlayer1().setText(opp.getName());
                    gc.getLblPlayer2().setText(user.getName());
                });
//                setTurnToUser(false);
            }

            players.add(this.user); // Voegt de 2 spelers toe aan speler lijst
            players.add(this.opp);

            startGame = true;

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

        startGame = false;

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
                Player tmpPlayer = grid[i][j].getPlayer();
                if(tmpPlayer != null) {
                    if (tmpPlayer.equals(user)) {
                        scoreP1 += 1;
                    } else {
                        scoreP2 += 1;
                    }
                } else {
                    Point tmpPoint = new Point(i,j);
                    if (board.setMove(tmpPoint,user,true,false, false)) movesUser.add(tmpPoint);
                    if (board.setMove(tmpPoint,opp,true,false, false)) movesOpp.add(tmpPoint);
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
    private void updateGame() {
        Player playerSetMove;

        if (tmpPoint!= null) return;

        if (user.isPlayersTurn()){
            playerSetMove = user;
            if (user instanceof HardAIPlayer) user.setPossibleMoves(possibleMoves.get(user));
            tmpPoint = user.doMove();
        }
        else {
            playerSetMove = opp;
            if (opp instanceof EasyAIPlayer) opp.setPossibleMoves(possibleMoves.get(opp));
            if (opp instanceof HardAIPlayer) opp.setPossibleMoves(possibleMoves.get(opp));
            tmpPoint = opp.doMove();
        }

        boolean toServer = false;
        if (playerSetMove.equals(user)&& opp instanceof OnlinePlayer) toServer = true;
        boolean finalToServer = toServer;
        if (tmpPoint != null){
            setTurnToUser(playerSetMove.equals(opp));
            Platform.runLater(()->{
                board.setMove(tmpPoint,playerSetMove,false,false, finalToServer);
                tmpPoint = null;
            });
        }

    }


    /**
     * This method will check if the server had finished a game.
     */
    public void checkForFinish() {
        if (opp instanceof OnlinePlayer) {
            String endResponse = sc.lastRespContains("SVR GAME");
            for (String end : endAnswers) {
                if (endResponse.contains(end)) endGame();
            }
        }
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
        startGame = false;
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

}
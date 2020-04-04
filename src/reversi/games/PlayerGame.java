package reversi.games;

import java.util.Map;

import framework.GameTimer;
import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import reversi.Settings;
import reversi.boards.PlayerBoard;
import reversi.controllers.GameController;

public class PlayerGame extends Game {


    public GameTimer gameTimer;

    public PlayerGame(GameController gc, ServerConnection sc) {
        super(gc, sc);
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

    public void startTimer() {
        new Thread(gameTimer).start();
        startGameScore();
    }

    /**
     * This method will rewind if gameFound = false.
     * It will look into the last responses from the server if there is match with the word "MATCH".
     * If there is a match, it will create a new board object and starts the game.
     */
    @SuppressWarnings("Duplicates")
    protected void searchingEnemy() {
        Platform.runLater(() -> {
            String matchResponse = sc.lastRespContains("MATCH");
            if (matchResponse != null) {
                Map<String, String> tmp = sc.getMap(matchResponse);

                if (tmp.get("PLAYERTOMOVE").equals(tmp.get("OPPONENT"))) {
                    gc.getLblPlayer1().setText(tmp.get("OPPONENT"));
                    gc.getLblPlayer2().setText(sc.getLoginName());
                    user = new Player(Settings.PLAYER2, sc.getLoginName(), Settings.PLAYER2COLOR);
                    opp = new Player(Settings.PLAYER1, tmp.get("OPPONENT"), Settings.PLAYER1COLOR);
                    opp.setPlayersTurn(true);
                } else {
                    gc.getLblPlayer2().setText(tmp.get("OPPONENT"));
                    gc.getLblPlayer1().setText(sc.getLoginName());
                    user = new Player(Settings.PLAYER1, sc.getLoginName(), Settings.PLAYER1COLOR);
                    opp = new Player(Settings.PLAYER2, tmp.get("OPPONENT"), Settings.PLAYER2COLOR);
                    user.setPlayersTurn(true);
                }
                players.add(user); // Voegt de 2 spelers toe aan speler lijst
                players.add(opp);

                gameFound = true;

                startTimer();

                board = new PlayerBoard(gc.getGameTable(), this);
                return;
            }
            gameFound = false;
        });
    }

}

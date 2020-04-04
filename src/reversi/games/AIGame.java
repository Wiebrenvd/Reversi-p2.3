package reversi.games;

import java.util.Map;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import reversi.boards.AIBoard;
import reversi.Settings;
import reversi.ai.AI;
import reversi.controllers.GameController;

/*
 * Game voor de ai
 * */
public class AIGame extends Game {

    private AI ai;

    String loginName = "AI";

    public AIGame(GameController gc, ServerConnection sc, AI ai) {
        super(gc, sc);
        sc.startConnection(Settings.host, Settings.port);
        AIlogin();
        this.ai = ai;
    }

    private void AIlogin() {
        sc.sendCommand("login " + loginName);
        sc.sendCommand("subscribe Reversi");
    }

    /**
     * This method will rewind if gameFound = false.
     * It will look into the last responses from the server if there is match with the word "MATCH".
     * If there is a match, it will create a new PlayerBoard object and starts the game.
     */
    @Override
    @SuppressWarnings("Duplicates")
    protected void searchingEnemy() {
        Platform.runLater(() -> {
            String matchResponse = sc.lastRespContains("MATCH");
            if (matchResponse != null) {

                // TODO Wordt 2x aangeroepen!! 
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

                board = new AIBoard(this);
                return;
            }
            gameFound = false;
        });
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
                    gameRunning();
                    showPlayerTurn();
//                    checkForFinish();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }



}

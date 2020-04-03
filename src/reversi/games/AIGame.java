package reversi.games;

import framework.server.ServerConnection;
import reversi.Cell;
import reversi.Settings;
import reversi.ai.AI;
import reversi.controllers.GameController;

/*
 * Game voor de ai
 * */
public class AIGame extends Game {

    private AI ai;
    private Cell[][] grid;

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




}

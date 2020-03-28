package framework.TicTacToe;


import framework.server.ServerConnection;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

import java.util.ArrayList;

public class Game implements Runnable {
    private ServerConnection serverConnection;
    private boolean gameFound = false;

    public GridPane gridPane;
    private Pane[][] board;
    private int scoreBlack, scoreWhite;
    private String nameBlack, nameWhite;
    public Label lblBlack, lblWhite;


    public Game(ServerConnection sc, GridPane gp, Label lblB, Label lblW) {
        this.serverConnection = sc;
        this.gridPane = gp;
        this.lblBlack = lblB;
        this.lblWhite = lblW;
        this.board = new Pane[8][8];
    }

    @Override
    public void run() {
        while (true) {
            try {

                Thread.sleep(500);
                ArrayList<String> responses = serverConnection.getLastResponses();
                String secLastResp = responses.get(responses.size() - 2);
                String lastResp = responses.get(responses.size() - 1);

                if (!gameFound) {
                    System.out.println("running1");
                    if (secLastResp.contains("MATCH") || lastResp.contains("MATCH")) {

                        gameFound = true;
                    }
                }
            } catch (InterruptedException e){

            }
        }
    }
}
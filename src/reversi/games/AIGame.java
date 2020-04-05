package reversi.games;

import framework.server.ServerConnection;
import reversi.Board;
import reversi.Cell;
import reversi.Settings;
import reversi.ai.AI;
import reversi.controllers.GameController;

import java.awt.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

/*
 * Game voor de ai
 * */
public class AIGame extends Game {

    private AI ai;
    String loginName = "AI";
    private Board board;

    public AIGame(GameController gc, ServerConnection sc, AI ai) {
        super(gc, sc);
        sc.startConnection(Settings.host, Settings.port);
        AIlogin();
        this.ai = ai;
    }
    public void setBoard(Board board){
        this.board = board;
    }
    public void SetMove(){
        //grid ophalen en kijken naar welke plekken geen steentje bevatten, die toevoegen aan een lijst
        Cell[][] grid = board.grid;
        ArrayList<Point> points = new ArrayList<Point>();
        for(int i =0;i<grid.length-1;i++){
            for(int j =0;j<grid[i].length-1;j++) {
                if(grid[i][j].getCircleOK() == null) {
                    points.add(new Point(i, j));
                }
            }
        }

        //Random waarde genereren voor de lijst
        Random rand = new Random();
        int value = rand.nextInt(points.size());
        int x = points.get(value).x;
        int y = points.get(value).y;
        Cell cell = grid[x][y];
        //door de opties blijven loopen als het de ai zijn buurt is.
        while (board.players.get(1).isPlayersTurn()) {
            if (board.players.get(1).isPlayersTurn() && cell.putPiece(4, x, y, board.players.get(1), true)) {
                sc.sendCommand("move " + cell.getMoveParameter(x, y));
                if (sc.lastRespContains("OK") != null) {
                    cell.putPiece(4, x, y, board.players.get(1), false);
                    board.players.get(1).setPlayersTurn(false);
                    board.players.get(0).setPlayersTurn(true);
                    board.currentGame.showPlayerScore();
                }
            } else {
                //geen mogelijke optie dus uit de lijst halen
                points.remove(points.get(value));
                //als de lijst leeg is dan de methode overnieuw aanroepen en nieuwe mogelijkheden berekenen
                if(points.size() == 0 && board.players.get(1).isPlayersTurn()){
                    SetMove();
                }else{
                    //nieuwe random waarde pakken voor het volgende item in de lijst
                    value = rand.nextInt(points.size());
                }
            }
        }
    }

    private void AIlogin() {
        sc.sendCommand("login " + loginName);
        sc.sendCommand("subscribe Reversi");
    }
}

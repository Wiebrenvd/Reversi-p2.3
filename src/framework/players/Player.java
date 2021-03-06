package Framework.players;

import javafx.scene.paint.Color;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;

public abstract class Player {

    protected int id;
    protected boolean playersTurn;
    protected String name;
    protected Color color;
    protected ArrayList<Point> possibleMoves;

    protected HashMap<Point,Integer> possibleMoveGain;

    public Player(String name) {
        this.name = name;
        this.playersTurn = false;
        this.possibleMoves = new ArrayList<>();
        this.possibleMoveGain = new HashMap<>();
    }

    public void setPossibleMoves(ArrayList<Point> moves){
        this.possibleMoves = moves;
    }

    public Point doMove(){
        return null;
    }

    public void setPossibleMoveGain(HashMap<Point, Integer> possibleMoveGain) {
        this.possibleMoveGain = possibleMoveGain;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}

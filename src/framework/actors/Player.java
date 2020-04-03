package framework.actors;

import javafx.scene.paint.Color;

public class Player {

    protected int id;
    protected boolean playersTurn;
    protected String name;
    protected Color color;

    public Player(int id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
        this.playersTurn = false;
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

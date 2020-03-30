package reversi;

import javafx.scene.paint.Color;

public class Player {

    // TODO player class
    private Color playerColor;
    private int player;
    private boolean playersTurn;

    public Player(Color pc, int player){
        this.playerColor = pc;
        this.player = player;
        this.playersTurn = false;
    }

    public Color getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(Color playerColor) {
        this.playerColor = playerColor;
    }

    public int getPlayer() {
        return player;
    }

    public void setPlayer(int player) {
        this.player = player;
    }

    public boolean isPlayersTurn() {
        return playersTurn;
    }

    public void setPlayersTurn(boolean playersTurn) {
        this.playersTurn = playersTurn;
    }
}

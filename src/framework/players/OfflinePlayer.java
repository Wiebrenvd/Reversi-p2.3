package framework.players;

import framework.actors.Player;

import java.awt.*;

public class OfflinePlayer extends Player {
    public OfflinePlayer(String name) {
        super(name);
    }

    @Override
    public Point doMove() {
        try{
            Thread.sleep(500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

package reversi.players;

import framework.actors.Player;
import framework.server.ServerConnection;
import javafx.application.Platform;
import javafx.scene.paint.Color;
import reversi.Settings;

import java.awt.*;
import java.util.Map;

public class OnlinePlayer extends Player {
    private ServerConnection sc;

    public OnlinePlayer(String name,ServerConnection sc) {
        super(name);
        this.sc = sc;
    }

    public OnlinePlayer(ServerConnection sc){
        this("",sc);
    }

    @Override
    public Point doMove() {
        Point output = null;
        if (playersTurn){
            String moveResponse = sc.lastRespContains("GAME MOVE");
//            System.out.println(moveResponse);
            if (moveResponse != null) {
                Map<String, String> tmp = sc.getMap(moveResponse);
                if (!tmp.get("PLAYER").equals(sc.getLoginName())) {
//                    System.out.println(tmp.get("MOVE"));
                    int[] xy = getMoveParameterEnemy(Integer.parseInt(tmp.get("MOVE")));
                    output = new Point(xy[0], xy[1]);
                }
            }
        }
        return output;
    }

    public String getName(){
        if (name.length()==0){
            this.name = searchOnline();
        }
        return name;
    }

    private String searchOnline() {
        String output = "";
        String matchResponse = sc.lastRespContains("MATCH");

        if (matchResponse != null) {
            Map<String, String> tmp = sc.getMap(matchResponse);
            System.out.println(tmp.toString());
            output = tmp.get("OPPONENT");
            if (tmp.get("PLAYERTOMOVE").equals(tmp.get("OPPONENT"))) {

                setPlayersTurn(true);
            }
        }
        return output;
    }

    /**
     * this will calculate the input into this [X,Y]
     *
     * @param move Integer(0-63) cell in the board.
     * @return [X, Y] coordinations
     */
    public int[] getMoveParameterEnemy(int move) {
        int rowIndex = (int) move / Settings.TILESY;
        int colIndex = move % Settings.TILESX;
        int[] output = {colIndex, rowIndex};
        return output;
    }
}

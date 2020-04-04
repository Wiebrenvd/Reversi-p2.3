package reversi.boards;

import reversi.cells.AICell;
import reversi.Settings;
import reversi.games.Game;

public class AIBoard extends Board {



    public AIBoard(Game game) {
        super(game);
        this.grid = new AICell[Settings.TILESX][Settings.TILESY];
    }
}

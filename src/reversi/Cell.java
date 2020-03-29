package reversi;

import java.awt.Point;

import javafx.geometry.Pos;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Cell {

    private Player player;

    private Point point;
    private StackPane clickablePane;

    public Cell(GridPane gameTable, Point point) {
        this.player = null;
        this.point = point;

        this.clickablePane = new StackPane();
        this.clickablePane.setAlignment(Pos.CENTER);
        this.clickablePane.setOnMouseClicked(e -> {
            System.out.printf("Mouse clicked cell [%d, %d]%n", (int) point.getX(), (int) point.getY());
            putPiece();
        });

        gameTable.add(clickablePane, point.x, point.y);

    }

    public Player getPlayer() {
        return player;
    }

    public void putPiece() {
        addCircle();
    }

    @SuppressWarnings("Duplicates")
    public Circle addCircle() {

        Color color = Settings.PlayerColors[0]; // TODO get player color from player class

        Circle circle = new Circle(0, 0, 12);

        circle.setFill(color);
        circle.setStroke(Color.BLACK);

        clickablePane.getChildren().add(circle);

        return circle;
    }
}

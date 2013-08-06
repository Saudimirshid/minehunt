package example.minehunt.client;

import example.minehunt.Cell;
import example.minehunt.DisplayCellResult;
import example.minehunt.Position;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import static java.lang.String.valueOf;

/**
 *
 */
public final class CellNode extends Group {

    private final GridNode gridNode;
    private final Rectangle rectangle;

    public CellNode(final GridNode grid, final Position position) {
        gridNode = grid;

        rectangle = new Rectangle(20, 20);
        rectangle.setFill(Color.RED);
        getChildren().add(rectangle);

        addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            final DisplayCellResult result = grid.getGrid().displayCell(position);
            showCell(result.getSelectedCell());
            for (Cell cell : result.getVisibleCells()) {
                showCell(cell);
            }
        });
    }

    private void showCell(final Cell cell) {
        final CellNode node = gridNode.getCell(cell.getPosition());
        node.rectangle.setFill(Color.GREEN);

        final Text text = new Text();
        text.setText(valueOf(cell.getAdjacentMines()));
        text.setTranslateY(15);
        text.setTranslateX(6);
        getChildren().add(text);
    }
}

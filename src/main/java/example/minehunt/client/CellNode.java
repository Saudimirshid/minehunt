package example.minehunt.client;

import example.minehunt.Cell;
import example.minehunt.CellActionResult;
import example.minehunt.CellActionResult.Outcome;
import example.minehunt.Position;
import javafx.scene.Group;
import javafx.scene.control.Button;
import javafx.scene.control.PopupControl;
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
            final Cell selectedCell = grid.getGrid().getCell(position);
            final CellActionResult result = selectedCell.visit();
            if (result.getOutcome() == Outcome.EXPLOSION) {
                final PopupControl popup = new PopupControl();
                popup.getScene().setRoot(new Button("Yoo loose (press escape)!"));
                popup.show(CellNode.this, 0, 0);
                popup.centerOnScreen();

            } else {
                showCell(selectedCell, result.getMinesNearby());
            }
        });
    }

    private void showCell(final Cell cell, final int nearby) {
        final CellNode node = gridNode.getCell(cell.getPosition());
        node.rectangle.setFill(Color.GREEN);

        final Text text = new Text();
        text.setText(valueOf(nearby));
        text.setTranslateY(15);
        text.setTranslateX(6);
        getChildren().add(text);
    }
}

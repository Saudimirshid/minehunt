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
/* new 2013-09-05 : begin */
import java.util.Set;
import java.util.Iterator;
/* new 2013-09-05 : end */

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
            /* before 2013-09-05 :
            final CellActionResult result = selectedCell.visit();
            */
            /* new 2013-09-05 : begin */
            final CellActionResult result = selectedCell.clearAround();
            /* new 2013-09-05 : end */
            if (result.getOutcome() == Outcome.EXPLOSION) {
                final PopupControl popup = new PopupControl();
                popup.getScene().setRoot(new Button("You lost (press escape)!"));
                popup.show(CellNode.this, 0, 0);
                popup.centerOnScreen();

            } else {
                /* before 2013-09-05 :
                showCell(selectedCell, result.getMinesNearby());
                */
                /* new 2013-09-05 : begin */
                final Set<Cell> set = result.getAffectedCells();
                final Iterator<Cell> iter = set.iterator();
                while (iter.hasNext()) {
                    Cell cell = iter.next();
                    int minesNearby;
                    try {
                        minesNearby = cell.getMinesNearby();
                    }
                    catch (IllegalAccessException e) {
                        minesNearby = -1;
                    }
                    showCell(cell, minesNearby); /* 2013-09-05 : target of link_1 */
                }
                /* new 2013-09-05 : end */
                /* new 2013-09-10 : begin */
                if (grid.getGrid().isGameWon()) {
                    final PopupControl popup = new PopupControl();
                    popup.getScene().setRoot(new Button("You won (press escape)!"));
                    popup.show(CellNode.this, 0, 0);
                    popup.centerOnScreen();
                }
                /* new 2013-09-10 : end */
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
        /* before 2013-09-05 :
        getChildren().add(text); // note by sapeur: check whether there is a
                                    bug here. Is the "getChildren" applying to
                                    the "node" variable declared inside this
                                    method "showCell", or to the "node" object
                                    enclosing this method "showCell" ?
                                    Experiment shows: without the 2013-09-05
                                    modification, when many cells are uncovered
                                    at once at link_1 in this file, the
                                    number "valueOf(nearby)" of every cell
                                    is written on the starting cell, yelding
                                    an illegible superposition of numbers.
        */
        /* new 2013-09-05 : begin */
        node.getChildren().add(text); /* this seems to solve the issue
        mentioned above, about the superposition of numbers */
        /* new 2013-09-05 : end */
    }
}


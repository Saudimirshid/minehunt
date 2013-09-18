package example.minehunt.client;

import example.minehunt.Grid;
import example.minehunt.Position;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class GridNode extends Parent {

    private final Grid grid;
    private final List<List<CellNode>> cells;
    private final SimpleStringProperty hiddenMinesCountProperty;
    private int hiddenMinesCount;
    private final double cellWidth;
    private final double cellHeight;

    public GridNode(final Grid grid, final Image gridImage) {
        this.grid = grid;
        cells = new ArrayList<>(grid.getLines());
        cellWidth = gridImage.getWidth() / grid.getColumns();
        cellHeight = gridImage.getHeight() / grid.getLines();
        for (int i = 0; i < grid.getLines(); i++) {
            final List<CellNode> cols = new ArrayList<>(grid.getColumns());
            cells.add(cols);
            for (int j = 0; j < grid.getColumns(); j++) {
                CellNode cellNode = new CellNode(this, new Position(i, j), gridImage);
                getChildren().add(cellNode);
                cols.add(cellNode);
            }
        }
        hiddenMinesCount = grid.getMineCount();
        hiddenMinesCountProperty = new SimpleStringProperty(String.valueOf(hiddenMinesCount));

    }

    CellNode getCell(Position position) {
        return cells.get(position.getI()).get(position.getJ());
    }

    Grid getGrid() {
        return grid;
    }

    double getCellWidth() {
        return cellWidth;
    }

    double getCellHeight() {
        return cellHeight;
    }

    void foundMine() {
        hiddenMinesCount--;
        hiddenMinesCountProperty.set(String.valueOf(hiddenMinesCount));
    }

    SimpleStringProperty hiddenMinesCountProperty() {
        return hiddenMinesCountProperty;
    }
}

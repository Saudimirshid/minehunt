package example.minehunt.client;

import example.minehunt.Grid;
import example.minehunt.Position;
import javafx.scene.layout.GridPane;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public final class GridNode extends GridPane {

    private final Grid grid;
    private final List<List<CellNode>> cells;

    public GridNode(final Grid grid) {
        this.grid = grid;
        setHgap(3);
        setVgap(3);
        cells = new ArrayList<>(grid.getLines());
        for (int i = 0; i < grid.getLines(); i++) {
            final List<CellNode> cols = new ArrayList<>(grid.getColumns());
            cells.add(cols);
            for (int j = 0; j < grid.getColumns(); j++) {
                CellNode cellNode = new CellNode(this, new Position(i, j));
                add(cellNode, i, j);
                cols.add(cellNode);
            }
        }
    }

    CellNode getCell(Position position) {
        return cells.get(position.getI()).get(position.getJ());
    }

    Grid getGrid() {
        return grid;
    }
}

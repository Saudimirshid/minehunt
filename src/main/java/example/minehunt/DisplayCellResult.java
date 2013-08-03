package example.minehunt;

import java.util.Set;

/**
 *
 */
public final class DisplayCellResult {

    private final Cell selectedCell;
    private final Set<Cell> visibleCells;

    public DisplayCellResult(Cell selectedCell, Set<Cell> visibleCells) {
        this.selectedCell = selectedCell;
        this.visibleCells = visibleCells;
    }

    public Cell getSelectedCell() {
        return selectedCell;
    }

    public Set<Cell> getVisibleCells() {
        return visibleCells;
    }

}

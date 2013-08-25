package example.minehunt;

import java.util.Set;

/*
 *
 * A CellActionResult object is given to whoever asks for the contents of a
 * cell.
 * Its purpose is only to be read, so the fields are directly accessible.
 *
 * The "minesNearby" and "affectedCells" fields have no significant value and
 * content if the outcome is "BLOCKED".
 *
 * The "affectedCells" field has no significant content unless the containing
 * "CellActionResult" object is the return value of a method that asks
 * explicitly for some action on the nearby cells.
 *
 * If the implementor of this class is nice he (or she, please do not start an
 * argument) might decide to set "affectedCells" to "null" or to the empty Set
 * whenever nothing has been asked about the nearby cells, or whenever nothing
 * has been done because the outcome is "BLOCKED". Nothing has been decided yet.
 * 
 * It has not been decided yet whether it is worth setting the "minesNearby"
 * and "affectedCells" fields to the proper value and content if the outcome is
 * "EXPLOSION".
 *
 * It has not been decided yet whether it is worthwhile/useful/desirable to
 * include the selected cell into "affectedCells". But in any case the
 * "selectedCell" field will still be there, the question is: will there be a
 * copy in "affectedCells"?
 */
public final class CellActionResult {

    public enum Outcome {
        BLOCKED, // cause: the cell is flagged
        CLEARED, // no mine in this cell, good job
        EXPLOSION // a mine has been triggered
    }
        
    final Cell selectedCell;
    final Outcome outcome;
    final int minesNearby;
    final Set<Cell> affectedCells;

    CellActionResult(Cell selectedCell,
                     Outcome outcome,
                     int minesNearby,
                     Set<Cell> affectedCells
                    ) {
        this.selectedCell = selectedCell;
        this.outcome = outcome;
        this.minesNearby = minesNearby;
        this.affectedCells = affectedCells;
    }
}

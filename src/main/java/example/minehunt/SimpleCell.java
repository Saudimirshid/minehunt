package example.minehunt; 
import java.util.HashSet;

/**
 *
 */
public final class SimpleCell implements Cell {

    private final SimpleGrid grid;
    private final Position position;
    private final boolean mined;
    private final int minesNearby; // count of mines in the 8 neighbours
    private State state;

    /* note by sapeur:
     * I removed the other constructors which assumed that "mined is true =>
     * do not bother with minesNearby and just set it to 0" and conversely that
     * "I specify the quantity of minesNearby => mined is false".
     * reason for that: the underlying implementation might be eased if the
     * contents of these fields are independent.
     */
    SimpleCell(SimpleGrid grid, Position position, boolean mined, int minesNearby) {
        this.grid = grid;
        this.position = position;
        this.mined = mined;
        this.minesNearby = minesNearby;
        this.state = State.UNVISITED;
    }

    /**
     * the grid containing that cell
     * (might be useful if the player has lost trace of the grid, or is playing
     *  on several grids at once)
     */
    public Grid getGrid() {
        return grid;
    }


    public Position getPosition() {
        return position;
    }


    boolean isMined() {
        return mined;
    }


    int getMinesNearby() {
        return minesNearby;
    }


    /* question: why are the getters for "mined" and "minesNearby" not public?
     * answer: because we do not want to encourage cheating. To know the values
     * of "mined" and "minesNearby" you have to visit or revisit the cell and
     * then read the contents of the CellActionResult object.
     */


    public State getState() {
        return state;
    }


    /**
     * the player has located a mine.
     *
     * @return "false" if the cell has already been visited,
     *         "true" if the cell has been flagged (or was flagged already,
     *                the net effect is the same)
     */
    public boolean setFlag() {
    /* REMOVE THIS IF the switch variant works and is better
        if (state == State.VISITED)
            return false;
        if (state == State.FLAGGED)
            return true;
        state = State.FLAGGED;
        grid.incrFlagCount();
        return true;
     */
        switch (state) {
            case VISITED:
                return false;
            case UNVISITED:
                state = State.FLAGGED;
                grid.incrFlagCount();
        }
        return true;
    }


    /**
     * the player thought there was a mine there, but is changing minds.
     *
     * @return "false" if the cell has already been visited,
     *         "true" if the flag has been removed (or if there was no flag
     *                already, the net effect is the same)
     */
    public boolean unsetFlag() {
    /* REMOVE THIS IF the switch variant works and is better
        if (state == State.VISITED)
            return false;
        if (state == State.UNVISITED)
            return true;
        state = State.UNVISITED;
        grid.decrFlagCount();
        return true;
     */
        switch (state) {
            case VISITED:
                return false;
            case FLAGGED:
                state = State.UNVISITED;
                grid.decrFlagCount();
        }
        return true;
    }


    /* question: where is the "isFlag" method?
     * answer: to know whether the cell is flagged or not, use "getState",
     * there is a flag only if the state is "FLAGGED", 
     */


    /* general-purpose cell-visiting method
     * not intended to be used from outside this class, better use one of
     * the methods that wrap around this one.
     *
     * @parameter clearAround set to true if you want to also visit recursively
                  every empty cell around (but only if this cell happens to
     *            have no mine)
     */
    CellActionResult action(boolean clearAround) {
        if (state == State.FLAGGED)
            return new CellActionResult(
                                        this,
                                        CellActionResult.Outcome.BLOCKED,
                                        -1,
                                        new HashSet<Cell>()
                                       );

        state = State.VISITED;

        CellActionResult.Outcome outcome =
            ( mined ? CellActionResult.Outcome.EXPLOSION
                    : CellActionResult.Outcome.CLEARED
            );
        HashSet<Cell> set = new HashSet<Cell>();
        set.add(this);

        if (clearAround && !mined) {
            // TODO: unveil neighbouring mines with a flood fill algorithm
        }

        return new CellActionResult(
                                    this,
                                    outcome,
                                    minesNearby,
                                    set
                                   );
    }


    /**
     * the player visits the cell
     *
     * with this method, you must ignore the contents of the "affectedCells"
     * field from the object returned
     */
    public CellActionResult visit() {
        return action(false);
    }


    /**
     * the player visits the cell.
     * If this cell happens to be devoid of mine, and if no mine is detected
     * in any of the 8 neighbouring cells, the player wants to proceed visiting
     * all of those neighbours, and to iterate upon every newly visited cell
     * until no more obvious action is possible
     */
    public CellActionResult clearAround() {
        return action(true);
    }


}

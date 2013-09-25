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
     * the grid containing that cell.
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


    /* question: why are the getters for "mined" and "minesNearby" not public?
     * answer: because we do not want to encourage cheating. To know the values
     * of "mined" and "minesNearby" you have to visit or revisit the cell and
     * then read the contents of the CellActionResult object.
     */

    /* edit: the clearAround() method returns a Set of cells automatically
     * visited. It is painful to revisit each one just to get the value of
     * minesNearby.
     * So, here is a getter for minesNearby. It raises an exception if the
     * cell is not in the state VISITED.
     */
    public int getMinesNearby() {
        if (state != State.VISITED)
            throw new RuntimeException("cell is not visited");
        return minesNearby;
    }


    /* package-private equivalent of getMinesNearby()
     *
     */
    int peekMinesNearby() {
        return minesNearby;
    }


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


    /* package-private cell-visiting method.
     * If the cell is flagged, the flag is removed beforehand.
     */
    void forcedVisit() {
        if (state == State.VISITED)
            return;
        if (state == State.FLAGGED)
            grid.decrFlagCount();
        state = State.VISITED;
        grid.decrUnvisitedCount();
        if (mined)
            grid.incrExplosionCount();
        return;
    }


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

        HashSet<Cell> set = new HashSet<Cell>();

        if (state != State.VISITED) {
            forcedVisit();
            set.add(this);
        }

        CellActionResult.Outcome outcome =
            ( mined ? CellActionResult.Outcome.EXPLOSION
                    : CellActionResult.Outcome.CLEARED
            );

        if (clearAround && !mined) {
            Flood flood = new Flood(grid, position);
            set.addAll(flood.getVisited());
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
     * with this method, you may ignore the contents of the "affectedCells"
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


    /**
     * go to the cell located di lines down and dj columns to the right.
     * throws an exception if the desired move falls out of the grid
     */
    SimpleCell move(int di, int dj) throws IndexOutOfBoundsException {
        int i = position.getI();
        int j = position.getJ();

        Position targetPosition = new Position(i+di, j+dj);
        SimpleCell targetCell;
        /* grid.getCell can throw an IndexOutOfBoundsException */
        targetCell = grid.getCell(targetPosition);
        return targetCell;
    }


    /**
     * @return the cell located above this.
     * throws an exception (from "move") if the desired move falls out of
     * the grid
     */
    public SimpleCell goUp() throws IndexOutOfBoundsException {
        return move(-1,0);
    }


    /**
     * @return the cell located below this.
     * throws an exception (from "move") if the desired move falls out of
     * the grid
     */
    public SimpleCell goDown() throws IndexOutOfBoundsException {
        return move(1,0);
    }


    /**
     * @return the cell located to the left of this.
     * throws an exception (from "move") if the desired move falls out of
     * the grid
     */
    public SimpleCell goLeft() throws IndexOutOfBoundsException{
        return move(0,-1);
    }


    /**
     * @return the cell located to the right of this.
     * throws an exception (from "move") if the desired move falls out of
     * the grid
     */
    public SimpleCell goRight() throws IndexOutOfBoundsException {
        return move(0,1);
    }

}


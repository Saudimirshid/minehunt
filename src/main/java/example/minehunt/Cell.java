package example.minehunt;

/**
 *
 */
public interface Cell {

    public enum State {
        UNVISITED, // initial state
        FLAGGED,   // user is sure there is a mine
        VISITED    // cell visited
    }

    /**
     * the grid containing that cell
     * (might be useful if the player has lost trace of the grid, or is playing
     *  on several grids at once)
     */
    Grid getGrid();

    Position getPosition();

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
    int getMinesNearby() throws IllegalAccessException;

    State getState();

    /**
     * the player has located a mine
     *
     * @return "false" if the cell has already been visited,
     *         "true" if the cell has been flagged (or was flagged already,
     *                the net effect is the same)
     */
    boolean setFlag();

    /**
     * the player thought there was a mine there, but is changing minds
     *
     * @return "false" if the cell has already been visited,
     *         "true" if the flag has been removed (or if there was no flag
     *                already, the net effect is the same)
     */
    boolean unsetFlag();

    /* question: where is the "isFlag" method?
     * answer: to know whether the cell is flagged or not, use "getState",
     * there is a flag only if the state is "FLAGGED", 
     */

    /**
     * the player visits the cell.
     *
     * with this method, you must ignore the contents of the "affectedCells"
     * field from the object returned
     */
    CellActionResult visit();

    /**
     * the player visits the cell.
     * If this cell happens to be devoid of mine, and if no mine is detected
     * in any of the 8 neighbouring cells, the player wants to proceed visiting
     * all of those neighbours, and to iterate upon every newly visited cell
     * until no more obvious action is possible
     */
    CellActionResult clearAround();

}

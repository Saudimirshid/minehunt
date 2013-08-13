package example.minehunt;

/**
 *
 */
public final class Cell {

    public final enum State {
        UNVISITED, // initial state
        FLAGGED,   // user is sure there is a mine
        VISITED    // cell visited
    }

    private final Grid grid;
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
    Cell(Grid grid, Position position, boolean mined, int minesNearby) {
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
     * Le joueur a localisé une mine
     *
     * @return "false" si la case a déjà été visitée, "true" si le drapeau a
     *         bien été mis (ou s'il y en avait déjà un. L'effet est le même)
     */
    public boolean setFlag();

    /**
     * Le joueur avait cru localiser une mine, mais change d'avis
     *
     * @return "false" si la case a déjà été visitée, "true" si le drapeau a
     *         bien été ôté (ou s'il n'y en avait pas. L'effet est le même)
     */
    public boolean unsetFlag();

    /* question: where is the "isFlag" method?
     * answer: to know whether the cell is flagged or not, use "getState",
     * there is a flag only if the state is "FLAGGED", 
     */

    /**
     * Le joueur visite la case.
     *
     * Avec cette méthode, le contenu de l'attribut "affectedCells" doit être
     * ignoré.
     */
    public CellActionResult visit();

    /**
     * Le joueur visite la case, et si elle ne contient pas de mine alors le
     * joueur demande à visiter toutes les cases alentours qui ne contiennent
     * évidemment pas de mine (c'est à dire: en partant de la case qui vient
     * d'être visitée, si une case a 0 mine autour d'elle alors visiter toutes
     * les cases adjacentes et itérer).
     */
     public CellActionResult clearAround();

}

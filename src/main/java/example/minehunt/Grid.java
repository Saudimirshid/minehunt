package example.minehunt;

/**
 *
 */
public interface Grid {

    /**
     * @return the count of lines
     */
    int getLines();

    /**
     * @return the count of columns
     */
    int getColumns();

    /**
     *
     * @return the count of mines on the grid
     */
    int getMineCount();

    /**
     * returns the count of cells the player has flagged as surely mined
     *
     * @return the count of Flags
     */
    int getFlagCount();

    /** returns a reference on a cell
     * @param position the position of the desired cell
     * @return the desired cell
     * An IllegalArgumentException is raised if there is no cell with such a
     * position (you should check getLines() and getColumns())
     */
    Cell getCell(Position position) throws IllegalArgumentException;

    /* TODO: méthode(s) pour donner l'état courant de la grille, c'est à dire
     *       l'état de toutes les cases.
     *       Cela peut servir pour déboguer le package "minehunt", et aussi
     *       dans les cas où le joueur/client a besoin de redessiner la grille
     */

}

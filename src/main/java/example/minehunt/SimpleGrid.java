package example.minehunt;

import java.lang.Math;

/**
 *
 */
public final class SimpleGrid implements Grid {

    private final int lines;
    private final int cols;
    private final int mineCount;
    private int unvisitedCount;
    private int explosionCount;
    private int flagCount;
    private final SimpleCell[][] cells;


    /**
     */
    SimpleGrid(int line, int col, int mineCount)
    throws IllegalArgumentException {

        if (line < 0 || col < 0 || mineCount < 0)
            throw new IllegalArgumentException(
                      "no parameter can be negative");
        if (mineCount > line * col)
            throw new IllegalArgumentException(
                      "not enough cells in the grid to hold so many mines");

        this.lines = line;
        this.cols = col;
        this.mineCount = mineCount;
        this.unvisitedCount = lines * cols;
        this.explosionCount = 0;
        this.flagCount = 0;
        this.cells = new SimpleCell[lines][cols];

        boolean[][] tempgrid = new boolean[lines][cols];
        int cellsRemaining = lines * cols;
        int minesRemaining = this.mineCount;
        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < cols; j++) {
                if (Math.random() * cellsRemaining < minesRemaining) {
                    tempgrid[i][j] = true;
                    minesRemaining--;
                }
                cellsRemaining--;
            }
        }

        for (int i = 0; i < lines; i++) {
            for (int j = 0; j < cols; j++) {
                Position position = new Position(i,j);
                boolean mined = tempgrid[i][j];

                int minesNearby = 0;

                /* TODO: in a future version, the following code might be
                         replaced by an Iterator provided by a dedicated
                         method. Something like: cell.getNeighbours() */
                for (int ii = Math.max(i-1,0);
                     ii <= Math.min(i+1,lines-1);
                     ii++) {
                    for (int jj = Math.max(j-1,0);
                         jj <= Math.min(j+1,cols-1);
                         jj++) {
                        if ( !((ii == i) && (jj == j )) && tempgrid[ii][jj])
                            minesNearby++;
                    }
                }

                cells[i][j] = new SimpleCell(this,position,mined,minesNearby);
            }
        }
    }


    /**
     * @return the count of lines
     */
    public int getLines() {
        return lines;
    }


    /**
     * @return the count of columns
     */
    public int getColumns() {
        return cols;
    }


    /**
     * @return the count of cells on the grid.
     * For a rectangular grid, it is lines * columns
     */
    public int getCellCount() {
        return lines * cols;
    }


    /**
     *
     * @return the count of mines on the grid
     */
    public int getMineCount() {
        return mineCount;
    }


    /**
     * @return the count of unvisited cells on the grid
     * (including flagged ones).
     */
    public int getUnvisitedCellsCount() {
        return unvisitedCount;
    }


    /**
     * decrease by one the count of unvisited cells.
     * NOTE: no check is done. To ensure consistency, this method
     * must only be invoked by a Cell that has just been visited.
     */
    void decrUnvisitedCount() {
        unvisitedCount--;
    }


    /**
     * increase by one the count of explosions.
     * NOTE: no check is done. To ensure consistency, this method
     * must only be invoked by a Cell that has just exploded.
     */
    void incrExplosionCount() {
        explosionCount++;
    }


    /**
     * @return true if the game is won.
     * The game is won if every unmined cell has been visited, and no mined
     * cell has been visited.
     * This method returns false if the game is lost or unfinished.
     */
    public boolean isGameWon() {
        if (explosionCount > 0)
            return false;
        if (unvisitedCount == mineCount)
            return true;
        return false;
    }


    /**
     * returns the count of cells the player has flagged as surely mined
     *
     * @return the count of Flags
     */
    public int getFlagCount() {
        return flagCount;
    }


    /**
     * increase by one the count of cells the player has flagged as surely
     * mined.
     * NOTE: no check is done. To ensure consistency, this method
     * must only be invoked by a Cell that has just set a flag
     */
    void incrFlagCount() {
        flagCount++;
    }


    /**
     * decrease by one the count of cells the player has flagged as surely
     * mined.
     * NOTE: no check is done. To ensure consistency, this method
     * must only be invoked by a Cell that has just removed a flag
     */
    void decrFlagCount() {
        flagCount--;
    }


    /** returns a reference on a cell
     * @param position the position of the desired cell
     * @return the desired cell
     * An IndexOutOfBoundsException is raised if there is no cell with such a
     * position (you should check getLines() and getColumns())
     */
    public SimpleCell getCell(Position position)
        throws IndexOutOfBoundsException {
        int i = position.getI();
        int j = position.getJ();
        if ( i < 0 || i >= lines || j < 0 || j >= cols )
            throw new IndexOutOfBoundsException("position is out of bounds");
        return cells[i][j];
    }


   /* TODO: méthode(s) pour donner l'état courant de la grille, c'est à dire
    *       l'état de toutes les cases.
    *       Cela peut servir pour déboguer le package "minehunt", et aussi
    *       dans les cas où le joueur/client a besoin de redessiner la grille
    */

}

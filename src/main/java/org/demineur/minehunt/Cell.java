package org.demineur.minehunt;

/**
 *
 */
public final class Cell {

    private final Position position;
    private final boolean mine;
    private final int adjacentMines;

    public Cell(Position position, boolean mine) {
        this(position, mine, 0);
    }

    public Cell(Position position, int adjacentMines) {
        this(position, false, adjacentMines);
    }

    private Cell(Position position, boolean mine, int adjacentMines) {
        this.position = position;
        this.mine = mine;
        this.adjacentMines = adjacentMines;
    }

    public Position getPosition() {
        return position;
    }

    public boolean isMine() {
        return mine;
    }

    public int getAdjacentMines() {
        return adjacentMines;
    }
}

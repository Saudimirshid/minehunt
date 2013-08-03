package example.minehunt;

/**
 *
 */
public interface Grid {

    /**
     * Rend visible une case et renvoie toutes les cases visibles suite à cette action.
     *
     * @param position la position de la case à rendre visible
     * @return la liste des cases visibles suite à cette action
     */
    DisplayCellResult displayCell(Position position);
}

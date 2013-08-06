package example.minehunt;

/**
 *
 */
public interface Grid {

    /**
     * Renvoie le nombre de lignes.
     *
     * @return le nombre de lignes
     */
    int getLines();

    /**
     * Renvoie le nombre de colonnes.
     *
     * @return le nombre de colonnes
     */
    int getColumns();

    /**
     * Rend visible une case et renvoie toutes les cases visibles suite à cette action.
     *
     * @param position la position de la case à rendre visible
     * @return la liste des cases visibles suite à cette action
     */
    DisplayCellResult displayCell(Position position);
}

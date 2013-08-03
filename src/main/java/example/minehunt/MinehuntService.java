package example.minehunt;

/**
 *
 */
public interface MinehuntService {

    /**
     * Crée une nouvelle grille.
     *
     * @param line le nombre de lignes
     * @param col le nombre de colonnes
     * @return la nouvelle grille
     */
    Grid createGrid(int line, int col);

}

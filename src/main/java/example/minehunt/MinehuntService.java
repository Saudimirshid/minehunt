package example.minehunt;

/**
 *
 */
public interface MinehuntService {

/* TODO: que se passe-t-il si les paramètres de "createGrid" sont impossibles
 *       à satisfaire?
 *       qu'est-ce qu'il est d'usage de faire? lever une "..argumentException",
 *       renvoyer "null", autre chose?
 */

    /**
     * Crée une nouvelle grille.
     *
     * @param line le nombre de lignes
     * @param col le nombre de colonnes
     * @param mineCount le nombre de mines
     * @return la nouvelle grille
     */
    Grid createGrid(int line, int col, int mineCount);

}

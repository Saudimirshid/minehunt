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
     * Renvoie le nombre de mines.
     *
     * @return le nombre de mines
     */
    int getMineCount();

    /**
     * Renvoie le nombre de mines que le joueur a localisées
     *
     * @return le nombre de "Flags"
     */
    int getFlagCount();

    /** Renvoie une référence sur une case
     * @param position la position de la case voulue
     * @return la case voulue, ou null si la position demandée est en dehors du champ (TODO: est-ce que l'usage est plutôt de lever une exception?)
     */
     Cell getCell(Position position);

   /* TODO: méthode(s) pour donner l'état courant de la grille, c'est à dire
    *       l'état de toutes les cases.
    *       Cela peut servir pour déboguer le package "minehunt", et aussi
    *       dans les cas où le joueur/client a besoin de redessiner la grille
    */

}

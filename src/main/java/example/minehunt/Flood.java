package example.minehunt;

import java.util.ArrayList;
import java.util.LinkedList; /* all we need is a LIFO stack */

/**
 * flood fill the grid around a visited cell.
 * you give a grid and a position, and if the corresponding cell is visited
 * and unmined, this class visits every cell that is obviously devoid of mine,
 * starting from the cell you specified and examining recursively the
 * neighbors.
 * A cell "obviously devoid of mine" is one next to a cell marked "0 mine
 * nearby". But the flagged cells are left untouched by this class, even if
 * they appear to be obviously devoid of mine (and therefore are incorrectly
 * flagged). (a future version of this class might give a list of such
 * incorrectly flagged cells that are revealed during the execution of the
 * algorithm).
 *
 * note: to simplify the algorithm, the recursion does not go past cells that
 * are already visited before its inception, because it uses the status
 * "visited" as an mark for already examined cells, in order to avoid
 * reexamining endlessly the same cells and never stop.
 * Therefore, cells that would be candidates for visiting, but are behind
 * already visited ones, are not visited.
 * A little graphical and fictitious example:
 *  limits of the grid : - | +
 *  starting cell (without mines nearby) : S
 *  cells visited so far by the algorithm, without mines nearby: ,
 *  cells visited, with mines nearby: 1
 *  cells unvisited: ?
 *  cells visited before the start of the algorithm, without mines nearby: . 
 *  cells that the algorithm could visit, but will not because of the way it
 *   is implemented: !
 *
 *                +-----+
 *                |????1|
 *                |1111!|
 *                |,,,.!|
 *                |S,,.!|
 *                |,,,.!|
 *                +-----+
 *
 * If you apply Flood every time the player visits a cell, that inconvenience
 * will not happen, so your are not advised to activate the Flood feature on
 * an already started game. Activate it at the start or never.
 * As concerns that inconvenience, you can deactivate the Flood feature at any
 * moment without risk (just refrain from reactivating it later).
 *
 * note: the algorithm has been implemented with in mind a rectangular
 * (or square) grid without hole.
 * Should the algorithm work also on other kinds of grid would be a lucky
 * chance on which you are advised not to bet.
 */
class Flood {

    /* the starting cell */
    private SimpleCell seed;

    /* the list of newly visited cells throughout the process */
    private ArrayList<SimpleCell> list;


    /* a locally used enumeration, to remember the status of cells examined
     * during the execution of the algorithm
     */
    private enum Status {
        NONE,    // undecided state
        FORGET,  // everything is done about this cell, no need to come back
        TOVISIT  // this cell must be examined again for the recursion
    }

    /* all the work is done in this constructor */
    Flood(SimpleGrid grid, Position position) {

        this.list = new ArrayList<SimpleCell>();

        try {
            this.seed = grid.getCell(position);
        }
        catch (IllegalArgumentException e) {
            this.seed = null;
            return;
        }

        if (this.seed.getState() != Cell.State.VISITED ||
            this.seed.isMined() ||
            this.seed.peekMinesNearby() > 0)
            return;

        /* contents of stack : visited cells in need of clearing around */
        LinkedList<SimpleCell> stack = new LinkedList<SimpleCell>();
        stack.push(this.seed);

        while (stack.size() > 0) {

            SimpleCell current = stack.pop();

            /* we will examine the cells linewise:
             * process the line containing the current cell, to the left and
             * to the right, up to the first obstacle.
             * simultaneously, scan the cells above and below and push every
             * leftmost cell in need of recursion.
             * Encountered cells that are on the edge of the area to fill are
             * visited immediately.
             */
            SimpleCell above;
            try {
                above = current.goUp();
            }
            catch (IndexOutOfBoundsException e) {
                above = null;
            }
            Status aboveStatus = Status.NONE;
            SimpleCell below;
            try {
                below = current.goDown();
            }
            catch (IndexOutOfBoundsException e) {
                below = null;
            }
            Status belowStatus = Status.NONE;
            SimpleCell formerAbove;
            Status formerAboveStatus;
            SimpleCell formerBelow;
            Status formerBelowStatus;

            /* implementor's note: from here down to the end of the block,
             * there are opportunities for code factorization/refactoring */

            FloodAlgo2 fa2;
            /* fin de modif */

            /* FloodAlgo.processCell(above, aboveStatus, list); */
            fa2 = new FloodAlgo2(above);
            aboveStatus = fa2.status;
            if (fa2.visitNow) {
            	above.forcedVisit();
            	list.add(above);
            }
            /* fin de modif */

            /* FloodAlgo.processCell(below, belowStatus, list); */
            fa2 = new FloodAlgo2(below);
            belowStatus = fa2.status;
            if (fa2.visitNow) {
            	below.forcedVisit();
            	list.add(below);
            }
            /* fin de modif */


            SimpleCell startingCurrent = current;
            SimpleCell startingAbove = above;
            Status startingAboveStatus = aboveStatus;
            SimpleCell startingBelow = below;
            Status startingBelowStatus = belowStatus;

            Status currentStatus = Status.NONE;

            /* visit the line to the right */
            do {
                if (above != null) {
                    formerAbove = above;
                    formerAboveStatus = aboveStatus;
                    try {
                        above = above.goRight();
                    }
                    catch (IndexOutOfBoundsException e) {
                        above = null;
                    }
                    /* FloodAlgo.processCell(above, aboveStatus, list); */
                    fa2 = new FloodAlgo2(above);
                    aboveStatus = fa2.status;
                    if (fa2.visitNow) {
                    	above.forcedVisit();
                    	list.add(above);
                    }
                    /* fin de modif */

                    if (formerAboveStatus != aboveStatus &&
                        aboveStatus == Status.TOVISIT) {
                        above.forcedVisit();
                        list.add(above);
                        stack.push(above);
                    }
                }

                if (below != null) {
                    formerBelow = below;
                    formerBelowStatus = belowStatus;
                    try {
                        below = below.goRight();
                    }
                    catch (IndexOutOfBoundsException e) {
                        below = null;
                    }
                    /* FloodAlgo.processCell(below, belowStatus, list); */
                    fa2 = new FloodAlgo2(below);
                    belowStatus = fa2.status;
                    if (fa2.visitNow) {
                    	below.forcedVisit();
                    	list.add(below);
                    }
                    /* fin de modif */

                    if (formerBelowStatus != belowStatus &&
                        belowStatus == Status.TOVISIT) {
                        below.forcedVisit();
                        list.add(below);
                        stack.push(below);
                    }
                }

                try {
                    current = current.goRight();
                }
                catch (IndexOutOfBoundsException e) {
                    current = null;
                }
                /* FloodAlgo.processCell(current, currentStatus, list); */
                fa2 = new FloodAlgo2(current);
                currentStatus = fa2.status;
                if (fa2.visitNow) {
                	current.forcedVisit();
                	list.add(current);
                }
                /* fin de modif */

                if (currentStatus == Status.TOVISIT) {
                    current.forcedVisit();
                    list.add(current);
                }
            } while (currentStatus == Status.TOVISIT);

            current = startingCurrent;
            above = startingAbove;
            aboveStatus = startingAboveStatus;
            below = startingBelow;
            belowStatus = startingBelowStatus;

            /* visit the line to the left */
            do {
                if (above != null) {
                    formerAbove = above;
                    formerAboveStatus = aboveStatus;
                    try {
                        above = above.goLeft();
                    }
                    catch (IndexOutOfBoundsException e) {
                        above = null;
                    }
                    /* FloodAlgo.processCell(above, aboveStatus, list); */
                    fa2 = new FloodAlgo2(above);
                    aboveStatus = fa2.status;
                    if (fa2.visitNow) {
                    	above.forcedVisit();
                    	list.add(above);
                    }
                    /* fin de modif */

                    if (formerAboveStatus != aboveStatus &&
                        formerAboveStatus == Status.TOVISIT) {
                        formerAbove.forcedVisit();
                        list.add(formerAbove);
                        stack.push(formerAbove);
                    }
                }

                if (below != null) {
                    formerBelow = below;
                    formerBelowStatus = belowStatus;
                    try {
                        below = below.goLeft();
                    }
                    catch (IndexOutOfBoundsException e) {
                        below = null;
                    }
                    /* FloodAlgo.processCell(below, belowStatus, list); */
                    fa2 = new FloodAlgo2(below);
                    belowStatus = fa2.status;
                    if (fa2.visitNow) {
                    	below.forcedVisit();
                    	list.add(below);
                    }
                    /* fin de modif */

                    if (formerBelowStatus != belowStatus &&
                        formerBelowStatus == Status.TOVISIT) {
                        formerBelow.forcedVisit();
                        list.add(formerBelow);
                        stack.push(formerBelow);
                    }
                }

                try {
                    current = current.goLeft();
                }
                catch (IndexOutOfBoundsException e) {
                    current = null;
                }
                /* FloodAlgo.processCell(current, currentStatus, list); */
                fa2 = new FloodAlgo2(current);
                currentStatus = fa2.status;
                if (fa2.visitNow) {
                	current.forcedVisit();
                	list.add(current);
                }
                /* fin de modif */

                if (currentStatus == Status.TOVISIT) {
                    current.forcedVisit();
                    list.add(current);
                }
            } while (currentStatus == Status.TOVISIT);

            if (aboveStatus == Status.TOVISIT) {
                above.forcedVisit();
                list.add(above);
                stack.push(above);
            }

            if (belowStatus == Status.TOVISIT) {
                below.forcedVisit();
                list.add(below);
                stack.push(below);
            }

        } /* while (stack.size() > 0) */
    }


    /**
     * @return the list of cells newly visited throughout the Flood process.
     */
    ArrayList<SimpleCell> getVisited() {
        return list;
    }


    /* classe imbriquee, qui contrairement a "FloodAlgo" presuppose que l'on
     * ne peut pas (toujours) modifier la valeur associee a un parametre formel
     * avec pour effet que la valeur du parametre effectif changera de concert
     */
    private static class FloodAlgo2 {

        final Status status;
        final boolean visitNow;

        FloodAlgo2(SimpleCell cell) {
            if (cell == null) {
                this.status = Status.NONE;
                this.visitNow = false;
            }
            else {
                /* assert(cell.isMined() == false) */
                /* if (cell.getState() == Cell.State.FLAGGED) {
                     the player has flagged a cell which comes out to be next
                     to a cell with no mine around. Therefore the flag is
                     wrong, and the interface could mention that to the player
                   } */
                if (cell.getState() != Cell.State.UNVISITED) {
                    this.status = Status.FORGET;
                    this.visitNow = false;
                }
                else {
                    if (cell.peekMinesNearby() > 0) {
                        this.status = Status.FORGET;
                        this.visitNow = true;
                    }
                    else {
                        this.status = Status.TOVISIT;
                        this.visitNow = false;
                    }
                }
            }
        }
    }


    /* procedure interne, qui presuppose que les parametres sont passes par
     * reference, et que la valeur associee peut etre modifiee par
     * "processCell" , c'est a dire sur un exemple:
     *   SimpleCell cell = grid.getCell(position);
     *   Status status = Status.NONE;
     *   ArrayList<SimpleCell> list = new ArrayList<SimpleCell>();
     *   FloodAlgo.processCell(cell,status,list);
     *   // ici le contenu de cell et/ou status et/ou list peut avoir change
     */
    private static class FloodAlgo {

        static void processCell(SimpleCell cell,
                                Status status,
                                ArrayList<SimpleCell> list) {
            if (cell == null)
                status = Status.NONE;
            else {
                /* assert(cell.isMined() == false) */
                /* if (cell.getState() == Cell.State.FLAGGED) {
                     the player has flagged a cell which comes out to be next
                     to a cell with no mine around. Therefore the flag is
                     wrong, and the interface could mention that to the player
                   } */
                if (cell.getState() != Cell.State.UNVISITED)
                    status = Status.FORGET;
                else {
                    if (cell.peekMinesNearby() > 0) {
                        cell.forcedVisit(); /* because of the context, "cell" is
                                                unvisited and devoid of mine,
                                                therefore much of the code in
                                                forcedVisit() is redundant.
                                                Possible fix for this (slight?)
                                                inefficiency: make some of the
                                                fields of SimpleCell package-
                                                private, and modify them directly
                                                from here */
                        list.add(cell);
                        status = Status.FORGET;
                    }
                    else
                        status = Status.TOVISIT;
                }
            }
        }

    }

}


package com.a14.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
/**
 * Represents the model of the game board (grid).
 * It stores the state of all currently living cells and provides methods
 * to manipulate the board state (add, remove, toggle cells).
 */
public class GameBoard {
    /**
     * A set containing the coordinates of all currently living cells.
     */
    private Set<Point> liveCells;
    /**
     * Initializes a new, empty game board with no living cells.
     */
    public GameBoard() {
        liveCells = new HashSet<>();
    }
    /**
     * Adds a living cell at the specified coordinates.
     * If the cell is already alive, the state remains unchanged.
     * @param point The coordinate point where the cell should be added.
     */
    public void addCell(Point point) {
        liveCells.add(point);
    }
    /**
     * Removes a living cell from the specified coordinates.
     * If the cell is not alive (not present), nothing happens.
     * @param point The coordinate point of the cell to remove.
     */
    public void removeCell(Point point) {
        liveCells.remove(point);
    }
    /**
     * Checks if a cell at the given coordinates is currently alive.
     * @param point The coordinate point to check.
     * @return true if the cell is alive (present in the set), false otherwise.
     */
    public boolean isAlive(Point point) {
        return liveCells.contains(point);
    }
    /**
     * Revives a cell at specific (x, y) coordinates.
     * This method is primarily used for restoring game state from a save file
     * or explicit cell creation where a Point object is not yet created.
     * @param x The X coordinate of the cell.
     * @param y The Y coordinate of the cell.
     */
    public void reviveCell(int x, int y) { //do wskrzeszenia żywych komórek z zapisu
        liveCells.add(new Point(x, y));
    }
    /**
     * Retrieves an unmodifiable view of the currently living cells.
     * Attempts to modify the returned set will result in an UnsupportedOperationException.
     * @return An unmodifiable Set of Points representing live cells.
     */
    public Set<Point> getLiveCells() {
        return Collections.unmodifiableSet(liveCells);
    }
    /**
     * Clears the entire board, removing all living cells.
     */
    public void clearLiveCells() {
        liveCells.clear();
    }
    /**
     * Replaces the current state of the board with a new generation of cells.
     * This is typically called by the simulation engine after calculating the next step.
     * @param nextGen The set of points representing the new generation of living cells.
     */
    public void newGeneration(Set<Point> nextGen) {
        this.liveCells = nextGen;
    }
    /**
     * Toggles the state of the cell at the specified point.
     * If the cell is alive, it dies. If it is dead, it becomes alive.
     * @param point The coordinate point to toggle.
     */
    public void toggleCell(Point point) {
        if(liveCells.contains(point)) {
            liveCells.remove(point);
        }
        else{
            liveCells.add(point);
        }

    }

}

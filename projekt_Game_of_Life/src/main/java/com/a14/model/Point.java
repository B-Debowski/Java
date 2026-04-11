package com.a14.model;

/**
 * Represents an immutable 2D coordinate on the game board.
 * This record holds the horizontal (x) and vertical (y) positions of a cell.
 *
 * @param x The horizontal coordinate (column index).
 * @param y The vertical coordinate (row index).
 * @author Igor Toboja & Bartosz Dębowski
 */
public record Point(int x, int y) {

    /**
     * Calculates the coordinates of all 8 adjacent neighbors (Moore neighborhood).
     * This includes cells horizontally, vertically, and diagonally adjacent
     * to the current point.
     *
     * @return An array of 8 Point objects representing the surrounding neighbors.
     */
    public Point[] getNeighbors() {
        Point[] neighbors = new Point[8];
        neighbors[0] = new Point(x-1, y+1);
        neighbors[1] = new Point(x, y+1);
        neighbors[2] = new Point(x+1, y+1);
        neighbors[3] = new Point(x+1, y);
        neighbors[4] = new Point(x+1, y-1);
        neighbors[5] = new Point(x, y-1);
        neighbors[6] = new Point(x-1, y-1);
        neighbors[7] = new Point(x-1, y);
        return neighbors;
    }
}
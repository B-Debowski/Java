package com.a14.json;

import java.util.List;
import java.util.Set;

/**
 * Represents a snapshot of the game state for serialization purposes.
 * <p>
 * This class serves as a Data Transfer Object (DTO) used during the Save/Load process.
 * It stores the camera's viewport settings and the coordinates of all living cells,
 * allowing the simulation to be reconstructed exactly as it was left.
 */
public class GameState {
    public double cameraX;
    public double cameraY;
    public double zoom;
    public List<SavedCell> liveCells;
    public Set<Integer> bornRules;
    public Set<Integer> surviveRules;


    public GameState() {
    }
    public record SavedCell(int x, int y) {}
}
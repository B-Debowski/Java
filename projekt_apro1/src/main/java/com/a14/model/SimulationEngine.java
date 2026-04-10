package com.a14.model;


import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * The core logic engine for the Game of Life simulation.
 * It is responsible for calculating the evolution of the game state
 * from one generation to the next based on the provided GameRules.
 */
public class SimulationEngine {
    private GameBoard currentGameBoard;
    private GameRules rules;

    /**
     * Initializes the simulation engine with a specific board and set of rules.
     * @param gameBoard The game board model containing the current state of cells.
     * @param rules The rules defining the conditions for cell birth and survival.
     */
    public SimulationEngine(GameBoard gameBoard, GameRules rules) {
        this.currentGameBoard = gameBoard;
        this.rules = rules;
    }

    /**
     * Updates the simulation rules dynamically.
     * This allows changing the game behavior (e.g., birth/survival counts) at runtime.
     * @param rules The new GameRules object to apply in subsequent generations.
     */
    public void setGameRules(GameRules rules) {
        this.rules = rules;
    }

    /**
     * Calculates the next generation of cells (one simulation step).
     * <p>
     * The algorithm uses a Map to count neighbors only for active cells and their immediate neighbors,
     * which is efficient for sparse grids. It then applies the birth and survival rules
     * to determine the state of the board for the next frame.
     * </p>
     */
    public void calculateNextGeneration(){
        // Map storing the count of live neighbors for each potential cell
        Map<Point, Integer> neighborMap = new  HashMap<>();

        // Iterate only through currently living cells to populate the neighbor counts
        for(Point point : currentGameBoard.getLiveCells()){
            Point[] neighbors = point.getNeighbors();
            for(Point neighbor : neighbors){
                neighborMap.merge(neighbor, 1, Integer::sum);
            }
        }

        Set<Point> nextGeneration = new HashSet<>();

        // Evaluate all cells that have at least one neighbor
        for (Point p : neighborMap.keySet()) {
            int liveNeighborCount = neighborMap.get(p);

            if(currentGameBoard.isAlive(p)){
                if(rules.shouldSurvive(liveNeighborCount)){
                    nextGeneration.add(p);
                }
            }
            else{
                if(rules.shouldBeBorn(liveNeighborCount)){
                    nextGeneration.add(p);
                }
            }
        }

        // Apply the calculated state to the board
        currentGameBoard.newGeneration(nextGeneration);
    }

    /**
     * Retrieves the game board instance managed by this engine.
     * @return The current GameBoard object.
     */
    public GameBoard getBoard() {
        return currentGameBoard;
    }

    /**
     * Retrieves the current rules used by the simulation.
     * @return The current GameRules object.
     */
    public GameRules getRules() {
        return rules;
    }
}
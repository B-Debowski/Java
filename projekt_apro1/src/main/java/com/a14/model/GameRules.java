package com.a14.model;

import com.a14.json.GameConfig;

import java.util.HashSet;
import java.util.Set;

/**
 * Defines the rules for the Game of Life simulation.
 * It specifies the conditions under which a cell is born or survives
 * based on the number of its living neighbors.
 */
public class GameRules {
    /**
     * A set of neighbor counts that cause a dead cell to become alive (be born).
     */
    private final Set<Integer> birthConditions;

    /**
     * A set of neighbor counts that allow a live cell to stay alive.
     */
    private final Set<Integer> survivalConditions;

    /**
     * Creates a new set of game rules with explicit conditions.
     * @param birthConditions A set of integers representing the number of neighbors required for birth.
     * @param survivalConditions A set of integers representing the number of neighbors required for survival.
     */
    public GameRules(Set<Integer> birthConditions, Set<Integer> survivalConditions) {
        this.birthConditions = birthConditions;
        this.survivalConditions = survivalConditions;
    }

    /**
     * Creates a new set of game rules based on a configuration object.
     * Typically used when loading rules from a JSON file or user settings.
     * @param config The configuration object containing birth and survival rules lists.
     */
    public GameRules(GameConfig config) {
        this.birthConditions = new HashSet<>(config.born);
        this.survivalConditions = new HashSet<>(config.survive);
    }

    /**
     * Retrieves the set of conditions required for a cell to be born.
     * @return A Set of Integers representing neighbor counts for birth.
     */
    public Set<Integer> getBirthConditions() {
        return birthConditions;
    }

    /**
     * Retrieves the set of conditions required for a cell to survive.
     * @return A Set of Integers representing neighbor counts for survival.
     */
    public Set<Integer> getSurvivalConditions() {
        return survivalConditions;
    }

    public void setBirthConditions(Set<Integer> birthConditions) {
        this.birthConditions.clear();
        this.birthConditions.addAll(birthConditions);
    }
    public void setSurvivalConditions(Set<Integer> survivalConditions) {
        this.survivalConditions.clear();
        this.survivalConditions.addAll(survivalConditions);
    }

    /**
     * Checks if a dead cell should become alive based on its neighbor count.
     * @param neighborsCount The number of living neighbors surrounding the dead cell.
     * @return true if the neighbor count meets any of the birth conditions, false otherwise.
     */
    public boolean shouldBeBorn(int neighborsCount) {
        return birthConditions.contains(neighborsCount);
    }

    /**
     * Checks if a currently living cell should stay alive based on its neighbor count.
     * @param neighborsCount The number of living neighbors surrounding the live cell.
     * @return true if the neighbor count meets any of the survival conditions, false otherwise.
     */
    public boolean shouldSurvive(int neighborsCount) {
        return survivalConditions.contains(neighborsCount);
    }
}
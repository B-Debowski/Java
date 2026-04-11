package com.a14.model;
import java.util.Set;

/**
 * Represents a reusable pattern of cells.
 * @param name The display name of the pattern.
 * @param offsets A set of points representing the pattern's shape relative to a (0,0) origin.
 * @author Tomasz Kozłowski
 */
public record Pattern(String name, Set<Point> offsets) {}

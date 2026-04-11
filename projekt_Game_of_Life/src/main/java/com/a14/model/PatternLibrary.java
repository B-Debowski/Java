package com.a14.model;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Tomasz Kozłowski
 */
public class PatternLibrary {

    public static List<Pattern> getPatterns() {
        return List.of(
            new Pattern("Single Cell", Set.of(new Point(0, 0))),
            createGlider(),
            createToad(),
            createBeacon(),
            createLWSS(),
            createPulsar(),
            createGosperGliderGun()
        );
    }

    private static Pattern createGlider() {
        Set<Point> points = new HashSet<>();
        points.add(new Point(1, 0));
        points.add(new Point(2, 1));
        points.add(new Point(0, 2));
        points.add(new Point(1, 2));
        points.add(new Point(2, 2));
        return new Pattern("Glider", points);
    }

    private static Pattern createToad() {
        Set<Point> points = new HashSet<>();
        points.add(new Point(1, 1));
        points.add(new Point(2, 1));
        points.add(new Point(3, 1));
        points.add(new Point(0, 2));
        points.add(new Point(1, 2));
        points.add(new Point(2, 2));
        return new Pattern("Toad", points);
    }

    private static Pattern createBeacon() {
        Set<Point> points = new HashSet<>();
        points.add(new Point(0, 0));
        points.add(new Point(1, 0));
        points.add(new Point(0, 1));
        points.add(new Point(1, 1));
        
        points.add(new Point(2, 2));
        points.add(new Point(3, 2));
        points.add(new Point(2, 3));
        points.add(new Point(3, 3));
        return new Pattern("Beacon", points);
    }

    private static Pattern createLWSS() {
        Set<Point> points = new HashSet<>();
        points.add(new Point(1, 0));
        points.add(new Point(4, 0));
        points.add(new Point(0, 1));
        points.add(new Point(0, 2));
        points.add(new Point(4, 2));
        points.add(new Point(0, 3));
        points.add(new Point(1, 3));
        points.add(new Point(2, 3));
        points.add(new Point(3, 3));
        return new Pattern("LWSS", points);
    }
    
    private static Pattern createPulsar() {
         Set<Point> points = new HashSet<>();
         points.add(new Point(1, 0));
         points.add(new Point(2, 0));
         points.add(new Point(0, 1));
         points.add(new Point(1, 1));
         points.add(new Point(1, 2));
         return new Pattern("R-pentomino", points);
    }

    private static Pattern createGosperGliderGun() {
        Set<Point> points = new HashSet<>();
        points.add(new Point(0, 4));
        points.add(new Point(1, 4));
        points.add(new Point(0, 5));
        points.add(new Point(1, 5));
        points.add(new Point(10, 4));
        points.add(new Point(10, 5));
        points.add(new Point(10, 6));
        points.add(new Point(11, 3));
        points.add(new Point(11, 7));
        points.add(new Point(12, 2));
        points.add(new Point(12, 8));
        points.add(new Point(13, 2));
        points.add(new Point(13, 8));
        points.add(new Point(14, 5));
        points.add(new Point(15, 3));
        points.add(new Point(15, 7));
        points.add(new Point(16, 4));
        points.add(new Point(16, 5));
        points.add(new Point(16, 6));
        points.add(new Point(17, 5));
        points.add(new Point(20, 2));
        points.add(new Point(20, 3));
        points.add(new Point(20, 4));
        points.add(new Point(21, 2));
        points.add(new Point(21, 3));
        points.add(new Point(21, 4));
        points.add(new Point(22, 1));
        points.add(new Point(22, 5));
        points.add(new Point(24, 0));
        points.add(new Point(24, 1));
        points.add(new Point(24, 5));
        points.add(new Point(24, 6));
        points.add(new Point(34, 2));
        points.add(new Point(35, 2));
        points.add(new Point(34, 3));
        points.add(new Point(35, 3));
        return new Pattern("Gosper Glider Gun", points);
    }
}

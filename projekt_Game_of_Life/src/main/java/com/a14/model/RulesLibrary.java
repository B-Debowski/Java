package com.a14.model;

import java.util.List;
import java.util.Set;

public class RulesLibrary {
    public static List<RuleSet> getRuleSets() {
        return List.of(
                new RuleSet("Conway's Life", Set.of(3), Set.of(2, 3)),
                new RuleSet("HighLife", Set.of(3, 6), Set.of(2, 3)),
                new RuleSet("Seeds", Set.of(2), Set.of()),
                new RuleSet("Day and Night", Set.of(3,6,7,8), Set.of(3,4,6,7,8)),
                new RuleSet("Antilife", Set.of(0,1,2,3,4,7,8), Set.of(0,1,2,3,4,6,7,8)),
                new RuleSet("Wickstretcher And The Parasites", Set.of(0,1,3,5,6), Set.of(0,1,2,3,4,5)),
                new RuleSet("Oils", Set.of(0,1,4), Set.of(2)),
                new RuleSet("Sierpinski triangle", Set.of(1), Set.of(1,2)),
                new RuleSet("H-trees", Set.of(1), Set.of(0,1,2,3,4,5,6,7,8)),
                new RuleSet("Gnarl", Set.of(1), Set.of(1)),
                new RuleSet("Replicator", Set.of(1,3,5,7), Set.of(1,3,5,7)),
                new RuleSet("Live Free or Die", Set.of(2), Set.of(0)),
                new RuleSet("Iceballs", Set.of(2,5,6,7,8), Set.of(5,6,7,8)),
                new RuleSet("Maze", Set.of(3), Set.of(1,2,3,4,5)),
                new RuleSet("Maze with Mice", Set.of(3,7), Set.of(1,2,3,4,5)),
                new RuleSet("Walled cities ", Set.of(4,5,6,7,8), Set.of(2,3,4,5)),
                new RuleSet("Bugs", Set.of(3,5,6,7), Set.of(1,5,6,7,8)),
                new RuleSet("Rings 'n' Slugs", Set.of(5,6), Set.of(1,4,5,6,8)),
                new RuleSet("Shoots and Roots", Set.of(3), Set.of(2,4,5,6,7,8)),
                new RuleSet("Never happy", Set.of(3,4,5), Set.of(0,4,5,6))
        );
    }
}

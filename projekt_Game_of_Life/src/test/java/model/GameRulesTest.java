package model;

import com.a14.model.GameRules;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class GameRulesTest {

    @Test
    void testStandardConwayRules() {
        GameRules rules = new GameRules(Set.of(3), Set.of(2, 3));

        assertTrue(rules.shouldBeBorn(3), "Dead cell with 3 neighbors should be born");
        assertFalse(rules.shouldBeBorn(2), "Dead cell with 2 neighbors should not be born");

        assertTrue(rules.shouldSurvive(2), "Live cell with 2 neighbors should survive");
        assertTrue(rules.shouldSurvive(3), "Live cell with 3 neighbors should survive");
        assertFalse(rules.shouldSurvive(1), "Live cell with 1 neighbor should die (underpopulation)");
        assertFalse(rules.shouldSurvive(4), "Live cell with 4 neighbors should die (overpopulation)");
    }
}
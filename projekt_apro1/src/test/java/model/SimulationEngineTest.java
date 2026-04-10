package model;

import com.a14.model.GameBoard;
import com.a14.model.GameRules;
import com.a14.model.Point;
import com.a14.model.SimulationEngine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class SimulationEngineTest {

    private GameBoard board;
    private SimulationEngine engine;

    @BeforeEach
    void setUp() {
        board = new GameBoard();
        GameRules rules = new GameRules(Set.of(3), Set.of(2, 3));
        engine = new SimulationEngine(board, rules);
    }

    @Test
    void testBlockLife() {
        board.addCell(new Point(0, 0));
        board.addCell(new Point(1, 0));
        board.addCell(new Point(0, 1));
        board.addCell(new Point(1, 1));

        engine.calculateNextGeneration();

        assertEquals(4, board.getLiveCells().size());
        assertTrue(board.isAlive(new Point(0, 0)));
        assertTrue(board.isAlive(new Point(1, 1)));
    }

    @Test
    void testBlinkerOscillator() {

        board.addCell(new Point(0, 1));
        board.addCell(new Point(1, 1));
        board.addCell(new Point(2, 1));

        engine.calculateNextGeneration();

        assertTrue(board.isAlive(new Point(1, 0)));
        assertTrue(board.isAlive(new Point(1, 1)));
        assertTrue(board.isAlive(new Point(1, 2)));

        assertFalse(board.isAlive(new Point(0, 1)), "The cells on the sides should not be alive");
        assertFalse(board.isAlive(new Point(2, 1)), "The cells on the sides should not be alive");

        engine.calculateNextGeneration();

        assertTrue(board.isAlive(new Point(0, 1)));
        assertTrue(board.isAlive(new Point(1, 1)));
        assertTrue(board.isAlive(new Point(2, 1)));
    }
}
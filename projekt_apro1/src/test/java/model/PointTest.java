package model;
import com.a14.model.Point;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PointTest {

    @Test
    void testGetNeighborsReturnsEightPoints() {

        Point point = new Point(0, 0);

        Point[] neighbors = point.getNeighbors();

        assertEquals(8, neighbors.length, "Every cell should have 8 neighbors");

        boolean foundTopRight = false;
        for (Point p : neighbors) {
            if (p.x() == 1 && p.y() == 1) {
                foundTopRight = true;
                break;
            }

        }
        assertTrue(foundTopRight, "Should have found top right cell");
    }

    @Test
    void testEquality() {
        Point p1 = new Point(5, 10);
        Point p2 = new Point(5, 10);

        assertEquals(p1, p2, "Point with the same coordinates should be equal");
        assertEquals(p1.hashCode(), p2.hashCode(), "HashCode should be equal for two points with the same coordinates");
    }
}
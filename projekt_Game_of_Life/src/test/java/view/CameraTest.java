package view;

import com.a14.json.GameConfig;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import com.a14.view.Camera;

class CameraTest {

    @Test
    void testCoordinateTranslation() {
        GameConfig config = new GameConfig();
        config.cameraX = 0.0;
        config.cameraY = 0.0;
        config.cameraZoom = 1.0;

        Camera camera = new Camera(config);

        camera.setZoom(1.0);

        assertEquals(0, camera.getCellX(25));
        assertEquals(0, camera.getCellY(25));

        assertEquals(1, camera.getCellX(60));
        assertEquals(1, camera.getCellY(60));

        assertEquals(100.0, camera.getPixelX(2), 0.001);
    }

    @Test
    void testZoomAffectsCoordinates() {
        GameConfig config = new GameConfig();
        Camera camera = new Camera(config);
        camera.setOffsetX(0);
        camera.setOffsetY(0);
        camera.setZoom(2.0);

        assertEquals(0, camera.getCellX(75));

        assertEquals(1, camera.getCellX(110));
    }
}
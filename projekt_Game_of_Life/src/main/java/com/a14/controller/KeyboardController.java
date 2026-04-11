package com.a14.controller;
import com.a14.view.Camera;
import javafx.scene.input.KeyEvent;

/**
 * Controller class responsible for handling keyboard interactions.
 * Translates key presses into Camera movement, zoom, and simulation control.
 * @author Tomasz Kozłowski
 */
public class KeyboardController {
    private final Camera camera;
    private static final double MOVE_SPEED = 10.0;
    private Runnable onSpacePressed;
    private Runnable onClearPressed;

    /**
     * Initializes the controller with a reference to the camera.
     * @param camera The camera to control.
     */
    public KeyboardController(Camera camera) {
        this.camera = camera;
    }

    public void setOnSpacePressed(Runnable onSpacePressed) {
        this.onSpacePressed = onSpacePressed;
    }

    public void setOnClearPressed(Runnable onClearPressed) {
        this.onClearPressed = onClearPressed;
    }

    /**
     * Handles key press events.
     * Supported keys:
     * - WASD Camera movement
     * - +/- Zoom
     * - SPACE Toggle simulation
     * - DELETE Clear board
     * @param event The key event to process.
     */
    public void onKeyPressed(KeyEvent event) {
        switch (event.getCode()) {
            case W -> camera.moveCamera(0, MOVE_SPEED);
            case S -> camera.moveCamera(0, -MOVE_SPEED);
            case A -> camera.moveCamera(MOVE_SPEED, 0);
            case D -> camera.moveCamera(-MOVE_SPEED, 0);
            case PLUS, EQUALS -> camera.zoomFocus(1.1, camera.getPixelX(0), camera.getPixelY(0));
            case MINUS -> camera.zoomFocus(0.9, camera.getPixelX(0), camera.getPixelY(0));
            case SPACE -> {
                if (onSpacePressed != null) onSpacePressed.run();
            }
            case DELETE -> {
                if (onClearPressed != null) onClearPressed.run();
            }
        }
    }
}

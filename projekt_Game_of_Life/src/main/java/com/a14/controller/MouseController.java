package com.a14.controller;
import com.a14.json.GameConfig;
import com.a14.view.Camera;
import com.a14.model.GameBoard;
import com.a14.model.Point;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;

/**
 * Controller class responsible for handling mouse interactions.
 * It translates mouse events into Camera movements or GameBoard actions.
 * @author Tomasz Kozłowski
 */
public class MouseController {
    private static final double ZOOM_IN_FACTOR = 1.1;
    private static final double ZOOM_OUT_FACTOR = 0.9;
    private final Camera camera;
    private static boolean isRightClick(MouseEvent event){
        return event.getButton() == MouseButton.SECONDARY;
    }
    private boolean isMousePressed = false;
    private boolean drawState;
    private final GameBoard gameBoard;
    private double lastMouseX;
    private double lastMouseY;
    private Point currentHoverPoint;

    /**
     * Initializes the MouseController with the given game board.
     * A new {@link Camera} instance is created internally as part of the controller's state.
     * @param gameBoard The game board model to interact with.
     */
    public MouseController(GameBoard gameBoard, GameConfig config) {
        this.gameBoard = gameBoard;
        this.camera = new Camera(config);
    }

    public Camera getCamera() {
        return camera;
    }

    /**
     * Handles the mouse press event.
     * Records the initial mouse position to enable drag calculations.
     * In drawing mode, it also applies the initial cell change
     * @param event The mouse event containing the cursor coordinates.
     */
    public void onMousePressed(MouseEvent event) {
        this.isMousePressed = true;
        if (isRightClick(event)) {
            double mouseX = event.getX();
            double mouseY = event.getY();
            int cellX = camera.getCellX(mouseX);
            int cellY = camera.getCellY(mouseY);
            Point targetPoint = new Point(cellX, cellY);
            drawState = !gameBoard.isAlive(targetPoint);
            if (drawState) {
                gameBoard.addCell(targetPoint);
            } else {
                gameBoard.removeCell(targetPoint);
            }
        }
        lastMouseX = event.getX();
        lastMouseY = event.getY();
    }
    public void onMouseMoved(MouseEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        int cellX = camera.getCellX(mouseX);
        int cellY = camera.getCellY(mouseY);
        this.currentHoverPoint = new Point(cellX, cellY);
    }
    /**
     * Handles the mouse drag event.
     * Calculates the change in mouse position (delta) and moves the camera accordingly.
     * In drawing mode, it paints cells instead of moving the camera.
     * After moving the camera, the last known mouse position is updated.
     * @param event The mouse event containing the current cursor coordinates.
     */
    public void onMouseDragged(MouseEvent event) {
        if (isRightClick(event)) {
            double mouseX = event.getX();
            double mouseY = event.getY();
            int cellX = camera.getCellX(mouseX);
            int cellY = camera.getCellY(mouseY);
            Point targetPoint = new Point(cellX, cellY);
            if (drawState) {
                gameBoard.addCell(targetPoint);
            } else {
                gameBoard.removeCell(targetPoint);
            }
        } else {
            double currentX = event.getX();
            double currentY = event.getY();
            double deltaX = currentX - lastMouseX;
            double deltaY = currentY - lastMouseY;
            camera.moveCamera(deltaX, deltaY);
            lastMouseX = currentX;
            lastMouseY = currentY;
        }
    }
    public Point getCurrentHoverPoint() {
        return currentHoverPoint;
    }
    public boolean isMousePressed() {
        return isMousePressed;
    }
    public void onMouseReleased(MouseEvent event) {
        this.isMousePressed = false;
    }
    /**
     * Handles the mouse scroll event.
     * Zooms the camera in or out based on the vertical scroll direction.
     * @param event The scroll event containing the scroll delta information.
     */
    public void onScroll(ScrollEvent event) {
        double mouseX = event.getX();
        double mouseY = event.getY();
        double scrollDeltaY = event.getDeltaY();
        if (scrollDeltaY > 0) {
            camera.zoomFocus(ZOOM_IN_FACTOR, mouseX, mouseY);
        } else {
            camera.zoomFocus(ZOOM_OUT_FACTOR, mouseX, mouseY);
        }
    }

    /**
     * Handles the mouse click event.
     * Toggles the state of the game cell located at the clicked coordinates.
     * This action is validated by checking {@link MouseEvent#isStillSincePress()}.
     * In drawing mode, clicks are handled by onMousePressed, so this does nothing
     * to avoid double-toggling.
     */
    private com.a14.model.Pattern selectedPattern;

    public void setPattern(com.a14.model.Pattern pattern) {
        this.selectedPattern = pattern;
    }

    /**
     * Handles the mouse click event.
     * Toggles the state of the game cell located at the clicked coordinates.
     * If a pattern is selected, it places the pattern starting at the clicked location.
     * This action is validated by checking {@link MouseEvent#isStillSincePress()}.
     * In drawing mode, clicks are handled by onMousePressed, so this does nothing to avoid double-toggling.
     * @param event The mouse event triggered by the click.
     */
    public void onMouseClicked(MouseEvent event) {
        boolean isRightClick = event.getButton() == MouseButton.SECONDARY;
        if (!isRightClick && event.isStillSincePress()) {
            double mouseX = event.getX();
            double mouseY = event.getY();
            int cellX = camera.getCellX(mouseX);
            int cellY = camera.getCellY(mouseY);
            
            if (selectedPattern != null && !"Single Cell".equals(selectedPattern.name())) {
                for (Point offset : selectedPattern.offsets()) {
                    Point p = new Point(cellX + offset.x(), cellY + offset.y());
                    gameBoard.addCell(p);
                }
            } else {
                Point targetPoint = new Point(cellX, cellY);
                gameBoard.toggleCell(targetPoint);
            }
        }
    }
}

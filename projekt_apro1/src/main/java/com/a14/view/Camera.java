package com.a14.view;

import com.a14.json.GameConfig;

/**
 * Manages the simulation view, handling coordinate conversions, zooming, and panning.
 * It translates between screen space (pixels) and world space (grid coordinates).
 */
public class Camera {
    private double offsetX = 0.0;
    private double offsetY = 0.0;
    private double zoom = 0.5;
    private final double baseCellSize = 50.0;

    /**
     * Initializes the camera state based on the provided configuration.
     *
     * @param config The configuration object containing initial zoom and position.
     */
    public Camera(GameConfig config) {
        this.zoom = config.cameraZoom;
        this.offsetX = config.cameraX;
        this.offsetY = config.cameraY;
    }

    public double getZoom() { return zoom; }

    public double getOffsetX() { return offsetX; }

    public double getOffsetY() { return offsetY; }

    public int getCellX(double mouseCoordX){
        return (int) Math.floor ((mouseCoordX - offsetX) / (baseCellSize*zoom));
    }

    public int getCellY(double mouseCoordY){
        return (int) Math.floor ((mouseCoordY - offsetY) / (baseCellSize*zoom));
    }

    public double getCurrentCellSize(){
        return baseCellSize*zoom;
    }

    public double getPixelX(int cellX){
        return (cellX * baseCellSize * zoom) + offsetX;
    }

    public double getPixelY(int cellY){
        return (cellY * baseCellSize * zoom) + offsetY;
    }

    public void setOffsetX(double offsetX){
        this.offsetX = offsetX;
    }

    public void setOffsetY(double offsetY){
        this.offsetY = offsetY;
    }

    public void setZoom(double zoom){
        this.zoom = zoom;
    }

    /**
     * Moves the camera by a specified delta in screen pixels (panning).
     *
     * @param deltaScreenX The horizontal change.
     * @param deltaScreenY The vertical change.
     */
    public void moveCamera(double deltaScreenX, double deltaScreenY){
        this.offsetX += deltaScreenX;
        this.offsetY += deltaScreenY;
    }

    /**
     * Zooms the camera in or out relative to a specific focus point (e.g., mouse cursor).
     * Ensures the focus point remains stationary under the cursor.
     *
     * @param zoomFactor The multiplier for the zoom change.
     * @param mouseFocusX The screen X coordinate of the focus point.
     * @param mouseFocusY The screen Y coordinate of the focus point.
     */
    public void zoomFocus(double zoomFactor, double mouseFocusX, double mouseFocusY){
        double worldXBefore = (mouseFocusX - offsetX)/(baseCellSize*zoom);
        double worldYBefore = (mouseFocusY - offsetY)/(baseCellSize*zoom);
        this.zoom *= zoomFactor;
        this.zoom = Math.max(0.025,Math.min(this.zoom, 1.0));
        this.offsetX = mouseFocusX - (worldXBefore * zoom * baseCellSize);
        this.offsetY = mouseFocusY - (worldYBefore * zoom * baseCellSize);
    }

    /**
     * Calculates which grid cells are currently visible on the screen.
     * Used for rendering optimization (culling).
     *
     * @param screenWidth The width of the viewport.
     * @param screenHeight The height of the viewport.
     * @return A CameraBounds object containing the range of visible cell indices.
     */
    public CameraBounds getVisibleCells(double screenWidth, double screenHeight) {
        int minX = getCellX(0);
        int minY = getCellY(0);
        int maxX = getCellX(screenWidth) + 2;
        int maxY = getCellY(screenHeight) + 2;
        return new CameraBounds(minX, minY, maxX, maxY);
    }
}
package com.a14.view;

import com.a14.model.GameBoard;
import com.a14.model.Pattern;
import com.a14.model.Point;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import java.util.Set;

/**
 * GameCanvas is responsible for rendering the graphical representation of
 * Game of Life simulation. It handles drawing the background, the coordinate grid,
 * and the living cells based on the current camera view.
 */
public class GameCanvas extends Canvas {
    /** The graphics context used for all drawing operations on this canvas. */
    private GraphicsContext gc;

    /**
     * Initializes a new GameCanvas with a default size.
     * The GraphicsContext is retrieved internally from the Canvas.
     */
    public GameCanvas(GraphicsContext gc){
        super(800,600); //window size, can be changed later
        this.gc=this.getGraphicsContext2D();
    }

    /**
     * Clears the canvas and redraws the game state.
     * @param gameBoard The model containing the set of currently alive cells.
     * @param camera    The camera object used to translate grid coordinates
     * into screen pixels and determine visibility.
     */
    public void draw(GameBoard gameBoard, Camera camera) {
        double width = getWidth();
        double height = getHeight();

        //draw background
        gc.setFill(Color.web("#e0e0e0")); // very light grey
        gc.fillRect(0, 0, width, height);

        //determine visible area
        CameraBounds bounds = camera.getVisibleCells(width, height);
        double cellSize = camera.getCurrentCellSize();

        //draw grid
        gc.setStroke(Color.web("#bbbbbb")); // grey
        gc.setLineWidth(1.0);

        //draws vertical lines
        for (int x = bounds.minX(); x <= bounds.maxX(); x++) {
            double pixX = camera.getPixelX(x);
            gc.strokeLine(pixX, 0, pixX, height);
        }

        //draws horizontal lines
        for (int y = bounds.minY(); y <= bounds.maxY(); y++) {
            double pixY = camera.getPixelY(y);
            gc.strokeLine(0, pixY, width, pixY);
        }

        //get and draw living cells
        Set<Point> liveCells = gameBoard.getLiveCells();
        gc.setFill(Color.web("#36bb37")); // green

        for (Point p : liveCells) {
            if (p.x() >= bounds.minX() && p.x() <= bounds.maxX() &&
                    p.y() >= bounds.minY() && p.y() <= bounds.maxY()) {

                double px = camera.getPixelX(p.x());
                double py = camera.getPixelY(p.y());

                gc.fillRect(px, py, cellSize, cellSize);
            }
        }
    }
    public void drawGhost(Pattern pattern, Point hoverPoint, Camera camera) {
        if (pattern == null || hoverPoint == null) return;

        GraphicsContext gc = getGraphicsContext2D();
        gc.setFill(Color.rgb(100, 100, 100, 0.4));

        double cellSize = camera.getZoom() * 50.0;
        double offsetX = camera.getOffsetX();
        double offsetY = camera.getOffsetY();

        if ("Single Cell".equals(pattern.name())) {
            double screenX = (hoverPoint.x() * 50.0) * camera.getZoom() + offsetX;
            double screenY = (hoverPoint.y() * 50.0) * camera.getZoom() + offsetY;
            gc.fillRect(screenX, screenY, cellSize, cellSize);
        } else {
            for (Point offset : pattern.offsets()) {
                int targetX = hoverPoint.x() + offset.x();
                int targetY = hoverPoint.y() + offset.y();

                double screenX = (targetX * 50.0) * camera.getZoom() + offsetX;
                double screenY = (targetY * 50.0) * camera.getZoom() + offsetY;

                if (screenX + cellSize > 0 && screenY + cellSize > 0 &&
                        screenX < getWidth() && screenY < getHeight()) {
                    gc.fillRect(screenX, screenY, cellSize, cellSize);
                }
            }
        }
    }
}

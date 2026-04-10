package com.a14.json;

import com.a14.model.GameBoard;
import com.a14.model.GameRules;
import com.a14.view.Camera;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;

/**
 * Handles the loading of saved game states from the file system.
 * <p>
 * This class is responsible for deserializing JSON data back into a {@link GameState} object
 * and applying those values to the active {@link GameBoard}, {@link Camera}, and {@link GameRules}.
 * It returns the loaded state object so the main controller can update simulation speed.
 */
public class SaveLoader {

    /**
     * Restores the simulation state from a specified JSON file.
     * <p>
     * This method performs the following steps:
     * <ol>
     * <li>Deserializes the JSON file into a temporary state object.</li>
     * <li>Clears and repopulates the game board with live cells.</li>
     * <li>Updates the camera's position and zoom.</li>
     * <li>Updates the game rules (birth and survival conditions).</li>
     * </ol>
     *
     * @param file   The JSON file containing the saved state.
     * @param board  The active game board to be updated.
     * @param camera The active camera to be repositioned.
     * @param rules  The active game rules object to be updated.
     * @return The deserialized {@link GameState} object (containing e.g., speed), or null if loading failed.
     */
    public GameState loadGame(File file, GameBoard board, Camera camera, GameRules rules) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

            GameState state = mapper.readValue(file, GameState.class);

            board.clearLiveCells();

            if (state.liveCells != null) {
                for (GameState.SavedCell cell : state.liveCells) {
                    board.reviveCell(cell.x(), cell.y());
                }
            }

            camera.setOffsetX(state.cameraX);
            camera.setOffsetY(state.cameraY);
            camera.setZoom(state.zoom);

            if (state.bornRules != null) {
                rules.setBirthConditions(state.bornRules);
            }
            if (state.surviveRules != null) {
                rules.setSurvivalConditions(state.surviveRules);
            }

            System.out.println("Game loaded successfully from: " + file.getName());
            return state;

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Error loading game: " + e.getMessage());
            return null;
        }
    }
}
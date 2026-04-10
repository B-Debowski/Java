package com.a14.json;

import com.a14.model.GameBoard;
import com.a14.model.GameRules;
import com.a14.view.Camera;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class GameSaver {

    /**
     * Zapisuje aktualny stan gry do wybranego pliku.
     */
    public void saveGame(File file, GameBoard board, Camera camera, GameRules rules) {
        /**
         *
         * Saves the current game state to a specified file.
         * <p>
         * This method extracts data from the {@link GameBoard}, {@link Camera} and {@link GameRules}
         * wraps it in a {@link GameState} DTO (Data Transfer Object), and serializes
         * it to JSON format with pretty printing enabled.
         *
         *@param file   The destination file selected by the user.
         *@param board  The game board containing the set of currently live cells.
         *@param camera The camera object containing current view coordinates and zoom level.
         *@param rules  Game rules that contain birth and survive conditions.
         */
        try {
            GameState state = new GameState();

            state.cameraX = camera.getOffsetX();
            state.cameraY = camera.getOffsetY();
            state.zoom = camera.getZoom();
            state.surviveRules = rules.getSurvivalConditions();
            state.bornRules = rules.getBirthConditions();

            List<GameState.SavedCell> cellsToSave = new ArrayList<>();

            for (var point : board.getLiveCells()) {
                cellsToSave.add(new GameState.SavedCell(point.x(), point.y()));
            }
            state.liveCells = cellsToSave;

            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            mapper.writeValue(file, state);

            System.out.println("Gra zapisana pomyślnie: " + file.getAbsolutePath());

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Błąd zapisu gry: " + e.getMessage());
        }
    }
}
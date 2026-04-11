package json;

import com.a14.json.GameConfig;
import com.a14.json.GameSaver;
import com.a14.json.GameState;
import com.a14.json.SaveLoader;
import com.a14.model.GameBoard;
import com.a14.model.GameRules; // <--- Dodano import
import com.a14.view.Camera;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

public class JsonTests {
    @TempDir
    Path tempDir;

    /**
     * TEST 1: Sprawdza, czy GameConfig poprawnie zapisuje się i wczytuje.
     * Weryfikuje działanie Jacksona na prostej klasie.
     */
    @Test
    public void testGameConfigSerialization() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        GameConfig config = new GameConfig();
        config.appName = "Testowa Aplikacja";
        config.initialSpeed = 999;
        config.born = List.of(3, 6, 8);

        //(Symulacja zapisu do Stringa i odczytu z powrotem)
        String jsonString = mapper.writeValueAsString(config);
        GameConfig loadedConfig = mapper.readValue(jsonString, GameConfig.class);

        Assertions.assertEquals("Testowa Aplikacja", loadedConfig.appName);
        Assertions.assertEquals(999, loadedConfig.initialSpeed);
        Assertions.assertEquals(3, loadedConfig.born.size());
        Assertions.assertTrue(loadedConfig.born.contains(6));
    }

    /**
     * TEST 2: Test integracyjny zapisu i odczytu stanu gry.
     * Sprawdza współpracę GameSaver, SaveLoader i formatu GameState.
     */
    @Test
    public void testGameSaveAndLoadRoundTrip() {
        File saveFile = tempDir.resolve("save_test.json").toFile();

        // 1. Setup mapy
        GameBoard originalBoard = new GameBoard();
        originalBoard.reviveCell(10, 10);
        originalBoard.reviveCell(-5, -5);

        // 2. Setup Kamery
        GameConfig dummyConfig = new GameConfig();
        Camera originalCamera = new Camera(dummyConfig);
        originalCamera.setZoom(2.5);
        originalCamera.setOffsetX(123.0);
        originalCamera.setOffsetY(456.0);

        // 3. Setup Zasad i Prędkości
        GameConfig rulesConfig = new GameConfig();
        rulesConfig.born = List.of(3, 6);
        rulesConfig.survive = List.of(2, 3);
        GameRules originalRules = new GameRules(rulesConfig);
        long originalSpeed = 500_000L;

        GameSaver saver = new GameSaver();
        saver.saveGame(saveFile, originalBoard, originalCamera, originalRules);

        // 3. ODCZYT
        GameBoard newBoard = new GameBoard();
        Camera newCamera = new Camera(dummyConfig);
        GameRules newRules = new GameRules(dummyConfig);
        SaveLoader loader = new SaveLoader();
        GameState loadedState = loader.loadGame(saveFile, newBoard, newCamera, newRules);

        //4. WERYFIKACJA

        // Dane kamery
        Assertions.assertEquals(2.5, newCamera.getZoom(), 0.0001, "Zoom musi się zgadzać");
        Assertions.assertEquals(123.0, newCamera.getOffsetX(), 0.0001, "OffsetX musi się zgadzać");

        // Dane planszy
        var loadedCells = newBoard.getLiveCells();
        Assertions.assertEquals(2, loadedCells.size(), "Powinny być wczytane 2 żywe komórki");

        // Dane Zasad
        Assertions.assertNotNull(loadedState, "Stan gry nie może być null");
        Assertions.assertEquals(originalRules.getBirthConditions(), newRules.getBirthConditions(), "Zasady 'Born' muszą być takie same");
    }

    @Test
    public void testResilienceToUnknownProperties() throws Exception {
        //JSON z nieznanym polem "hackerField"
        String maliciousJson = """
            {
                "cameraX": 50.0,
                "cameraY": 50.0,
                "zoom": 1.0,
                "hackerField": "To pole nie istnieje w klasie GameState!"
            }
            """;

        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);


        // Próbujemy wczytać ten JSON do klasy GameState
        GameState state = mapper.readValue(maliciousJson, GameState.class);

        Assertions.assertNotNull(state);
        Assertions.assertEquals(50.0, state.cameraX);
    }
}
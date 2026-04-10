package com.a14.json;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.InputStream;


/**
 * Zrobiłem delikatne zmiany żebym mógł zapisywać config w runtime, bo chcę zrobić okienko do settingsów.
 * Logika I/O plików przeniesiona do ConfigIO
 * - Tomek
 */
public class ExternalFileLoader {
    /**
     * Read JSON file that keeps data about GameConfig.
     * @return GameConfig
     */
    public GameConfig loadConfig() {
        GameConfig local = ConfigIO.tryLoadLocal();
        if (local != null) return local;

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("config.json");
            if (is == null) {
                System.out.println("config.json not found");
            }
            GameConfig config = objectMapper.readValue(is, GameConfig.class);
            return config;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new GameConfig();
    }

    public void saveConfig(GameConfig config) {
        ConfigIO.save(config);
    }
}

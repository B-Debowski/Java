package com.a14.json;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;

/**
 * Helper class for low-level configuration I/O operations.
 * Handles reading and writing JSON files to the local filesystem,
 * abstracting file path logic for Dev vs Prod environments.
 * @author Tomasz Kozłowski
 */
public class ConfigIO {
    private static final String DEV_CONFIG_PATH = "src/main/resources/config.json";
    private static final String PROD_CONFIG_PATH = "config.json";

    /**
     * Attempts to load configuration from local files.
     * Checks 'src/main/resources/config.json' (Dev) then 'config.json' (Prod).
     * @return The loaded GameConfig or null if no local file exists.
     */
    public static GameConfig tryLoadLocal() {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            
            File devFile = new File(DEV_CONFIG_PATH);
            if (devFile.exists()) {
                return objectMapper.readValue(devFile, GameConfig.class);
            }
            
            File prodFile = new File(PROD_CONFIG_PATH);
            if (prodFile.exists()) {
                return objectMapper.readValue(prodFile, GameConfig.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Saves the configuration to a local file.
     * Prefers 'src/main/resources/config.json' if it exists or if the folder structure exists.
     * Otherwise defaults to 'config.json' in the working directory.
     * @param config The configuration object to save.
     */
    public static void save(GameConfig config) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
            File targetFile;
            File devFile = new File(DEV_CONFIG_PATH);
            if (devFile.exists() || new File("src/main/resources").exists()) {
                 if (!devFile.getParentFile().exists()) {
                     devFile.getParentFile().mkdirs();
                 }
                 targetFile = devFile;
            } else {
                targetFile = new File(PROD_CONFIG_PATH);
            }
            objectMapper.writeValue(targetFile, config);
            System.out.println("Config saved to: " + targetFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to save config: " + e.getMessage());
        }
    }
}

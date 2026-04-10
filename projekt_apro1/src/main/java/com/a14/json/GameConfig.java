package com.a14.json;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameConfig {
    /**
     * Represents the configuration data for the Game of Life application.
     * <p>
     * This class acts as a Data Transfer Object (DTO) used for serializing and deserializing
     * game settings (rules, camera position, speed) to and from a JSON file.
     * It is compatible with the Jackson library.
     */
        public String appName;
        public List<Integer> born = new ArrayList<Integer>();
        public List<Integer> survive = new ArrayList<Integer>();
        public int initialSpeed;
        public double cameraZoom;
        public double cameraX;
        public double cameraY;

        public GameConfig() {
        }
    }


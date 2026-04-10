package com.a14.view;

import com.a14.controller.MouseController;
import com.a14.json.ExternalFileLoader;
import com.a14.json.GameConfig;
import com.a14.json.GameSaver;
import com.a14.json.SaveLoader;
import com.a14.model.*;
import javafx.animation.AnimationTimer;
import javafx.collections.FXCollections;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.File;
import java.util.Objects;

public class GameWindow {
    /**
     * Zmieniłem na metodę instancyjną bo robię żeby można to zmieniać w settingsach
     * w trakcie działania
     * - Tomek
     */
    private long simulationSpeedNanos = 100_000_000;
    private SimulationEngine engine;
    private GameBoard gameBoard;
    private MouseController mouseController;
    private com.a14.controller.KeyboardController keyboardController;
    private GameCanvas canvas;
    private Camera camera;
    private javafx.scene.control.ComboBox<Pattern> patternCombo;
    private boolean isPaused = true;
    private long lastUpdate = 0;

    public void show(Stage primaryStage) {
        setupGame();
        Scene scene = createScene();
        primaryStage.setMinWidth(900);
        primaryStage.setMinHeight(450);
        primaryStage.setTitle("InfGOL");
        primaryStage.setScene(scene);
        try {
            java.io.InputStream iconStream = getClass().getResourceAsStream("/icon.png");
            if (iconStream != null) {
                primaryStage.getIcons().add(new javafx.scene.image.Image(iconStream));
            }
        } catch (Exception e) {
            System.out.println("Could not load icon");
        }
        primaryStage.show();
        drawFrame();
        startGameLoop();
    }

    private void setupGame() {
        gameBoard = new GameBoard();
        ExternalFileLoader loader = new ExternalFileLoader();
        GameConfig config = loader.loadConfig();
        GameRules rules = new GameRules(config);
        if (config.initialSpeed > 0) {
            this.simulationSpeedNanos = config.initialSpeed * 100_000_000L;
        }
        engine = new SimulationEngine(gameBoard, rules);
        mouseController = new MouseController(gameBoard, config);
        camera = mouseController.getCamera();
        keyboardController = new com.a14.controller.KeyboardController(camera);
        canvas = new GameCanvas(null);
    }

    private Scene createScene() {
        Button btnStartStop = new Button("Start");
        btnStartStop.setPrefWidth(100);
        btnStartStop.setPrefHeight(36);
        btnStartStop.getStyleClass().add("button-start");
        btnStartStop.setFocusTraversable(false);

        Button btnClear = new Button("Clear");
        btnClear.setPrefWidth(100);
        btnClear.getStyleClass().add("button");
        btnClear.setFocusTraversable(false);

        btnStartStop.setOnAction(e -> {
            isPaused = !isPaused;
            btnStartStop.getStyleClass().removeAll("button-start", "button-stop");
            if (isPaused) {
                btnStartStop.setText("Start");
                btnStartStop.getStyleClass().add("button-start");
            } else {
                btnStartStop.setText("Stop");
                btnStartStop.getStyleClass().add("button-stop");
            }
        });

        btnClear.setOnAction(e -> {
            isPaused = true;
            btnStartStop.setText("Start");
            btnStartStop.getStyleClass().removeAll("button-start", "button-stop");
            btnStartStop.getStyleClass().add("button-start");
            engine.getBoard().clearLiveCells();
            drawFrame();
        });



        Button btnSave = new Button("Save");
        btnSave.setPrefWidth(100);
        btnSave.getStyleClass().add("button");
        btnSave.setFocusTraversable(false);
        btnSave.setOnAction(e -> {
            isPaused = true;
            btnSave.setText("Save");
            FileChooser filechooser = new FileChooser();
            filechooser.setTitle("Save the game");
            filechooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));
            filechooser.setInitialFileName("save_1.json");

            File file = filechooser.showSaveDialog(null);
            if (file != null) {
                GameSaver saver = new GameSaver();
                saver.saveGame(file,gameBoard,camera,engine.getRules());
            }

        });
        Button btnLoad = new Button("Load");
        btnLoad.setPrefWidth(100);
        btnLoad.getStyleClass().add("button");
        btnLoad.setFocusTraversable(false);

        btnLoad.setOnAction(e -> {
            isPaused = true;
            btnStartStop.setText("Start");
            btnStartStop.getStyleClass().removeAll("button-start", "button-stop");
            btnStartStop.getStyleClass().add("button-start");

            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Wczytaj grę");
            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("JSON", "*.json"));

            File file = fileChooser.showOpenDialog(null);

            if (file != null) {

                SaveLoader loader = new SaveLoader();
                loader.loadGame(file, gameBoard, camera,engine.getRules());

                drawFrame();
            }
        });
        Button btnSettings = new Button("Settings");
        btnSettings.setPrefWidth(100);
        btnSettings.getStyleClass().add("button");
        btnSettings.setFocusTraversable(false);
        btnSettings.setOnAction(e -> new SettingsDialog(engine, camera).showAndWait());

        patternCombo = new ComboBox<>();
        patternCombo.setItems(
                FXCollections.observableArrayList(PatternLibrary.getPatterns()));
        patternCombo.setConverter(new StringConverter<Pattern>() {
            @Override
            public String toString(Pattern object) {
                return object.name();
            }

            @Override
            public Pattern fromString(String string) {
                return patternCombo.getItems().stream().filter(p -> p.name().equals(string)).findFirst().orElse(null);
            }
        });
        patternCombo.getSelectionModel().selectFirst();
        patternCombo.getStyleClass().add("combo-box");
        patternCombo.setFocusTraversable(false);
        patternCombo.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            mouseController.setPattern(newVal);
        });
        mouseController.setPattern(patternCombo.getSelectionModel().getSelectedItem());

        HBox simSpeed = new HBox(5);
        Label sliderLabel = new Label("Speed:");
        Slider speedSlider = new Slider(1, 50, 10);
        speedSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
            double fps = newVal.doubleValue();
            this.simulationSpeedNanos = (long) (1_000_000_000.0 / fps);
        });
        simSpeed.getChildren().addAll(sliderLabel, speedSlider);

        StackPane canvasHolder = new StackPane(canvas);
        canvasHolder.setMinHeight(0);

        btnStartStop.setMinWidth(0);
        btnClear.setMinWidth(0);
        btnSettings.setMinWidth(0);
        btnSave.setMinWidth(0);
        btnLoad.setMinWidth(0);

        simSpeed.setMinWidth(0);
        patternCombo.setMinWidth(0);
        speedSlider.setMinWidth(0);
        speedSlider.setMaxWidth(Double.MAX_VALUE);

        GridPane bottomControls = new GridPane();
        bottomControls.setPadding(new Insets(10));
        bottomControls.setHgap(14);
        bottomControls.setVgap(6);
        bottomControls.setAlignment(Pos.CENTER);
        bottomControls.getStyleClass().add("bottom-bar");
        bottomControls.setPrefWidth(1);
        bottomControls.setMinWidth(0);

        bottomControls.getColumnConstraints().clear();
        for (int i = 0; i < 7; i++) {
            ColumnConstraints cc = new ColumnConstraints();
            cc.setPercentWidth(100.0 / 7);
            cc.setMinWidth(0);
            cc.setHalignment(HPos.CENTER);
            bottomControls.getColumnConstraints().add(cc);
        }

        bottomControls.add(patternCombo, 0, 0);
        bottomControls.add(simSpeed,     1, 0);
        bottomControls.add(btnStartStop,3, 0);
        bottomControls.add(btnClear,     2, 0);
        bottomControls.add(btnSettings,  4, 0);
        bottomControls.add(btnSave,     5, 0);
        bottomControls.add(btnLoad,  6, 0);

        BorderPane root = new BorderPane();
        root.setCenter(canvasHolder);
        root.setBottom(bottomControls);

        canvas.setFocusTraversable(true);
        canvas.setOnMousePressed(e -> mouseController.onMousePressed(e));
        canvas.setOnMouseMoved(e -> {
            mouseController.onMouseMoved(e);
            drawFrame();
        });
        canvas.setOnMouseDragged(e -> {
            mouseController.onMouseDragged(e);
            drawFrame();
        });
        canvas.setOnScroll(e -> {
            mouseController.onScroll(e);
            drawFrame();
        });
        canvas.setOnMouseClicked(e -> {
            mouseController.onMouseClicked(e);
            drawFrame();
        });
        canvas.setOnMouseReleased(e -> {
            mouseController.onMouseReleased(e);
            drawFrame(); // Odśwież, żeby duch wrócił po puszczeniu
        });
        keyboardController.setOnSpacePressed(() -> btnStartStop.fire());
        keyboardController.setOnClearPressed(() -> btnClear.fire());
        canvas.widthProperty().bind(canvasHolder.widthProperty());
        canvas.heightProperty().bind(canvasHolder.heightProperty());

        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );
        scene.setOnKeyPressed(e -> {
            keyboardController.onKeyPressed(e);
            drawFrame();
        });

        return scene;
    }

    private void startGameLoop() {
        AnimationTimer timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if (!isPaused && (now - lastUpdate >= simulationSpeedNanos)) {
                    engine.calculateNextGeneration();
                    lastUpdate = now;
                }
                drawFrame();
            }
        };
        timer.start();
    }

    private void drawFrame() {
        canvas.draw(gameBoard, camera);
        if(!mouseController.isMousePressed()) {
            Pattern currentPattern = patternCombo.getSelectionModel().getSelectedItem();
            com.a14.model.Point hoverPoint = mouseController.getCurrentHoverPoint();
            canvas.drawGhost(currentPattern, hoverPoint, camera);
        }
    }
}
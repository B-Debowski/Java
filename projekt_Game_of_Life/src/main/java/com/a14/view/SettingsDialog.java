package com.a14.view;
import com.a14.json.ExternalFileLoader;
import com.a14.json.GameConfig;
import com.a14.model.*;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.util.StringConverter;

import java.util.Objects;
import java.util.stream.Collectors;
import java.util.Set;

/**
 * Dialog for modifying game configuration at runtime.
 * Allows changing birth/survival rules and simulation speed.
 * Persists changes using ExternalFileLoader.
 * @author Tomasz Kozłowski
 */
public class SettingsDialog extends Dialog<GameConfig> {

    private final SimulationEngine engine;
    private final Camera camera;

    private final ToggleButton[] bornButtons = new ToggleButton[9];
    private final ToggleButton[] surviveButtons = new ToggleButton[9];

    private MenuButton libraryMenu;
    private final String DEFAULT_LABEL = "Library Presets";
    private boolean isUpdatingFromPreset = false;

    /**
     * Creates a new configuration dialog.
     * @param engine The simulation engine to update rules for.
     * @param camera The camera to retrieve current state from.
     */
    public SettingsDialog(SimulationEngine engine, Camera camera) {
        this.engine = engine;
        this.camera = camera;

        setTitle("Game Settings");
        setHeaderText("Modify Game Rules");

        getDialogPane().getStylesheets().add(
                Objects.requireNonNull(getClass().getResource("/style.css")).toExternalForm()
        );

        ButtonType applyButtonType = new ButtonType("Apply", ButtonBar.ButtonData.OK_DONE);
        getDialogPane().getButtonTypes().addAll(applyButtonType, ButtonType.CANCEL);

        Button applyButton = (Button) getDialogPane().lookupButton(applyButtonType);
        applyButton.getStyleClass().add("button-apply");

        GridPane grid = createGrid();
        getDialogPane().setContent(grid);

        setResultConverter(dialogButton -> {
            if (dialogButton == applyButtonType) {
                applyChanges();
            }
            return null;
        });
    }

    private GridPane createGrid() {
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        Set<Integer> bornInitial = engine.getRules().getBirthConditions();
        Set<Integer> surviveInitial = engine.getRules().getSurvivalConditions();

        HBox bornSelect = createRuleSelector(bornButtons, bornInitial);
        HBox surviveSelect = createRuleSelector(surviveButtons, surviveInitial);
        MenuButton rulesLibrary = createLibraryMenu();

        grid.add(new Label("Rules presets:"), 0, 0);
        grid.add(rulesLibrary, 1, 0);
        grid.add(new Label("Born (neighbors):"), 0, 2);
        grid.add(bornSelect, 1,2);
        grid.add(new Label("Survive (neighbors):"), 0, 3);
        grid.add(surviveSelect, 1,3);
        return grid;
    }

    private MenuButton createLibraryMenu() {
        libraryMenu = new MenuButton(DEFAULT_LABEL);
        libraryMenu.getStyleClass().add("menu-button-library");

        for (RuleSet rs : RulesLibrary.getRuleSets()) {
            MenuItem item = new MenuItem(rs.name());
            item.setOnAction(e -> {
                applyPresetToToggles(rs);
                libraryMenu.setText(rs.name());
            });
            libraryMenu.getItems().add(item);
        }

        return libraryMenu;
    }

    private void applyPresetToToggles(RuleSet ruleSet) {
        isUpdatingFromPreset = true;
        for (int i = 0; i <= 8; i++) {
            bornButtons[i].setSelected(ruleSet.born().contains(i));
            surviveButtons[i].setSelected(ruleSet.survive().contains(i));
        }
        isUpdatingFromPreset = false;
    }

    private HBox createRuleSelector(ToggleButton[] buttons, Set<Integer> initial) {
        for (int i = 0; i <= 8; i++) {
            ToggleButton tb = new ToggleButton(String.valueOf(i));
            tb.getStyleClass().add("rule-toggle");
            tb.setPrefWidth(35);
            tb.setSelected(initial.contains(i));
            buttons[i] = tb;

            tb.selectedProperty().addListener((obs, wasSelected, isNowSelected) -> {
                if (!isUpdatingFromPreset) {
                    libraryMenu.setText(DEFAULT_LABEL);
                }
            });
        }

        Button btnClr = new Button("Clr");
        btnClr.getStyleClass().add("rule-Clr");
        btnClr.setOnAction(e -> {
            for (ToggleButton tb : buttons) {
                tb.setSelected(false);
            }
        });

        btnClr.setOnMouseEntered(e -> {
            for (ToggleButton tb : buttons) {
                if (tb.isSelected()) {
                    tb.setStyle("-fx-background-color: #93E497;");
                }
            }
        });

        btnClr.setOnMouseExited(e -> {
            for (ToggleButton tb : buttons) {
                tb.setStyle("");
            }
        });

        Button btnAll = new Button("All");
        btnAll.getStyleClass().add("rule-All");
        btnAll.setOnAction(e -> {
            for (ToggleButton tb : buttons) {
                tb.setSelected(true);
            }
        });

        btnAll.setOnMouseEntered(e -> {
            for (ToggleButton tb : buttons) {
                if (!tb.isSelected()) {
                    tb.setStyle("-fx-background-color: #93E497;");
                }
            }
        });

        btnAll.setOnMouseExited(e -> {
            for (ToggleButton tb : buttons) {
                tb.setStyle("");
            }
        });

        HBox box = new HBox(0);
        box.getStyleClass().add("rule-toggle-box");
        box.getChildren().add(btnClr);
        box.getChildren().addAll(buttons);
        box.getChildren().add(btnAll);

        return box;
    }

    private Set<Integer> getSelectedIndices(ToggleButton[] buttons) {
        return java.util.stream.IntStream.range(0, buttons.length)
                .filter(i -> buttons[i].isSelected())
                .boxed()
                .collect(Collectors.toSet());
    }

    private void applyChanges() {
        Set<Integer> born = getSelectedIndices(bornButtons);
        Set<Integer> survive = getSelectedIndices(surviveButtons);

        engine.setGameRules(new GameRules(born, survive));

        GameConfig newConfig = new GameConfig();
        newConfig.born = new java.util.ArrayList<>(born);
        newConfig.survive = new java.util.ArrayList<>(survive);
        newConfig.cameraZoom = camera.getZoom();
        newConfig.cameraX = camera.getOffsetX();
        newConfig.cameraY = camera.getOffsetY();

        new ExternalFileLoader().saveConfig(newConfig);
    }
}

package com.a14.app;

import com.a14.view.GameWindow;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) {
        GameWindow window = new GameWindow();
        window.show(primaryStage);
    }


    public static void main(String[] args) {
        launch(args);
    }
}
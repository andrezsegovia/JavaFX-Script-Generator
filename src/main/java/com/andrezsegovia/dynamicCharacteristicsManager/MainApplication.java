package com.andrezsegovia.dynamicCharacteristicsManager;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class MainApplication extends Application {

    private Button btnImport;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("/view/sample.fxml"));
        primaryStage.setTitle("Import Files");
        primaryStage.setScene(new Scene(root, 500, 200));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}

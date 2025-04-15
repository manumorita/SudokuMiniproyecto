package com.example.sudokuproyecto2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class to launch the Sudoku application using JavaFX.
 * <p>
 * Initializes the primary stage and loads the main FXML layout.
 */
public class Main extends Application {

    /**
     * Starts the JavaFX application and sets up the main window.
     *
     * @param primaryStage the primary stage for this application
     * @throws Exception if the FXML file cannot be loaded
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Load the FXML layout without a leading '/'
        Parent root = FXMLLoader.load(getClass().getResource("sudoku.fxml"));

        // Set the main window title and scene
        primaryStage.setTitle("GatuSudoku - Mini Proyecto");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    /**
     * Entry point of the application.
     *
     * @param args the command-line arguments passed to the application
     */
    public static void main(String[] args) {
        launch(args);
    }
}

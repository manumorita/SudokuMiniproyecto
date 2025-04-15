package com.example.sudokuproyecto2;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Clase principal para ejecutar la aplicación Sudoku en JavaFX.
 */
public class Main extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        // Usar la ruta relativa sin el '/' al inicio
        Parent root = FXMLLoader.load(getClass().getResource("sudoku.fxml"));

        // Configurar la ventana principal
        primaryStage.setTitle("Sudoku 6x6 - Mini Proyecto");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}


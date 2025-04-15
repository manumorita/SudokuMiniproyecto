package com.example.sudokuproyecto2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

public class SudokuStage extends Stage {

    public SudokuStage() throws IOException {
        // Cargar el archivo FXML y asignarlo al BorderPane
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sudoku.fxml"));
        BorderPane borderPane = loader.load();

        // Establecer el título y la escena
        this.setTitle("Sudoku 6x6 - Mini Proyecto");
        this.setScene(new Scene(borderPane));
    }
}

package com.example.sudokuproyecto2.view;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Custom JavaFX {@link Stage} for displaying the main Sudoku game interface.
 * <p>
 * This class loads the FXML layout from {@code /sudoku.fxml} and initializes
 * the window with a specific title and layout.
 */
public class SudokuStage extends Stage {

    /**
     * Constructs the main game window and loads its layout from the corresponding FXML file.
     *
     * @throws IOException if the FXML file cannot be loaded
     */
    public SudokuStage() throws IOException {
        // Load the FXML file and assign it to the BorderPane
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/sudoku.fxml"));
        BorderPane borderPane = loader.load();

        // Set the window title and scene
        this.setTitle("GatuSudoku - Mini Proyecto");
        this.setScene(new Scene(borderPane));
    }
}
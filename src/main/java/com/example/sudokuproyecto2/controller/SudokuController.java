package com.example.sudokuproyecto2.controller;

import com.example.sudokuproyecto2.model.Sudoku;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.control.TextField;

public class SudokuController {

    @FXML private GridPane sudokuGrid;
    @FXML private Button btnHelp;
    @FXML private Button btnSolve;
    @FXML private Label lblMessage;

    private final TextField[][] cells = new TextField[6][6];
    private Sudoku sudoku;
    private int helpUses = 0;

    @FXML
    public void initialize() {
        // Cargar la fuente personalizada
        Font.loadFont(getClass().getResourceAsStream("/com/example/sudokuproyecto2/fonts/BubblegumSans-Regular.ttf"), 16);

        sudoku = new Sudoku(6, 2, 3, 15);
        renderBoard();
    }

    private void renderBoard() {
        sudokuGrid.getChildren().clear();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int value = sudoku.rows.get(row).get(col);
                TextField tf = new TextField();
                tf.setPrefSize(50, 50);
                tf.setFont(Font.font("Bubblegum Sans", 16));
                tf.setStyle("-fx-alignment: center; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");

                if (value != 0) {
                    tf.setText(String.valueOf(value));
                    tf.setDisable(true);
                    tf.setStyle("-fx-background-color: #FFD8D9; -fx-border-color: none; -fx-alignment: center; -fx-text-fill: #F09696;");
                } else {
                    int finalRow = row;
                    int finalCol = col;
                    tf.textProperty().addListener((observable, oldValue, newValue) -> handleUserInput(tf, finalRow, finalCol, newValue));
                }

                cells[row][col] = tf;
                sudokuGrid.add(tf, col, row);
            }
        }
    }

    private void handleUserInput(TextField tf, int row, int col, String newValue) {
        if (newValue.isEmpty()) {
            tf.setStyle("-fx-border-color: none; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
            lblMessage.setText("");
            return;
        }

        if (!newValue.matches("[1-6]")) {
            tf.setStyle("-fx-border-color: #FF6F61; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
            lblMessage.setText("Solo se permiten números del 1 al 6.");
            return;
        }

        int value = Integer.parseInt(newValue);
        Sudoku.Coordinate coord = new Sudoku.Coordinate(row, col);

        if (!sudoku.available_cells.contains(coord)) {
            tf.setStyle("-fx-border-color: #FF6F61; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
            lblMessage.setText("Celda ya llenada.");
            return;
        }

        if (!sudoku.is_valid_user_move(value, row, col)) {
            tf.setStyle("-fx-border-color: #FF6F61; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
            lblMessage.setText("Movimiento inválido: número repetido en fila, columna o bloque.");
        } else {
            tf.setStyle("-fx-border-color: #F09696; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
            lblMessage.setText("");
        }
    }

    @FXML private void onHelpClicked() {
        if (helpUses >= 5) {
            lblMessage.setText("Límite de ayudas alcanzado.");
            btnHelp.setDisable(true);
            return;
        }
        if (sudoku.help()) {
            sudoku.applyHelp();
            helpUses++;
            if (helpUses >= 5) {
                btnHelp.setDisable(true);
            }
            renderBoard();
            lblMessage.setText("Sugerencia aplicada. Ayudas restantes: " + (5 - helpUses));
        } else {
            lblMessage.setText("No se pudo aplicar ayuda.");
        }
    }

    @FXML private void onSolveClicked() {
        sudoku.solve_board();
        renderBoard();
        lblMessage.setText("Sudoku resuelto.");
    }

    @FXML private void onNewClicked() {
        sudoku = new Sudoku(6, 2, 3, 15);
        helpUses = 0;
        btnHelp.setDisable(false);
        renderBoard();
        lblMessage.setText("Nuevo juego iniciado.");
    }

    @FXML private void onExitClicked() {
        System.exit(0);
    }
}

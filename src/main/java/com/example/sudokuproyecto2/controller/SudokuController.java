package com.example.sudokuproyecto2.controller;
import com.example.sudokuproyecto2.model.Sudoku; // Importación de la clase Sudoku
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;

public class SudokuController {

    @FXML private GridPane sudokuGrid;
    @FXML private Button btnHelp;
    @FXML private Button btnSolve;
    @FXML private Label lblMessage;

    private final TextField[][] cells = new TextField[6][6];
    private Sudoku sudoku;

    @FXML
    public void initialize() {
        sudoku = new Sudoku(6, 2, 3, 15);  // Inicialización del objeto Sudoku
        renderBoard();
    }

    private void renderBoard() {
        sudokuGrid.getChildren().clear();
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                int value = sudoku.rows.get(row).get(col);
                TextField tf = new TextField();
                tf.setPrefSize(50, 50);
                tf.setFont(new Font(18));
                tf.setStyle("-fx-alignment: center;");
                if (value != 0) {
                    tf.setText(String.valueOf(value));
                    tf.setDisable(true);
                    tf.setStyle("-fx-background-color: #ddd; -fx-alignment: center;");
                } else {
                    int finalRow = row;
                    int finalCol = col;
                    tf.setOnAction(e -> handleUserInput(tf, finalRow, finalCol));
                }
                cells[row][col] = tf;
                sudokuGrid.add(tf, col, row);
            }
        }
    }

    private void handleUserInput(TextField tf, int row, int col) {
        String text = tf.getText().trim();
        if (!text.matches("[1-6]")) {
            lblMessage.setText("Solo se permiten números del 1 al 6.");
            tf.setText("");
            return;
        }
        int value = Integer.parseInt(text);
        Sudoku.Coordinate coord = new Sudoku.Coordinate(row, col);
        if (!sudoku.available_cells.contains(coord)) {
            lblMessage.setText("Celda ya llenada.");
            tf.setText("");
            return;
        }
        if (sudoku.is_valid_user_move(value, row, col)) {
            sudoku.rows.get(row).set(col, value);
            sudoku.columns.get(col).set(row, value);
            sudoku.available_cells.remove(coord);
            tf.setDisable(true);
            tf.setStyle("-fx-background-color: lightgreen; -fx-alignment: center;");
            lblMessage.setText("Movimiento válido.");
            if (sudoku.available_cells.isEmpty()) {
                lblMessage.setText("¡Ganaste!");
            }
        } else {
            tf.setText("");
            lblMessage.setText("Movimiento inválido.");
        }
    }

    @FXML private void onHelpClicked() {
        sudoku.help();
        renderBoard();
        lblMessage.setText("Ayuda aplicada.");
    }

    @FXML private void onSolveClicked() {
        sudoku.solve_board();
        renderBoard();
        lblMessage.setText("Sudoku resuelto.");
    }

    @FXML private void onNewClicked() {
        sudoku = new Sudoku(6, 2, 3, 15);
        renderBoard();
        lblMessage.setText("Nuevo juego iniciado.");
    }

    @FXML private void onExitClicked() {
        System.exit(0);
    }
}

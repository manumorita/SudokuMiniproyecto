package com.example.sudokuproyecto2.controller;

import com.example.sudokuproyecto2.model.ISudoku;
import com.example.sudokuproyecto2.model.ISudokuAdapter;
import com.example.sudokuproyecto2.model.Sudoku;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.control.TextField;

/**
 * Controller class for the Sudoku game interface.
 * Handles user interactions and updates the board accordingly.
 */
public class SudokuController {

    @FXML private GridPane sudokuGrid;
    @FXML private Button btnHelp;
    @FXML private Button btnSolve;
    @FXML private Button btnVerify;
    @FXML private Label lblMessage;

    private final TextField[][] cells = new TextField[6][6];
    private Integer[][] userValues = new Integer[6][6]; // Stores user-entered values
    private Sudoku sudoku;
    private ISudoku helper;
    private int helpUses = 0;

    /**
     * Initializes the controller after FXML loading.
     * Sets up the board, helper, fonts, and button event handlers.
     */
    @FXML
    public void initialize() {
        Font.loadFont(getClass().getResourceAsStream("/com/example/sudokuproyecto2/fonts/BubblegumSans-Regular.ttf"), 16);

        sudoku = new Sudoku(6, 2, 3, 15);
        helper = new ISudokuAdapter(sudoku); // Initialize helper adapter
        renderBoard();

        btnHelp.setOnMouseClicked(event -> onHelpClicked());
        btnSolve.setOnMouseClicked(event -> onSolveClicked());
        btnVerify.setOnMouseClicked(event -> onVerifyClicked());
    }

    /**
     * Renders the Sudoku board with the current values.
     * Pre-filled cells are disabled, and user-entered values are preserved.
     */
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
                } else if (userValues[row][col] != null) {
                    tf.setText(String.valueOf(userValues[row][col]));
                    tf.setStyle("-fx-alignment: center; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
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

    /**
     * Handles the input entered by the user in a Sudoku cell.
     * Validates the input and updates the internal representation.
     *
     * @param tf       the TextField being modified
     * @param row      the row of the cell
     * @param col      the column of the cell
     * @param newValue the new value entered
     */
    private void handleUserInput(TextField tf, int row, int col, String newValue) {
        tf.textProperty().removeListener((observable, oldValue, newValue1) -> handleUserInput(tf, row, col, newValue1));

        try {
            if (!newValue.isEmpty() && !newValue.matches("[1-6]")) {
                tf.setText("");
                tf.setStyle("-fx-border-color: #FF6F61; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
                lblMessage.setText("Solo se permiten números del 1 al 6.");
            }

            if (newValue.isEmpty()) {
                tf.setStyle("-fx-border-color: none; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
                lblMessage.setText("");
            } else {
                int value = Integer.parseInt(newValue);
                Sudoku.Coordinate coord = new Sudoku.Coordinate(row, col);

                if (!sudoku.available_cells.contains(coord)) {
                    tf.setStyle("-fx-border-color: #FF6F61; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
                    lblMessage.setText("Celda ya llenada.");
                    tf.setText("");
                } else if (!sudoku.is_valid_user_move(value, row, col)) {
                    tf.setStyle("-fx-border-color: #FF6F61; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
                    lblMessage.setText("Movimiento inválido: número repetido en fila, columna o bloque.");
                } else {
                    tf.setStyle("-fx-border-color: #F09696; -fx-background-color: #FFD8D9; -fx-text-fill: #F09696;");
                    lblMessage.setText("");
                    userValues[row][col] = value;
                }
            }
        } finally {
            tf.textProperty().addListener((observable, oldValue, newValue1) -> handleUserInput(tf, row, col, newValue1));
        }
    }

    /**
     * Triggered when the help button is clicked.
     * Applies a helper suggestion if available and updates the board.
     * Limits help to 5 uses.
     */
    @FXML
    private void onHelpClicked() {
        if (helpUses >= 5) {
            lblMessage.setText("Límite de ayudas alcanzado.");
            btnHelp.setDisable(true);
            return;
        }
        if (helper.provideHelp()) {
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

    /**
     * Triggered when the solve button is clicked.
     * Solves the Sudoku puzzle and disables user input.
     */
    @FXML
    private void onSolveClicked() {
        sudoku.solve_board();
        renderBoard();
        lblMessage.setText("Sudoku resuelto.");
        btnHelp.setDisable(true);
        btnVerify.setDisable(true);
    }

    /**
     * Triggered when the verify button is clicked.
     * Checks whether the current user solution is valid.
     */
    @FXML
    private void onVerifyClicked() {
        boolean validSolution = true;

        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                String cellText = cells[row][col].getText();

                if (cellText.isEmpty()) {
                    validSolution = false;
                    break;
                }

                int value = Integer.parseInt(cellText);
                if (value < 1 || value > 6 || !sudoku.is_valid_user_move(value, row, col)) {
                    validSolution = false;
                    break;
                }
            }
            if (!validSolution) break;
        }

        if (validSolution) {
            lblMessage.setText("¡Ganaste!");
            disableAllCells();
            btnHelp.setDisable(true);
            btnSolve.setDisable(true);
        } else {
            lblMessage.setText("La solución no es correcta.");
        }
    }

    /**
     * Disables all input fields on the Sudoku board.
     * Used when the puzzle is completed or solved.
     */
    private void disableAllCells() {
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 6; col++) {
                cells[row][col].setDisable(true);
            }
        }
    }
}

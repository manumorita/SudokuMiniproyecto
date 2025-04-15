package com.example.sudokuproyecto2.model;

/**
 * Adapter class that implements the {@link ISudoku} interface using a {@link Sudoku} instance.
 * <p>
 * This adapter allows external components (such as controllers) to interact with the {@link Sudoku}
 * model in a simplified and decoupled manner, specifically to request help functionality.
 */
public class ISudokuAdapter implements ISudoku {
    private final Sudoku sudoku;

    /**
     * Constructs a new {@code ISudokuAdapter} for the given {@link Sudoku} instance.
     *
     * @param sudoku the Sudoku game instance to be adapted
     */
    public ISudokuAdapter(Sudoku sudoku) {
        this.sudoku = sudoku;
    }

    /**
     * Provides a hint to the player by identifying and applying a correct move.
     * <p>
     * Internally calls {@code help()} and, if successful, applies the suggested move
     * via {@code applyHelp()}.
     *
     * @return {@code true} if a valid hint was found and applied; {@code false} otherwise
     */
    @Override
    public boolean provideHelp() {
        boolean helpResult = sudoku.help();
        if (helpResult) {
            sudoku.applyHelp();
        }
        return helpResult;
    }
}
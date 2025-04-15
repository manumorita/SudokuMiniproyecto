package com.example.sudokuproyecto2.model;

/**
 * Interface representing a simplified Sudoku helper service.
 * Implementations of this interface should provide assistance to the player,
 * typically by suggesting or applying a correct move.
 */
public interface ISudoku {

    /**
     * Attempts to provide a valid hint or move to the user.
     *
     * @return true if a valid help was provided and applied; false otherwise
     */
    boolean provideHelp();
}

package com.example.sudokuproyecto2.model;

import java.util.*;

/**
 * Represents a Sudoku board and provides methods to initialize, solve,
 * validate, and manipulate the game state.
 */
public class Sudoku {

    /**
     * Represents a coordinate (row, column) on the board.
     */
    public static class Coordinate {
        public final Integer row;
        public final Integer column;

        /**
         * Constructs a coordinate with the given row and column.
         *
         * @param row    the row index
         * @param column the column index
         */
        public Coordinate(Integer row, Integer column) {
            this.row = row;
            this.column = column;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            Coordinate that = (Coordinate) obj;
            return Objects.equals(row, that.row) && Objects.equals(column, that.column);
        }

        @Override
        public int hashCode() {
            return Objects.hash(row, column);
        }
    }

    public int size, block_height, block_width;
    public ArrayList<ArrayList<Integer>> rows, columns;
    public ArrayList<Coordinate> available_cells;

    private ArrayList<ArrayList<Integer>> temp_rows, temp_columns;

    public Coordinate lastHelpCoordinate = null;
    public int lastHelpValue = 0;

    /**
     * Constructs a Sudoku board with given parameters.
     *
     * @param size             board size (e.g., 6 for a 6x6 board)
     * @param block_height     height of each block
     * @param block_width      width of each block
     * @param random_elements  number of cells to prefill randomly
     */
    public Sudoku(int size, int block_height, int block_width, int random_elements) {
        this.size = size;
        this.block_height = block_height;
        this.block_width = block_width;
        inicialization();
        fill_randomly(random_elements);
    }

    /**
     * Initializes the data structures for the board.
     */
    private void inicialization() {
        rows = new ArrayList<>(size);
        columns = new ArrayList<>(size);
        temp_rows = new ArrayList<>(size);
        temp_columns = new ArrayList<>(size);
        available_cells = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            rows.add(new ArrayList<>(Collections.nCopies(size, 0)));
            columns.add(new ArrayList<>(Collections.nCopies(size, 0)));
        }
    }

    /**
     * Validates whether a move is legal in the current temporary board.
     */
    private boolean is_valid_move(int element, int row_position, int column_position) {
        if (temp_rows.get(row_position).contains(element)) return false;
        if (temp_columns.get(column_position).contains(element)) return false;

        int block_start_row = row_position / block_height * block_height;
        int block_start_column = column_position / block_width * block_width;

        for (int i = block_start_row; i < block_start_row + block_height; i++) {
            for (int j = block_start_column; j < block_start_column + block_width; j++) {
                if (temp_rows.get(i).get(j) == element) return false;
            }
        }
        return true;
    }

    /**
     * Attempts to solve the temporary board using backtracking.
     *
     * @return true if a solution is found
     */
    private boolean solve() {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                if (temp_rows.get(i).get(j) == 0) {
                    for (int k = 1; k <= size; k++) {
                        if (is_valid_move(k, i, j)) {
                            temp_rows.get(i).set(j, k);
                            temp_columns.get(j).set(i, k);
                            if (solve()) return true;
                            temp_rows.get(i).set(j, 0);
                            temp_columns.get(j).set(i, 0);
                        }
                    }
                    return false;
                }
            }
        return true;
    }

    /**
     * Copies the current board into temporary structures for solving.
     */
    private void copy_original_board_into_temp() {
        temp_rows = new ArrayList<>();
        temp_columns = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            temp_rows.add(new ArrayList<>(rows.get(i)));
            temp_columns.add(new ArrayList<>(columns.get(i)));
        }
    }

    /**
     * Randomly fills a predefined number of cells with valid values.
     *
     * @param n number of elements to prefill
     */
    private void fill_randomly(int n) {
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++)
                available_cells.add(new Coordinate(i, j));

        Random random = new Random();

        for (int i = 0; i < 6; i++) {
            for (int j = 0; j < 2; j++) {
                int initial_row = i / 2 * 2;
                int initial_column = i % 2 * 3;
                Coordinate coordinate;
                int row, column, value;

                do {
                    row = random.nextInt(2) + initial_row;
                    column = random.nextInt(3) + initial_column;
                    coordinate = new Coordinate(row, column);
                } while (!available_cells.contains(coordinate));

                available_cells.remove(coordinate);

                do {
                    copy_original_board_into_temp();
                    value = random.nextInt(size) + 1;
                    temp_rows.get(row).set(column, value);
                    temp_columns.get(column).set(row, value);
                } while (!solve());

                rows.get(row).set(column, value);
                columns.get(column).set(row, value);
            }
        }
    }

    /**
     * Validates a user's move by checking if the board remains solvable.
     *
     * @param value the number entered by the user
     * @param row   the row position
     * @param col   the column position
     * @return true if the move is valid
     */
    public boolean is_valid_user_move(int value, int row, int col) {
        copy_original_board_into_temp();
        temp_rows.get(row).set(col, value);
        temp_columns.get(col).set(row, value);
        return solve();
    }

    /**
     * Finds a valid move to suggest to the user.
     *
     * @return true if a hint was found and stored
     */
    public boolean help() {
        copy_original_board_into_temp();
        Random random = new Random();

        if (!available_cells.isEmpty()) {
            Coordinate coordinate = available_cells.get(random.nextInt(available_cells.size()));
            int row = coordinate.row;
            int col = coordinate.column;

            if (solve()) {
                int value = temp_rows.get(row).get(col);
                lastHelpCoordinate = coordinate;
                lastHelpValue = value;
                return true;
            }
        }
        return false;
    }

    /**
     * Applies the last generated help to the board.
     */
    public void applyHelp() {
        if (lastHelpCoordinate != null && lastHelpValue != 0) {
            int row = lastHelpCoordinate.row;
            int col = lastHelpCoordinate.column;
            int value = lastHelpValue;

            rows.get(row).set(col, value);
            columns.get(col).set(row, value);
            available_cells.remove(lastHelpCoordinate);

            lastHelpCoordinate = null;
            lastHelpValue = 0;
        }
    }

    /**
     * Solves the board completely and updates the official board with the solution.
     */
    public void solve_board() {
        copy_original_board_into_temp();
        solve();
        for (int i = 0; i < size; i++)
            for (int j = 0; j < size; j++) {
                rows.get(i).set(j, temp_rows.get(i).get(j));
                columns.get(j).set(i, temp_columns.get(j).get(i));
            }
        available_cells.clear();
    }
}

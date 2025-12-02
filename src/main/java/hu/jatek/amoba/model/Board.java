package hu.jatek.amoba.model;

import java.util.Arrays;

public class Board {

    private final int rows;
    private final int cols;
    private final char[][] cells;

    public Board(int rows, int cols) {
        if (rows < 5 || cols < 5 || cols > rows || rows > 25) {
            throw new IllegalArgumentException("Érvénytelen tábla méret: rows=" + rows + " cols=" + cols);
        }
        this.rows = rows;
        this.cols = cols;
        this.cells = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            Arrays.fill(cells[r], ' ');
        }
    }

    public int getRows() {
        return rows;
    }

    public int getCols() {
        return cols;
    }

    public boolean isInside(int r, int c) {
        return r >= 0 && r < rows && c >= 0 && c < cols;
    }

    public char get(int r, int c) {
        if (!isInside(r, c)) {
            throw new IndexOutOfBoundsException("r=" + r + " c=" + c);
        }
        return cells[r][c];
    }

    public void set(int r, int c, char symbol) {
        if (!isInside(r, c)) {
            throw new IndexOutOfBoundsException("r=" + r + " c=" + c);
        }
        cells[r][c] = symbol;
    }

    public boolean isEmpty(int r, int c) {
        return isInside(r, c) && cells[r][c] == ' ';
    }

    public boolean isFull() {
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (cells[r][c] == ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    public void print() {
        System.out.println();
        System.out.print("   ");
        for (int c = 0; c < cols; c++) {
            char colLabel = (char) ('a' + c);
            System.out.print(colLabel + " ");
        }
        System.out.println();
        for (int r = 0; r < rows; r++) {
            System.out.printf("%2d ", r + 1);
            for (int c = 0; c < cols; c++) {
                char cell = cells[r][c];
                if (cell == ' ') {
                    cell = '.';
                }
                System.out.print(cell + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public char[][] getCopyOfCells() {
        char[][] copy = new char[rows][cols];
        for (int r = 0; r < rows; r++) {
            System.arraycopy(cells[r], 0, copy[r], 0, cols);
        }
        return copy;
    }
}

package hu.jatek.amoba.logic;

import hu.jatek.amoba.model.Board;

public class WinChecker {

    private static final int WIN_LENGTH = 4;

    public boolean hasWon(Board board, char symbol) {
        int rows = board.getRows();
        int cols = board.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board.get(r, c) == symbol) {
                    if (checkDirection(board, symbol, r, c, 1, 0)) return true;  // vízszintes
                    if (checkDirection(board, symbol, r, c, 0, 1)) return true;  // függőleges
                    if (checkDirection(board, symbol, r, c, 1, 1)) return true;  // főátló
                    if (checkDirection(board, symbol, r, c, 1, -1)) return true; // mellékátló
                }
            }
        }
        return false;
    }

    private boolean checkDirection(Board board, char symbol, int r, int c, int dr, int dc) {
        int count = 0;
        for (int k = 0; k < WIN_LENGTH; k++) {
            int nr = r + dr * k;
            int nc = c + dc * k;
            if (!board.isInside(nr, nc)) {
                return false;
            }
            if (board.get(nr, nc) != symbol) {
                return false;
            }
            count++;
        }
        return count == WIN_LENGTH;
    }
}

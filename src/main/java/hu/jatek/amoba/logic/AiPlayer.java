package hu.jatek.amoba.logic;

import hu.jatek.amoba.model.Board;
import hu.jatek.amoba.model.Move;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class AiPlayer {

    private final Random random = new Random();

    public Move chooseMove(Board board, char symbol) {
        List<Move> validMoves = new ArrayList<>();
        int rows = board.getRows();
        int cols = board.getCols();

        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (board.isEmpty(r, c) && isAdjacentToAnyOccupied(board, r, c)) {
                    validMoves.add(new Move(r, c, symbol));
                }
            }
        }

        // ha nem talál elérhető helyet, akkor fallback: bármelyik üres (ritka)
        if (validMoves.isEmpty()) {
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    if (board.isEmpty(r, c)) {
                        validMoves.add(new Move(r, c, symbol));
                    }
                }
            }
        }

        if (validMoves.isEmpty()) {
            return null;
        }

        return validMoves.get(random.nextInt(validMoves.size()));
    }

    private boolean isAdjacentToAnyOccupied(Board board, int r, int c) {
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) {
                    continue;
                }
                int nr = r + dr;
                int nc = c + dc;
                if (board.isInside(nr, nc) && board.get(nr, nc) != ' ') {
                    return true;
                }
            }
        }
        return false;
    }
}

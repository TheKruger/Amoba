package hu.jatek.amoba.model;

import java.util.Objects;

public final class Move {

    private final int row;
    private final int col;
    private final char playerSymbol;

    public Move(int row, int col, char playerSymbol) {
        this.row = row;
        this.col = col;
        this.playerSymbol = playerSymbol;
    }

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }

    public char getPlayerSymbol() {
        return playerSymbol;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Move move)) return false;
        return row == move.row && col == move.col && playerSymbol == move.playerSymbol;
    }

    @Override
    public int hashCode() {
        return Objects.hash(row, col, playerSymbol);
    }

    @Override
    public String toString() {
        return "Move{" +
                "row=" + row +
                ", col=" + col +
                ", playerSymbol=" + playerSymbol +
                '}';
    }
}

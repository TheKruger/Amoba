package hu.jatek.amoba;

public class Board {
    private final char[][] board = new char[3][3];

    public Board() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                board[i][j] = ' ';
            }
        }
    }

    public boolean place(int r, int c, char player) {
        if (r < 0 || r > 2 || c < 0 || c > 2) return false;
        if (board[r][c] != ' ') return false;
        board[r][c] = player;
        return true;
    }

    public boolean checkWin(char p) {
        // Rows
        for (int i = 0; i < 3; i++) {
            if (board[i][0] == p && board[i][1] == p && board[i][2] == p) return true;
        }

        // Columns
        for (int j = 0; j < 3; j++) {
            if (board[0][j] == p && board[1][j] == p && board[2][j] == p) return true;
        }

        // Diagonals
        if (board[0][0] == p && board[1][1] == p && board[2][2] == p) return true;
        if (board[0][2] == p && board[1][1] == p && board[2][0] == p) return true;

        return false;
    }

    public boolean isFull() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (board[i][j] == ' ') return false;
            }
        }
        return true;
    }

    public void print() {
        System.out.println();
        for (int i = 0; i < 3; i++) {
            System.out.println(" " + board[i][0] + " | " + board[i][1] + " | " + board[i][2]);
            if (i < 2) System.out.println("-----------");
        }
        System.out.println();
    }
}
package hu.jatek.amoba.io;

import hu.jatek.amoba.model.Board;

import java.io.*;
import java.util.Optional;

public class BoardFileIO {

    public static class LoadedBoard {
        public final String playerName;
        public final Board board;

        public LoadedBoard(String playerName, Board board) {
            this.playerName = playerName;
            this.board = board;
        }
    }

    public static Optional<LoadedBoard> loadBoardFromFile(String path) throws IOException {
        File f = new File(path);
        if (!f.exists()) {
            return Optional.empty();
        }

        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String firstLine = br.readLine();
            if (firstLine == null) return Optional.empty();

            // formátum: playerName rows cols
            String[] header = firstLine.trim().split("\\s+");
            if (header.length != 3) return Optional.empty();

            String playerName = header[0];
            int rows = Integer.parseInt(header[1]);
            int cols = Integer.parseInt(header[2]);

            Board board = new Board(rows, cols);

            for (int r = 0; r < rows; r++) {
                String line = br.readLine();
                if (line == null) break;

                for (int c = 0; c < Math.min(cols, line.length()); c++) {
                    char ch = line.charAt(c);
                    char val = switch (ch) {
                        case 'x', 'X' -> 'x';
                        case 'o', 'O' -> 'o';
                        default -> ' ';
                    };
                    board.set(r, c, val);
                }
            }

            return Optional.of(new LoadedBoard(playerName, board));
        }
    }

    public static void saveBoardToFile(Board board, String playerName, String path) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {

            // Az első sorban: játékosnév + méretek
            bw.write(playerName + " " + board.getRows() + " " + board.getCols());
            bw.newLine();

            for (int r = 0; r < board.getRows(); r++) {
                StringBuilder sb = new StringBuilder();
                for (int c = 0; c < board.getCols(); c++) {
                    char ch = board.get(r, c);
                    sb.append(ch == ' ' ? '.' : ch);
                }
                bw.write(sb.toString());
                bw.newLine();
            }
        }
    }

    public static void deleteBoardFile(String path) {
        File f = new File(path);
        if (f.exists()) {
            f.delete();
        }
    }
}

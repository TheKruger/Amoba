package hu.jatek.amoba;

import hu.jatek.amoba.db.HighScoreRepository;
import hu.jatek.amoba.logic.Game;
import hu.jatek.amoba.model.Board;
import hu.jatek.amoba.model.Player;
import hu.jatek.amoba.io.BoardFileIO;
import hu.jatek.amoba.io.BoardFileIO.LoadedBoard;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class App {

    private static final Logger log = LoggerFactory.getLogger(App.class);

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("=== Amőba (Gomoku) NxM ===");
        System.out.print("Add meg a játékos neved: ");
        String playerName = scanner.nextLine().trim();
        if (playerName.isEmpty()) {
            playerName = "Játékos";
        }

        int n;
        int m;
        while (true) {
            System.out.print("Add meg a tábla méretét (N M, 5 <= M <= N <= 25), pl. 10 10: ");
            String line = scanner.nextLine();
            String[] parts = line.trim().split("\\s+");
            if (parts.length == 2) {
                try {
                    n = Integer.parseInt(parts[0]);
                    m = Integer.parseInt(parts[1]);
                    if (m >= 5 && m <= n && n <= 25) {
                        break;
                    } else {
                        System.out.println("Érvénytelen méret! 5 <= M <= N <= 25.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Nem számokat adtál meg.");
                }
            } else {
                System.out.println("Két szám kell, szóközzel elválasztva.");
            }
        }

        Board board;
        try {
            Optional<LoadedBoard> loaded = BoardFileIO.loadBoardFromFile("board.txt");
            if (loaded.isPresent() && loaded.get().board.getRows() == n && loaded.get().board.getCols() == m) {
                log.info("Létező játékállás betöltve (board.txt).");
                board = loaded.get().board;
            } else {
                board = new Board(n, m);
            }
        } catch (IOException e) {
            log.warn("Nem sikerült a board.txt betöltése, üres tábla indul.", e);
            board = new Board(n, m);
        }

        Player human = new Player(playerName, 'x', true);
        Player ai = new Player("Gép", 'o', false);
        HighScoreRepository highScoreRepository = new HighScoreRepository();

        Game game = new Game(board, human, ai, highScoreRepository, scanner);
        log.info("Játék kezdődik.");
        game.start();

        System.out.println("\nHigh score tábla:");
        highScoreRepository.printHighScores();
    }
}

package hu.jatek.amoba.logic;

import hu.jatek.amoba.db.HighScoreRepository;
import hu.jatek.amoba.io.BoardFileIO;
import hu.jatek.amoba.model.Board;
import hu.jatek.amoba.model.Move;
import hu.jatek.amoba.model.Player;
import hu.jatek.amoba.util.ConsoleUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Optional;
import java.util.Scanner;

public class Game {

    private static final Logger log = LoggerFactory.getLogger(Game.class);

    private final Board board;
    private final Player player;
    private final Player ai;
    private final HighScoreRepository highScoreRepository;
    private final Scanner scanner;
    private final WinChecker winChecker = new WinChecker();
    private final AiPlayer aiPlayer = new AiPlayer();
    private boolean firstMoveDone = false;

    public Game(Board board,
                Player player,
                Player ai,
                HighScoreRepository highScoreRepository,
                Scanner scanner) {
        this.board = board;
        this.player = player;
        this.ai = ai;
        this.highScoreRepository = highScoreRepository;
        this.scanner = scanner;
    }

    public void start() {
        // Ha betöltött állapot van, de nem a mostani játékos nevét tartalmazza → töröljük
        try {
            Optional<BoardFileIO.LoadedBoard> loaded = BoardFileIO.loadBoardFromFile("board.txt");

            if (loaded.isPresent()) {
                BoardFileIO.LoadedBoard lb = loaded.get();

                if (!lb.playerName.equals(player.getName()) ||
                    lb.board.getRows() != board.getRows() ||
                    lb.board.getCols() != board.getCols()) {

                    BoardFileIO.deleteBoardFile("board.txt");
                }
            }
        } catch (IOException e) {
            // ha baj van, töröljük
            BoardFileIO.deleteBoardFile("board.txt");
        }

        ConsoleUtils.clearScreen();
        System.out.println("Játék kezdődik. A te jeled: '" + player.getSymbol() + "'. Gépi ellenfél: '" + ai.getSymbol() + "'.");
        System.out.println("Győzelemhez 4 egymás melletti jel kell (vízszintes, függőleges, átlós).");
        System.out.println("Lépés formátuma: pl. a 1  (oszlop betű, sor szám).");
        System.out.println("A jelnek legalább átlósan érintkeznie kell egy már fenn lévő jellel (kivéve az első lépés).");
        System.out.println();

        char currentSymbol = player.getSymbol();
        Player currentPlayer = player;

        // ha a tábla teljesen üres, az első lépés mehet középre:
        if (isBoardEmpty()) {
            doFirstMoveToCenter();
        }

        while (true) {
            ConsoleUtils.clearScreen();
            board.print();

            if (board.isFull()) {
                ConsoleUtils.clearScreen();
                board.print();
                log.info("Döntetlen!");

                BoardFileIO.deleteBoardFile("board.txt");
                break;
            }

            if (currentPlayer.isHuman()) {
                System.out.println("Te jössz, " + player.getName() + ".");
                Move move = askPlayerMove(currentSymbol);
                board.set(move.getRow(), move.getCol(), currentSymbol);
                saveBoardSafely();
            } else {
                System.out.println("Gép lép (" + ai.getSymbol() + ")...");
                Move move = aiPlayer.chooseMove(board, currentSymbol);
                if (move == null) {
                    log.info("A gép nem tud lépni, döntetlen.");
                    BoardFileIO.deleteBoardFile("board.txt");
                    break;
                }
                board.set(move.getRow(), move.getCol(), currentSymbol);
                saveBoardSafely();
            }

            if (winChecker.hasWon(board, currentSymbol)) {
                ConsoleUtils.clearScreen();
                board.print();
                log.info("A győztes: " + currentPlayer.getName() + " ('" + currentSymbol + "')!");

                // HIGH SCORE
                if (currentPlayer.isHuman()) {
                    highScoreRepository.addWin(player.getName());
                }

                // játék vége → ne mentsünk, hanem töröljünk
                BoardFileIO.deleteBoardFile("board.txt");
                break;
            }

            // játékosváltás
            if (currentPlayer == player) {
                currentPlayer = ai;
                currentSymbol = ai.getSymbol();
            } else {
                currentPlayer = player;
                currentSymbol = player.getSymbol();
            }
        }
    }

    private Move askPlayerMove(char symbol) {
        while (true) {
            System.out.print("Add meg a lépésed (pl. a 1 vagy :save): ");
            String line = scanner.nextLine().trim();
            if (line.equalsIgnoreCase(":save")) {
                saveBoardSafely();
                System.out.println("Játékállás elmentve (board.txt).");
                continue;
            }
            String[] parts = line.split("\\s+");
            if (parts.length != 2) {
                log.warn("Hibás formátum. Pl.: a 1");
                continue;
            }
            String colPart = parts[0];
            String rowPart = parts[1];

            int col = parseColumn(colPart);
            int row = parseRow(rowPart);

            if (col == -1 || row == -1) {
                log.error("Érvénytelen oszlop vagy sor.");
                continue;
            }

            int r = row;
            int c = col;

            if (!board.isInside(r, c)) {
                log.error("A mező a táblán kívül van.");
                continue;
            }
            if (!board.isEmpty(r, c)) {
                log.warn("A mező már foglalt.");
                continue;
            }
            if (!isValidPlacement(r, c)) {
                log.error("A jelnek érintkeznie kell legalább átlósan egy meglévő jellel.");
                continue;
            }
            return new Move(r, c, symbol);
        }
    }

    private int parseColumn(String colPart) {
        colPart = colPart.toLowerCase();
        if (colPart.length() != 1) {
            return -1;
        }
        char ch = colPart.charAt(0);
        int c = ch - 'a';
        if (c < 0 || c >= board.getCols()) {
            return -1;
        }
        return c;
    }

    private int parseRow(String rowPart) {
        try {
            int rowIndex = Integer.parseInt(rowPart) - 1;
            if (rowIndex < 0 || rowIndex >= board.getRows()) {
                return -1;
            }
            return rowIndex;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean isBoardEmpty() {
        for (int r = 0; r < board.getRows(); r++) {
            for (int c = 0; c < board.getCols(); c++) {
                if (board.get(r, c) != ' ') {
                    return false;
                }
            }
        }
        return true;
    }

    private void doFirstMoveToCenter() {
        int centerRow = board.getRows() / 2;
        int centerCol = board.getCols() / 2;
        board.set(centerRow, centerCol, player.getSymbol());
        firstMoveDone = true;
        log.info("Első lépés középre: ({}, {})", centerRow, centerCol);
    }

    private boolean isValidPlacement(int r, int c) {
        if (isBoardEmpty() && !firstMoveDone) {
            return true;
        }
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                if (dr == 0 && dc == 0) continue;
                int nr = r + dr;
                int nc = c + dc;
                if (board.isInside(nr, nc) && board.get(nr, nc) != ' ') {
                    return true;
                }
            }
        }
        return false;
    }

    private void saveBoardSafely() {
        try {
            BoardFileIO.saveBoardToFile(board, player.getName(), "./board.txt");
        } catch (IOException e) {
            log.error("Nem sikerült a tábla mentése board.txt-be.", e);
        }
    }
}

package hu.jatek.amoba;

import hu.jatek.amoba.io.BoardFileIO;
import hu.jatek.amoba.model.Board;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class GameTest {

    @Test
    void testBoardSaveAndLoad() throws Exception {
        Board b = new Board(6, 6);
        b.set(2, 2, 'x');
        b.set(3, 3, 'o');

        BoardFileIO.saveBoardToFile(b, "TestPlayer", "test_board.txt");

        Optional<BoardFileIO.LoadedBoard> loaded =
                BoardFileIO.loadBoardFromFile("test_board.txt");

        assertTrue(loaded.isPresent());
        assertEquals("TestPlayer", loaded.get().playerName);

        Board loadedBoard = loaded.get().board;
        assertEquals('x', loadedBoard.get(2, 2));
        assertEquals('o', loadedBoard.get(3, 3));

        new File("test_board.txt").delete();
    }

    @Test
    void dummyTest() {
        assertTrue(true);
    }
}

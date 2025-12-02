package hu.jatek.amoba;

import hu.jatek.amoba.logic.WinChecker;
import hu.jatek.amoba.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class WinCheckerTest {

    @Test
    void testHorizontalWin() {
        Board b = new Board(10, 10);
        WinChecker w = new WinChecker();
        b.set(0, 0, 'x');
        b.set(0, 1, 'x');
        b.set(0, 2, 'x');
        b.set(0, 3, 'x');
        assertTrue(w.hasWon(b, 'x'));
    }

    @Test
    void testVerticalWin() {
        Board b = new Board(10, 10);
        WinChecker w = new WinChecker();
        b.set(0, 5, 'o');
        b.set(1, 5, 'o');
        b.set(2, 5, 'o');
        b.set(3, 5, 'o');
        assertTrue(w.hasWon(b, 'o'));
    }

    @Test
    void testDiagonalWin1() {
        Board b = new Board(10, 10);
        WinChecker w = new WinChecker();
        b.set(1, 1, 'x');
        b.set(2, 2, 'x');
        b.set(3, 3, 'x');
        b.set(4, 4, 'x');
        assertTrue(w.hasWon(b, 'x'));
    }

    @Test
    void testDiagonalWin2() {
        Board b = new Board(10, 10);
        WinChecker w = new WinChecker();
        b.set(4, 1, 'x');
        b.set(3, 2, 'x');
        b.set(2, 3, 'x');
        b.set(1, 4, 'x');
        assertTrue(w.hasWon(b, 'x'));
    }

    @Test
    void testNoWin() {
        Board b = new Board(10, 10);
        WinChecker w = new WinChecker();
        assertFalse(w.hasWon(b, 'x'));
    }

    @Test
    void testPartialSeriesNotWin() {
        Board b = new Board(10, 10);
        WinChecker w = new WinChecker();

        b.set(0,0,'x');
        b.set(0,1,'x');
        b.set(0,2,'x'); // csak 3, kell 4
        assertFalse(w.hasWon(b, 'x'));
    }

    @Test
    void testOutOfBoundsDiagonalCheckDoesNotCrash() {
        Board b = new Board(5, 5);
        WinChecker w = new WinChecker();

        b.set(4, 4, 'x'); // szélen
        assertFalse(w.hasWon(b, 'x')); // ne dobjon kivételt
    }
}

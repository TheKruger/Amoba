package hu.jatek.amoba;

import hu.jatek.amoba.model.Board;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void testBoardInitialization() {
        Board b = new Board(10, 10);
        assertEquals(10, b.getRows());
        assertEquals(10, b.getCols());
        assertTrue(b.isEmpty(0, 0));
    }

    @Test
    void testSetAndGet() {
        Board b = new Board(10, 10);
        b.set(5, 5, 'x');
        assertEquals('x', b.get(5, 5));
    }

    @Test
    void testIsFull() {
        Board b = new Board(5, 5);
        for (int r = 0; r < b.getRows(); r++) {
            for (int c = 0; c < b.getCols(); c++) {
                b.set(r, c, 'x');
            }
        }
        assertTrue(b.isFull());
    }

    @Test
    void testIsInside() {
        Board b = new Board(10, 10);
        assertTrue(b.isInside(0, 0));
        assertFalse(b.isInside(-1, 0));
        assertFalse(b.isInside(10, 10));
    }

    @Test
    void testGetCopyOfCells() {
        Board b = new Board(6, 6);
        b.set(2, 2, 'x');
        char[][] copy = b.getCopyOfCells();

        assertEquals('x', copy[2][2]);

        // módosítás a copy-ban nem érintheti a boardot
        copy[2][2] = 'o';
        assertEquals('x', b.get(2, 2));
    }

    @Test
    void testSetOutsideBoardThrows() {
        Board b = new Board(5, 5);
        assertThrows(IndexOutOfBoundsException.class, () -> b.set(10, 10, 'x'));
    }

    @Test
    void testConstructorInvalidSizes() {
        assertThrows(IllegalArgumentException.class, () -> new Board(3, 3));
        assertThrows(IllegalArgumentException.class, () -> new Board(30, 30));
        assertThrows(IllegalArgumentException.class, () -> new Board(10, 3));
    }
}

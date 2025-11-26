package hu.jatek.amoba;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class BoardTest {

    @Test
    void testPlace() {
        Board b = new Board();
        assertTrue(b.place(0,0,'X'));
        assertFalse(b.place(0,0,'O'));
    }

    @Test
    void testWinRow() {
        Board b = new Board();
        b.place(0,0,'X');
        b.place(0,1,'X');
        b.place(0,2,'X');
        assertTrue(b.checkWin('X'));
    }

    @Test
    void testWinDiag() {
        Board b = new Board();
        b.place(0,0,'O');
        b.place(1,1,'O');
        b.place(2,2,'O');
        assertTrue(b.checkWin('O'));
    }

    @Test
    void testDrawDiag() {
        Board b = new Board();

        b.place(0,0, 'X');
        b.place(0,2, 'X');
        b.place(1,0, 'X');
        b.place(1,2, 'X');
        b.place(2,1, 'X');

        b.place(0,1, 'O');
        b.place(1,1, 'O');
        b.place(2,0, 'O');
        b.place(2,2, 'O');

        assertFalse(b.checkWin('X'));
        assertFalse(b.checkWin('O'));
    }
}
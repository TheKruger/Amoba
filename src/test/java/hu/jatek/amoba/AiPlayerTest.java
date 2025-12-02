package hu.jatek.amoba;

import hu.jatek.amoba.logic.AiPlayer;
import hu.jatek.amoba.model.Board;
import hu.jatek.amoba.model.Move;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class AiPlayerTest {

    @Test
    void testAiChooseMove() {
        Board b = new Board(10, 10);
        AiPlayer ai = new AiPlayer();

        // rakunk le 1 jelet, hogy az adjacency szabály működjön
        b.set(5, 5, 'x');

        Move move = ai.chooseMove(b, 'o');
        assertNotNull(move);

        // az AI nem léphet foglalt helyre
        assertTrue(b.isEmpty(move.getRow(), move.getCol()));
    }

    @Test
    void testAiFallbackWhenNoAdjacent() {
        Board b = new Board(5,5);
        AiPlayer ai = new AiPlayer();

        // üres tábla → fallback minden üres mezőre
        Move m = ai.chooseMove(b, 'o');
        assertNotNull(m);
    }

    @Test
    void testAiReturnsNullWhenBoardFull() {
        Board b = new Board(5,5);
        AiPlayer ai = new AiPlayer();

        // tele rakjuk
        for (int r=0;r<5;r++){
            for (int c=0;c<5;c++){
                b.set(r,c,'x');
            }
        }

        Move m = ai.chooseMove(b,'o');
        assertNull(m);
    }
}

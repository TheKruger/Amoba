package hu.jatek.amoba;

import hu.jatek.amoba.db.HighScoreRepository;
import hu.jatek.amoba.db.Database;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import static org.mockito.Mockito.*;

public class HighScoreRepositoryMockTest {

    @Test
    void testAddWinExistingPlayer() throws Exception {

        // mock objektumok
        Connection conn = mock(Connection.class);
        PreparedStatement selectStmt = mock(PreparedStatement.class);
        PreparedStatement updateStmt = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        // static Database.getConnection() mockolása
        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            // mock visszatérés
            mockedDb.when(Database::getConnection).thenReturn(conn);

            when(conn.prepareStatement("SELECT wins FROM scores WHERE name = ?"))
                    .thenReturn(selectStmt);
            when(selectStmt.executeQuery()).thenReturn(resultSet);

            // player already exists
            when(resultSet.next()).thenReturn(true);
            when(resultSet.getInt("wins")).thenReturn(3);

            // update statement
            when(conn.prepareStatement("UPDATE scores SET wins = ? WHERE name = ?"))
                    .thenReturn(updateStmt);

            HighScoreRepository repo = new HighScoreRepository();
            repo.addWin("Pisti");

            // verify calls
            verify(updateStmt, times(1)).executeUpdate();
            verify(conn, times(1)).commit();
        }
    }

    @Test
    void testAddWinNewPlayer() throws Exception {

        Connection conn = mock(Connection.class);
        PreparedStatement selectStmt = mock(PreparedStatement.class);
        PreparedStatement insertStmt = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        try (MockedStatic<Database> mockedDb = mockStatic(Database.class)) {

            mockedDb.when(Database::getConnection).thenReturn(conn);

            when(conn.prepareStatement("SELECT wins FROM scores WHERE name = ?"))
                    .thenReturn(selectStmt);
            when(selectStmt.executeQuery()).thenReturn(resultSet);

            // no result
            when(resultSet.next()).thenReturn(false);

            when(conn.prepareStatement("INSERT INTO scores(name, wins) VALUES(?, ?)"))
                    .thenReturn(insertStmt);

            HighScoreRepository repo = new HighScoreRepository();
            repo.addWin("TesztElek");

            verify(insertStmt, times(1)).executeUpdate();
            verify(conn, times(1)).commit();
        }
    }
}

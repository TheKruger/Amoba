package hu.jatek.amoba;

import hu.jatek.amoba.db.Database;
import hu.jatek.amoba.db.HighScoreRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

public class HighScoreRepositoryTest {

    @BeforeEach
    void resetDb() throws Exception {
        File f = new File("amoba.db");
        if (f.exists()) f.delete();

        try (Connection conn = Database.getConnection()) {
            conn.createStatement().executeUpdate(
                "CREATE TABLE IF NOT EXISTS scores (" +
                "name TEXT PRIMARY KEY," +
                "wins INTEGER NOT NULL)"
            );
        }
    }

    @Test
    void testAddAndReadScores() {
        HighScoreRepository repo = new HighScoreRepository();

        repo.addWin("Pisti");
        repo.addWin("Pisti");
        repo.addWin("Géza");

        var map = repo.getHighScores();

        assertEquals(2, map.get("Pisti"));
        assertEquals(1, map.get("Géza"));
    }
}

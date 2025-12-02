package hu.jatek.amoba.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {

    private static final Logger log = LoggerFactory.getLogger(Database.class);
    private static final String URL = "jdbc:sqlite:amoba.db";

    static {
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement()) {
            String sql = """
                    CREATE TABLE IF NOT EXISTS scores (
                        name TEXT PRIMARY KEY,
                        wins INTEGER NOT NULL
                    )
                    """;
            stmt.execute(sql);
        } catch (SQLException e) {
            log.error("Nem sikerült létrehozni a scores táblát.", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}

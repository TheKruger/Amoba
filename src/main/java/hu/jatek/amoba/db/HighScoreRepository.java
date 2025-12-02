package hu.jatek.amoba.db;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class HighScoreRepository {

    private static final Logger log = LoggerFactory.getLogger(HighScoreRepository.class);

    public void addWin(String playerName) {
        try (Connection conn = Database.getConnection()) {
            conn.setAutoCommit(false);

            int currentWins = 0;
            try (PreparedStatement ps = conn.prepareStatement(
                    "SELECT wins FROM scores WHERE name = ?")) {
                ps.setString(1, playerName);
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        currentWins = rs.getInt("wins");
                    }
                }
            }

            if (currentWins == 0) {
                try (PreparedStatement ps = conn.prepareStatement(
                        "INSERT INTO scores(name, wins) VALUES(?, ?)")) {
                    ps.setString(1, playerName);
                    ps.setInt(2, 1);
                    ps.executeUpdate();
                }
            } else {
                try (PreparedStatement ps = conn.prepareStatement(
                        "UPDATE scores SET wins = ? WHERE name = ?")) {
                    ps.setInt(1, currentWins + 1);
                    ps.setString(2, playerName);
                    ps.executeUpdate();
                }
            }

            conn.commit();
        } catch (SQLException e) {
            log.error("Nem sikerült a győzelem mentése a DB-be.", e);
        }
    }

    public Map<String, Integer> getHighScores() {
        Map<String, Integer> result = new LinkedHashMap<>();
        try (Connection conn = Database.getConnection();
             PreparedStatement ps = conn.prepareStatement(
                     "SELECT name, wins FROM scores ORDER BY wins DESC, name ASC");
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                result.put(rs.getString("name"), rs.getInt("wins"));
            }

        } catch (SQLException e) {
            log.error("Nem sikerült a high score lekérdezése.", e);
        }
        return result;
    }

    public void printHighScores() {
        Map<String, Integer> scores = getHighScores();
        if (scores.isEmpty()) {
            System.out.println("Még nincs elmentett győzelem.");
            return;
        }
        System.out.println("Név           Győzelmek");
        System.out.println("------------------------");
        scores.forEach((name, wins) -> {
            System.out.printf("%-12s %d%n", name, wins);
        });
    }
}

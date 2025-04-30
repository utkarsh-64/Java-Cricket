package src;

import java.sql.*;

public class DatabaseManager {
    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/db_java";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "2407";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);
    }

    public static void createPlayer(String name) throws DatabaseException {
        try (Connection conn = getConnection()) {
            String sql = "INSERT INTO players (name) VALUES (?) ON DUPLICATE KEY UPDATE name=name";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Database error: " + e.getMessage());
        }
    }

    public static void updatePlayerStats(String name, boolean won) throws DatabaseException {
        String column = won ? "wins" : "losses";
        String sql = "UPDATE players SET " + column + " = " + column + " + 1 WHERE name = ?";

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new DatabaseException("Update failed: " + e.getMessage());
        }
    }

    public static void deletePlayer(String name) throws DatabaseException, PlayerNotFoundException {
        try (Connection conn = getConnection()) {
            if (!playerExists(name)) {
                throw new PlayerNotFoundException("Player not found!");
            }

            String sql = "DELETE FROM players WHERE name = ?";
            try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setString(1, name);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            throw new DatabaseException("Delete failed: " + e.getMessage());
        }
    }

    public static String getPlayerStats(String name) throws DatabaseException, PlayerNotFoundException {
        String sql = "SELECT * FROM players WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return String.format("Name: %s | Wins: %d | Losses: %d",
                        rs.getString("name"), rs.getInt("wins"), rs.getInt("losses"));
            } else {
                throw new PlayerNotFoundException("Player not found!");
            }
        } catch (SQLException e) {
            throw new DatabaseException("Fetch failed: " + e.getMessage());
        }
    }

    public static boolean playerExists(String name) throws SQLException {
        String sql = "SELECT 1 FROM players WHERE name = ?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            return stmt.executeQuery().next();
        }
    }
}

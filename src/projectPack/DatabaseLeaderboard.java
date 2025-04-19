package projectPack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseLeaderboard {

    public static void uploadResults(String name, int iqScore, String timeTaken) {
        String sql = "INSERT INTO Leaderboard (Name, IQ_Score, TimeTaken) VALUES (?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, name);
            stmt.setInt(2, iqScore);
            stmt.setString(3, timeTaken);

            int rowsInserted = stmt.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("Leaderboard entry added successfully.");
            } else {
                System.out.println("Failed to add leaderboard entry.");
            }

        } catch (SQLException e) {
            System.err.println("Error uploading to leaderboard: " + e.getMessage());
        }
    }
    public static LeaderboardEntry getUserLeaderboardEntry(String username) {
        String sql = "SELECT Id, Name, IQ_Score, TimeTaken, " +
                "ROW_NUMBER() OVER (ORDER BY IQ_Score ASC, TimeTaken ASC) AS RowNum " +
                "FROM Leaderboard WHERE Name = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    LeaderboardEntry entry = new LeaderboardEntry(
                            rs.getInt("Id"),
                            rs.getString("Name"),
                            rs.getInt("IQ_Score"),
                            rs.getString("TimeTaken")
                    );
                    entry.setRank(rs.getInt("RowNum")); // if you have a 'rank' field
                    return entry;
                }
            }

        } catch (SQLException e) {
            System.err.println("Error fetching user leaderboard entry: " + e.getMessage());
        }

        return null;
    }

    public static int getUserPosition(String username) {
        String sql = "SELECT ROW_NUMBER() OVER (ORDER BY IQ_Score DESC, TimeTaken ASC) AS RowNum " +
                "FROM Leaderboard WHERE Name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt("RowNum");
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user position for username " + username + ": " + e.getMessage());
            e.printStackTrace(); // Print stack trace for more context
        }

        return -1; // Return -1 if user is not found
    }
    public static List<LeaderboardEntry> getTopLeaderboard(int limit) {
        List<LeaderboardEntry> leaderboard = new ArrayList<>();
        String sql = "SELECT Id, Name, IQ_Score, TimeTaken, OptOut " +
                "FROM Leaderboard " +
                "ORDER BY IQ_Score DESC, TimeTaken ASC " +
                "OFFSET 0 ROWS FETCH NEXT ? ROWS ONLY";  // Azure SQL Server syntax

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, limit); // Set the limit dynamically

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    boolean optedOut = rs.getBoolean("OptOut");
                    String name = optedOut ? "Anonymous" : rs.getString("Name");

                    LeaderboardEntry entry = new LeaderboardEntry(
                            rs.getInt("Id"),
                            name,
                            rs.getInt("IQ_Score"),
                            rs.getString("TimeTaken")
                    );
                    leaderboard.add(entry);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching leaderboard: " + e.getMessage());
        }
        return leaderboard;
    }
}

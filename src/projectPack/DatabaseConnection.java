package projectPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Method to connect to the azure database where questions and leaderboard will be stored
 */
public class DatabaseConnection {


    public static Connection getConnection() throws SQLException {
        String connectionUrl = "jdbc:sqlserver://databasequestions.database.windows.net:1433;" +
                "database=QuestionsIQTest;" +
                "user=SHAdmin@databasequestions;" +
                "password=Pa$$w0rd;" + // Replace with your actual password
                "encrypt=true;" +
                "trustServerCertificate=false;" +
                "hostNameInCertificate=*.database.windows.net;" +
                "loginTimeout=30;";

        try {
            // Load SQL Server JDBC driver
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(connectionUrl);

        } catch (ClassNotFoundException e) {
            System.err.println("SQL Server JDBC driver not found.");
            throw new SQLException("SQL Server JDBC driver not found.", e);
        } catch (SQLException e) {
            System.err.println("Database connection failed: " + e.getMessage());
            throw e; // Rethrow the SQLException to propagate it
        }
    }

    public static List<Questions> getRandomQuestionsByDifficulty(String difficulty, int count) {
        List<Questions> questionList = new ArrayList<>();

        // Inject the count value directly into the SQL string
        String sql = String.format("""
        SELECT TOP %d id, question_text, option_a, option_b, option_c, option_d, 
                      correct_answer, difficulty, score, image_url
        FROM questions
        WHERE difficulty = ?
        ORDER BY NEWID();
    """, count);

        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, difficulty);

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Questions question = new Questions(
                            rs.getInt("id"),
                            rs.getString("question_text"),
                            rs.getString("option_a"),
                            rs.getString("option_b"),
                            rs.getString("option_c"),
                            rs.getString("option_d"),
                            rs.getString("correct_answer"),
                            rs.getString("difficulty"),
                            rs.getInt("score"),
                            rs.getString("image_url")
                    );
                    questionList.add(question);
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            e.printStackTrace();
        }

        return questionList;
    }
}

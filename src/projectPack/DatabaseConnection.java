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

    public static List<Questions> getRandomQuestionsByDifficulty() {
        List<Questions> questionList = new ArrayList<>();
        String[] difficulties = {"easy", "medium", "hard"};

        // Query to retrieve 3 random questions based on difficulty
        String sql = "SELECT TOP 3 question_text, option_a, option_b, option_c, option_d, correct_answer, difficulty, score, image_url " +
                "FROM questions WHERE UPPER(difficulty) = UPPER(?) ORDER BY NEWID();";

        // Establish database connection
        try (Connection conn = getConnection()) {

            for (String difficulty : difficulties) {
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, difficulty);  // Bind difficulty parameter
                    ResultSet rs = stmt.executeQuery();

                    int count = 0; // Counter for questions added
                    while (rs.next() && count < 3) {
                        // Create and add the question object
                        Questions question = new Questions(
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
                        count++;
                    }

                    if (count == 0) {
                        System.out.println("No questions found for difficulty: " + difficulty);
                    }
                } catch (SQLException e) {
                    System.out.println("Error executing query for difficulty " + difficulty + ": " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Check if any difficulty is missing
            for (String difficulty : difficulties) {
                long found = questionList.stream().filter(q -> q.getDifficulty().equalsIgnoreCase(difficulty)).count();
                if (found < 3) {
                    System.out.println("Warning: Less than 3 questions found for difficulty - " + difficulty);
                }
            }

            return questionList;
        } catch (SQLException e) {
            throw new RuntimeException("Database error: " + e.getMessage(), e);
        }
    }
}

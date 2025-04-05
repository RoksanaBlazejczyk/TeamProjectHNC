package projectPack;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

    // Method to get 1 random question for each difficulty level
    public static List<Questions> getRandomQuestionsByDifficulty() {
        List<Questions> questionList = new ArrayList<>();
        String[] difficulties = {"easy", "medium", "hard"};

        // Query to retrieve random questions based on difficulty
        String sql = "SELECT TOP 1 question_text, option_a, option_b, option_c, option_d, correct_answer, difficulty, score, image_url " +
                "FROM questions WHERE UPPER(difficulty) = UPPER(?) ORDER BY NEWID();";

        // Establish database connection
        try (Connection conn = getConnection()) {

            for (String difficulty : difficulties) {
                System.out.println("Executing SQL for difficulty: " + difficulty);  // Log the difficulty value
                try (PreparedStatement stmt = conn.prepareStatement(sql)) {
                    stmt.setString(1, difficulty);  // Binding the difficulty parameter
                    ResultSet rs = stmt.executeQuery();

                    if (rs.next()) {
                        // Log question found
                        System.out.println("Question found for difficulty " + difficulty);

                        // Add difficulty to the Questions object
                        Questions question = new Questions(
                                rs.getString("question_text"),
                                rs.getString("option_a"),
                                rs.getString("option_b"),
                                rs.getString("option_c"),
                                rs.getString("option_d"),
                                rs.getString("correct_answer"),
                                rs.getString("difficulty")  // Pass difficulty to constructor
                        );
                        questionList.add(question);
                    } else {
                        // Log no question found for difficulty
                        System.out.println("No question found for difficulty: " + difficulty);
                    }
                } catch (SQLException e) {
                    System.out.println("Error executing query: " + e.getMessage());
                    e.printStackTrace();
                }
            }

            // Check if the list contains all questions
            if (questionList.size() < difficulties.length) {
                System.out.println("Warning: Not enough questions found. Missing difficulty levels: ");
                if (questionList.stream().noneMatch(q -> q.getDifficulty().equals("easy"))) {
                    System.out.println("- easy");
                }
                if (questionList.stream().noneMatch(q -> q.getDifficulty().equals("medium"))) {
                    System.out.println("- medium");
                }
                if (questionList.stream().noneMatch(q -> q.getDifficulty().equals("hard"))) {
                    System.out.println("- hard");
                }
            }

            return questionList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }}

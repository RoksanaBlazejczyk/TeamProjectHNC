package projectPack;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    private static final String URL = "jdbc:mysql://localhost:3306/questions";
    private static final String USER = "root";
    private static final String PASSWORD = "";

    /**
     * Method to get two random questions with options and the correct answer from the database.
     * @param difficulty The difficulty level to fetch questions for (easy, medium, or hard)
     * @return A list containing two Questions objects
     */
    public static List<Questions> getRandomQuestionsWithOptions(String difficulty) {
        List<Questions> questionsList = new ArrayList<>();
        String query = "SELECT question_text, option_a, option_b, option_c, option_d, correct_answer FROM questions WHERE difficulty = ? ORDER BY RAND() LIMIT 2";

        try (Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
             PreparedStatement statement = connection.prepareStatement(query)) {

            // Set the difficulty parameter for the query
            statement.setString(1, difficulty);

            // Execute the query
            ResultSet resultSet = statement.executeQuery();

            // Retrieve the questions and their options
            while (resultSet.next()) {
                String questionText = resultSet.getString("question_text");
                String optionA = resultSet.getString("option_a");
                String optionB = resultSet.getString("option_b");
                String optionC = resultSet.getString("option_c");
                String optionD = resultSet.getString("option_d");
                String correctAnswer = resultSet.getString("correct_answer");

                // Add the retrieved question to the list
                questionsList.add(new Questions(questionText, optionA, optionB, optionC, optionD, correctAnswer, difficulty));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return questionsList;
    }

    /**
     * Fetches two questions from each difficulty level.
     * @return A list containing six Questions objects (2 easy, 2 medium, 2 hard)
     */
    public static List<Questions> getTwoFromEachDifficulty() {
        List<Questions> allQuestions = new ArrayList<>();
        allQuestions.addAll(getRandomQuestionsWithOptions("easy"));
        allQuestions.addAll(getRandomQuestionsWithOptions("medium"));
        allQuestions.addAll(getRandomQuestionsWithOptions("hard"));
        return allQuestions;
    }
}

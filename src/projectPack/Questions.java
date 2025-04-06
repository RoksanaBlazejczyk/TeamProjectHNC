package projectPack;

public class Questions {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String difficulty;
    private int score;  // Add score field
    private String imageUrl;  // Add image URL field (optional)

    // Updated constructor including score and imageUrl
    public Questions(String questionText, String optionA, String optionB, String optionC, String optionD,
                     String correctAnswer, String difficulty, int score, String imageUrl) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
        this.score = score;  // Initialize score
        this.imageUrl = imageUrl;  // Initialize image URL
    }

    // Getters
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getDifficulty() { return difficulty; }
    public int getScore() { return score; }  // Getter for score
    public String getImageUrl() { return imageUrl; }  // Getter for image URL
}

package projectPack;

public class Questions {
    private String questionText;
    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    private String correctAnswer;
    private String difficulty;  // Add difficulty field

    public Questions(String questionText, String optionA, String optionB, String optionC, String optionD, String correctAnswer, String difficulty) {
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;  // Initialize difficulty
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOptionA() {
        return optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getDifficulty() {   // Getter for difficulty
        return difficulty;
    }
}

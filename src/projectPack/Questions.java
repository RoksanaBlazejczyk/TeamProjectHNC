package projectPack;

public class Questions {
    int id;
    String questionText, optionA, optionB, optionC, optionD, correctAnswer, difficulty, imageUrl;
    int score;

    /**
     *
     * @param id
     * @param questionText
     * @param optionA
     * @param optionB
     * @param optionC
     * @param optionD
     * @param correctAnswer
     * @param difficulty
     * @param score
     * @param imageUrl
     * Corrected constructor, now includes 'id' as a parameter
     */

    public Questions(int id, String questionText, String optionA, String optionB, String optionC, String optionD,
                     String correctAnswer, String difficulty, int score, String imageUrl) {
        this.id = id;
        this.questionText = questionText;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAnswer = correctAnswer;
        this.difficulty = difficulty;
        this.score = score;
        this.imageUrl = imageUrl;
    }

    //Getters
    public int getId() { return id; }  //Added getter for id
    public String getQuestionText() { return questionText; }
    public String getOptionA() { return optionA; }
    public String getOptionB() { return optionB; }
    public String getOptionC() { return optionC; }
    public String getOptionD() { return optionD; }
    public String getCorrectAnswer() { return correctAnswer; }
    public String getDifficulty() { return difficulty; }
    public int getScore() { return score; }
    public String getImageUrl() { return imageUrl; }
}

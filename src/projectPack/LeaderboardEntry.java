package projectPack;

public class LeaderboardEntry {
    private int id;
    private String name;
    private int iqScore;
    private String timeTaken;
    private boolean optOut;  // Field for opt-out status
    private int rank;  // Add rank field

    /**
     * Constructor without rank
     * @param id
     * @param name
     * @param iqScore
     * @param timeTaken
     * @param optOut
     */

    public LeaderboardEntry(int id, String name, int iqScore, String timeTaken, boolean optOut) {
        this.id = id;
        this.name = name;
        this.iqScore = iqScore;
        this.timeTaken = timeTaken;
        this.optOut = optOut;
        this.rank = -1; // Default value if rank isn't set
    }

    /**
     * Constructor with rank
     * @param id
     * @param name
     * @param iqScore
     * @param timeTaken
     * @param rank
     * @param optOut
     */

    public LeaderboardEntry(int id, String name, int iqScore, String timeTaken, int rank, boolean optOut) {
        this.id = id;
        this.name = name;
        this.iqScore = iqScore;
        this.timeTaken = timeTaken;
        this.optOut = optOut;
        this.rank = rank;
    }


    /**
     * If the user opted out, display 'Anonymous'
     * @return
     */
    public String getName() {

        return optOut ? "Anonymous" : name;
    }

    /**
     *
     * @return
     */
    public int getIqScore() {
        return iqScore;
    }

    /**
     *
     * @return
     */
    public String getTimeTaken() {
        return timeTaken;
    }

    /**
     *
     * @return
     */
    public int getRank() {
        return rank;
    }

    /**
     *
     * @param rank
     */
    //Setter for rank
    public void setRank(int rank) {
        this.rank = rank;
    }


    //Override toString for better formatting
    @Override
    public String toString() {
        return (rank > 0 ? "#" + rank + ": " : "") + getName() + " - IQ: " + iqScore + " - Time: " + timeTaken;
    }
}

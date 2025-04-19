package projectPack;

public class LeaderboardEntry {
    private int id;
    private String name;
    private int iqScore;
    private String timeTaken;
    private boolean optOut;  // Field for opt-out status
    private int rank;  // Add rank field

    // Getter for optOut status
    public boolean isOptOut() {
        return optOut;
    }

    // Setter for optOut status
    public void setOptOut(boolean optOut) {
        this.optOut = optOut;
    }

    // Constructor without rank
    public LeaderboardEntry(int id, String name, int iqScore, String timeTaken, boolean optOut) {
        this.id = id;
        this.name = name;
        this.iqScore = iqScore;
        this.timeTaken = timeTaken;
        this.optOut = optOut;
        this.rank = -1; // Default value if rank isn't set
    }

    // Constructor with rank
    public LeaderboardEntry(int id, String name, int iqScore, String timeTaken, int rank, boolean optOut) {
        this.id = id;
        this.name = name;
        this.iqScore = iqScore;
        this.timeTaken = timeTaken;
        this.optOut = optOut;
        this.rank = rank;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        // If the user opted out, display 'Anonymous'
        return optOut ? "Anonymous" : name;
    }

    public int getIqScore() {
        return iqScore;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public int getRank() {
        return rank;
    }

    // Setter for rank
    public void setRank(int rank) {
        this.rank = rank;
    }

    // Override toString for better formatting
    @Override
    public String toString() {
        return (rank > 0 ? "#" + rank + ": " : "") + getName() + " - IQ: " + iqScore + " - Time: " + timeTaken;
    }
}

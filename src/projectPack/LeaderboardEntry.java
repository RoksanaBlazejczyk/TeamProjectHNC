package projectPack;

public class LeaderboardEntry {
    private int id;
    private String name;
    private int iqScore;
    private String timeTaken;
    private int rank;  // Add rank field

    // Constructor without rank
    public LeaderboardEntry(int id, String name, int iqScore, String timeTaken) {
        this.id = id;
        this.name = name;
        this.iqScore = iqScore;
        this.timeTaken = timeTaken;
        this.rank = -1; // Default value if rank isn't set
    }

    // Constructor with rank
    public LeaderboardEntry(int id, String name, int iqScore, String timeTaken, int rank) {
        this.id = id;
        this.name = name;
        this.iqScore = iqScore;
        this.timeTaken = timeTaken;
        this.rank = rank;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
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

    // Optional: Override toString for better formatting
    @Override
    public String toString() {
        return (rank > 0 ? "#" + rank + ": " : "") + name + " - IQ: " + iqScore + " - Time: " + timeTaken;
    }
}

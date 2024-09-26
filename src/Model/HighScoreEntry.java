package Model;
import java.io.Serializable;

public class HighScoreEntry implements Serializable, Comparable<HighScoreEntry> {
    private String playerName;
    private int score;

    public HighScoreEntry(String playerName, int score) {
        this.playerName = playerName;
        this.score = score;
    }

    @Override
    public int compareTo(HighScoreEntry other) {
        return Integer.compare(other.score, this.score);
    }

    @Override
    public String toString() {
        return playerName + " - " + score;
    }
}

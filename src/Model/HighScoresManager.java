package Model;

import java.io.*;
import java.util.*;

public class HighScoresManager {
    private List<HighScoreEntry> highScores;
    private static final String FILE_PATH = "high_scores.ser";

    public HighScoresManager() {
        loadScores();
    }

    public void addScore(HighScoreEntry entry) {
        highScores.add(entry);
        Collections.sort(highScores);
        saveScores();
    }

    public List<HighScoreEntry> getHighScores() {
        return highScores;
    }

    private void saveScores() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FILE_PATH))) {
            oos.writeObject(highScores);
        } catch (IOException e) {
            System.err.println("Error saving high scores: " + e.getMessage());
        }
    }

    private void loadScores() {
        try (ObjectInputStream input = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            highScores = (List<HighScoreEntry>) input.readObject();
        } catch (IOException | ClassNotFoundException e) {
            highScores = new ArrayList<>(); // If file not found, create a new list
        }
    }
}

package View;

import java.awt.*;
import javax.swing.*;
import Model.HighScoresManager;
import Model.HighScoreEntry;

public class HighScoresView extends JFrame {
    private JList<String> highScoresList;

    public HighScoresView(HighScoresManager manager) {
        setTitle("High Scores");
        setSize(300, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        highScoresList = new JList<>(
                manager.getHighScores().stream().map(HighScoreEntry::toString).toArray(String[]::new)
        );

        JScrollPane scrollPane = new JScrollPane(highScoresList);
        add(scrollPane, BorderLayout.CENTER);
    }
}

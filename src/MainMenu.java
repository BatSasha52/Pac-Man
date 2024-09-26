import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

import Model.HighScoresManager;
import View.HighScoresView;
public class MainMenu extends JFrame {
    ImageIcon backgroundIcon;
    public MainMenu() {
        setTitle("Pacman");
        setSize(500, 600);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel backgroundPanel = createBackgroundPanel();
        setContentPane(backgroundPanel);
        createMenu(backgroundPanel);
    }

    private JPanel createBackgroundPanel() {

        String file = "./resources/Images/pac-man-logo.png";
        try {
            byte[] byteArray = Files.readAllBytes(Paths.get(file));
            backgroundIcon = new ImageIcon(byteArray);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
        //ImageIcon backgroundIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/pac-man-logo.png")));

        // Create a panel to display the background image
        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.setColor(Color.BLACK);
                g.fillRect(0, 0, getWidth(), getHeight());
                int x = (getWidth() - backgroundIcon.getIconWidth()) / 2;
                int y = 10;
                g.drawImage(backgroundIcon.getImage(), x, y, this);
            }
        };
        backgroundPanel.setLayout(null);
        return backgroundPanel;
    }

    private void createMenu(JPanel backgroundPanel) {
        JButton btnNewGame = new JButton("New Game");
        JButton btnHighScores = new JButton("High Scores");
        JButton btnExit = new JButton("Exit");

        int buttonWidth = 300;
        int buttonHeight = 90;

        btnNewGame.setBounds(100, 200, buttonWidth, buttonHeight);
        btnHighScores.setBounds(100, 300, buttonWidth, buttonHeight);
        btnExit.setBounds(100, 400, buttonWidth, buttonHeight);

        backgroundPanel.add(btnNewGame);
        backgroundPanel.add(btnHighScores);
        backgroundPanel.add(btnExit);

        btnNewGame.addActionListener(e -> startNewGame());
        btnHighScores.addActionListener(e -> viewHighScores());
        btnExit.addActionListener(e -> System.exit(0));
    }
    
    private void startNewGame() {
        String rowsInput = JOptionPane.showInputDialog(this, "Enter the number of rows:");
        String colsInput = JOptionPane.showInputDialog(this, "Enter the number of columns:");

        int rows = Integer.parseInt(rowsInput);
        int cols = Integer.parseInt(colsInput);
    
        GameFrame gameFrame = new GameFrame(rows, cols);
        gameFrame.setVisible(true);
    
        this.setVisible(false);
    }    
    
    private void viewHighScores() {
        HighScoresManager manager = new HighScoresManager();
        HighScoresView window = new HighScoresView(manager);
        window.setVisible(true);
    }

}

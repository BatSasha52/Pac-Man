import javax.swing.*;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import Model.PacmanTableModel;
import Utils.CellType;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.InputEvent;

import Model.GameModel;
import Model.GameModel.GameModelListener;
import Model.HighScoresManager;
import Model.HighScoreEntry;
import View.PacManView;
import View.GameScaler;
import View.GameView;
import View.GameCellRenderer;

public class GameFrame extends JFrame implements GameModelListener {
    private JTable gameTable;
    private GameModel gameModel;
    private GameView gameView;
    private GameScaler scaler;
    private Dimension baseSize = new Dimension(800, 600);
    private int rows;
    private int cols;
    private JLabel scoreLabel;
    private JLabel timerLabel;
    private JLabel livesLabel;
    private TimeThread timeThread;
    private long startTime;
    private boolean endGameExecuted;
    private PacManView pacManView;
    private static final int cWidth = 35; // example value
    private static final int cHeight = 35;

    public GameFrame(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        gameModel = new GameModel(rows, cols);
        this.pacManView = new PacManView(gameModel, gameModel.getPacMan());
        KeyStroke escapeKeyStroke = KeyStroke.getKeyStroke(KeyEvent.VK_Q, InputEvent.CTRL_DOWN_MASK | InputEvent.SHIFT_DOWN_MASK);
        getRootPane().getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(escapeKeyStroke, "RETURN_TO_MAIN_MENU");
        getRootPane().getActionMap().put("RETURN_TO_MAIN_MENU", returnToMainMenuAction);

        initializeGameTable(gameModel);

        JPanel bottomPanel = new JPanel(new BorderLayout());

        scoreLabel = new JLabel("Score: 0");
        scoreLabel.setHorizontalAlignment(JLabel.CENTER); // Center the text
        bottomPanel.add(scoreLabel, BorderLayout.CENTER);
        setupFrame();
        this.scaler = new GameScaler(baseSize);
        gameView = new GameView(gameModel, baseSize);
        addKeyListener(new MyKeyAdapter(gameModel));
        gameModel.addGameModelListener(this);

        timerLabel = new JLabel("Time: 0");
        bottomPanel.add(timerLabel, BorderLayout.WEST);
        timeThread = new TimeThread(this);
        timeThread.start(); // Start the time thread

        livesLabel = new JLabel("Lives: " + gameModel.getPacMan().getLives());
        bottomPanel.add(livesLabel, BorderLayout.EAST);

        add(bottomPanel, BorderLayout.SOUTH);

        startGameLoop();
    }

    private void initializeGameTable(GameModel gameModel) {
    PacmanTableModel tableModel = new PacmanTableModel(gameModel);
    gameTable = new JTable(tableModel) {
        @Override
        public Dimension getPreferredScrollableViewportSize() {
            return new Dimension(cols * cWidth, rows * cHeight);
        }
    };

    gameTable.setRowHeight(cWidth);
    TableColumnModel columnModel = gameTable.getColumnModel();
    for (int i = 0; i < columnModel.getColumnCount(); i++) {
        columnModel.getColumn(i).setPreferredWidth(cHeight);
    }

    gameTable.setFillsViewportHeight(true);
    gameTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

    gameTable.setShowGrid(false);
    gameTable.setGridColor(Color.BLACK);
    gameTable.setTableHeader(null);

    gameTable.setDefaultRenderer(CellType.class, new GameCellRenderer(gameModel, pacManView));

    gameTable.setBackground(Color.BLACK);
    JScrollPane scrollPane = new JScrollPane(gameTable);
    scrollPane.getViewport().setBackground(Color.BLACK);
    this.add(scrollPane);
    gameTable.setFocusable(false);
    }

    private void setupFrame() {
        setTitle("Pac-Man");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setPreferredSize(new Dimension(900, 900));
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public void modelChanged() {
        SwingUtilities.invokeLater(() -> {
            gameView.updateView(scaler);
            scoreLabel.setText("Score: " + gameModel.getScore());
            ((AbstractTableModel)gameTable.getModel()).fireTableDataChanged(); // Notify the table model
            livesLabel.setText("Lives: " + gameModel.getPacMan().getLives());

            if (gameModel.isGameOver() && !endGameExecuted) {
                endGameExecuted = true;

                String playerName = JOptionPane.showInputDialog(this, "Enter your name for high scores:");
                if (playerName != null && !playerName.trim().isEmpty()) {
                    HighScoresManager manager = new HighScoresManager();
                    manager.addScore(new HighScoreEntry(playerName, gameModel.getScore()));
                }

                timeThread.stopTimer();

                this.dispose();

                // Return to main menu
                EventQueue.invokeLater(() -> {
                    MainMenu mainMenu = new MainMenu();
                    mainMenu.setVisible(true);
                });
            }
        });
    }

    public void updateTimeLabel(long elapsedSeconds) {
        long minutes = elapsedSeconds / 60;
        long seconds = elapsedSeconds % 60;
        String timeFormatted = String.format("Time: %d:%02d", minutes, seconds);
        timerLabel.setText(timeFormatted);
    }

    // Ctrl+Shift+Q hotkey
    Action returnToMainMenuAction = new AbstractAction() {
        @Override
        public void actionPerformed(ActionEvent e) {
            GameFrame.this.dispose();

            if (timeThread != null) {
                timeThread.stopTimer();
            }
            GameFrame.this.dispose();

            EventQueue.invokeLater(() -> {
                MainMenu mainMenu = new MainMenu();
                mainMenu.setVisible(true);
            });
        }
    };

    private void startGameLoop() {
        // Initialize the start time
        new Thread(() -> {
            while (!gameModel.isGameOver()) {
                try {
                    Thread.sleep(160); // Control the game speed
                    SwingUtilities.invokeLater(() -> {
                        gameModel.updateGame();
                        ((AbstractTableModel)gameTable.getModel()).fireTableDataChanged();
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }
            if (timeThread != null) {
                timeThread.stopTimer(); // Assuming you have a stopTimer() method in your TimeThread class
            }
        }).start();
        timeThread.start();
        startTime = System.currentTimeMillis();
    }
}

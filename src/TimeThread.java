import javax.swing.SwingUtilities;

public class TimeThread extends Thread {
    private final GameFrame gameFrame;
    private volatile boolean running = true; // Flag to control the thread
    private long startTime;
    private long elapsedSeconds = 0;

    public TimeThread(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        this.startTime = System.currentTimeMillis();
    }

    public void stopTimer() {
        running = false;
        interrupt();
    }

    @Override
    public void run() {
        while (running) {
            try {
                Thread.sleep(1000); // Sleep for 1 second
                elapsedSeconds = (System.currentTimeMillis() - startTime) / 1000;
                SwingUtilities.invokeLater(() -> {
                    gameFrame.updateTimeLabel(elapsedSeconds); // Update the time label
                });
            } catch (InterruptedException e) {
                running = false; // Stop the thread if interrupted
            }
        }
    }
}

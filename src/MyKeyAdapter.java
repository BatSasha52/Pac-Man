import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import Model.GameModel;
import Utils.Direction;

public class MyKeyAdapter extends KeyAdapter {
    private GameModel gameModel;

    MyKeyAdapter(GameModel gameModel) {
        this.gameModel = gameModel;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        Direction newDirection = null;
        switch(e.getKeyCode()) {
            case KeyEvent.VK_LEFT:
                newDirection = Direction.LEFT;
                break;
            case KeyEvent.VK_RIGHT:
                newDirection = Direction.RIGHT;
                break;
            case KeyEvent.VK_UP:
                newDirection = Direction.UP;
                break;
            case KeyEvent.VK_DOWN:
                newDirection = Direction.DOWN;
                break;
        }

        if (newDirection != null) {
            gameModel.setPacmanDirection(newDirection);
        }
    }
}

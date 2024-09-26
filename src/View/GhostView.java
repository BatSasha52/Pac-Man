package View;

import Model.Ghost;
import javax.swing.ImageIcon;
import java.awt.Point;
import java.util.Objects;

public class GhostView extends ObjectView {
    private Ghost ghost;
    private GameScaler scaler;

    public GhostView(Ghost ghost) {
        super(2);
        this.ghost = ghost;
        ImageIcon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/GhostRight.png")));
        setIcon(icon);
    }
    public void updatePosition() {
        Point scaledPosition = scaler.getScaledPoint(ghost.getPosition());
        setLocation(scaledPosition.x, scaledPosition.y);
    }
    @Override
    public void updateObjectView(GameScaler scaler) {
        Point scaledPosition = scaler.getScaledPoint(ghost.getPosition());
        setLocation(scaledPosition);
        repaint();
    }
}

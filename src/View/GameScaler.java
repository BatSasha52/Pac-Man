package View;

import java.awt.Dimension;
import java.awt.Point;

public class GameScaler {
    private double scale;
    private final Dimension baseSize;

    public GameScaler(Dimension baseSize) {
        this.baseSize = baseSize;
        this.scale = 1.0; // Default scale is 1:1
    }

    public void updateScale(Dimension currentSize) {
        double scaleX = currentSize.getWidth() / baseSize.getWidth();
        double scaleY = currentSize.getHeight() / baseSize.getHeight();
        scale = Math.min(scaleX, scaleY);
    }

    public Point getScaledPoint(Point originalPoint) {
        return new Point((int)(originalPoint.x * scale), (int)(originalPoint.y * scale));
    }

    public Dimension getScaledDimension(Dimension originalDimension) {
        return new Dimension((int)(originalDimension.width * scale), (int)(originalDimension.height * scale));
    }
}

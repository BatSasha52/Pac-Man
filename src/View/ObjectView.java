package View;

import javax.swing.JLabel;

public abstract class ObjectView extends JLabel {
    protected int layer;

    public ObjectView(int layer) {
        this.layer = layer;
    }

    public abstract void updateObjectView(GameScaler scaler);
    
}

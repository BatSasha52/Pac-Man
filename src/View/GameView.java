package View;

import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.util.ArrayList;
import java.awt.Dimension;

import javax.swing.JLayeredPane;

import Model.GameModel;

public class GameView extends JLayeredPane {
    private GameModel model;
    private ArrayList<ObjectView> objectViews;

    public GameView(GameModel model, Dimension baseSize) {
        this.model = model;
        this.objectViews = new ArrayList<>();
        setLayout(null);
    
        GameScaler scaler = new GameScaler(baseSize);
    
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                scaler.updateScale(getSize());
                updateView(scaler);
            }
        });
    }
    public void updateView(GameScaler scaler) {
        for (ObjectView view : objectViews) {
            view.updateObjectView(scaler);
        }
        revalidate();
        repaint();
    }
}

package View;

import Model.PacMan;
import Utils.Direction;
import Model.GameModel;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class PacManView extends ObjectView {
    private GameModel model;
    private PacMan pacMan;
    private ImageIcon iconUp;
    private ImageIcon iconDown;
    private ImageIcon iconLeft;
    private ImageIcon iconRight;
    private ImageIcon icon;

    public PacManView(GameModel model, PacMan pacMan) {
        super(1);
        this.pacMan = pacMan;
        this.model = model;
        String fileUp = "./resources/Images/PacmanUp3.png";
        String fileDown = "./resources/Images/PacmanDown3.png";
        String fileLeft = "./resources/Images/PacmanLeft3.png";
        String fileRight = "./resources/Images/PacmanRight3.png";
        try {
            byte[] byteArrayUp = Files.readAllBytes(Paths.get(fileUp));
            byte[] byteArrayDown = Files.readAllBytes(Paths.get(fileDown));
            byte[] byteArrayLeft = Files.readAllBytes(Paths.get(fileLeft));
            byte[] byteArrayRight = Files.readAllBytes(Paths.get(fileRight));
            iconUp = new ImageIcon(byteArrayUp);
            iconDown = new ImageIcon(byteArrayDown);
            iconLeft = new ImageIcon(byteArrayLeft);
            iconRight = new ImageIcon(byteArrayRight);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ImageIcon getIcon(Direction direction) {
        setIcon(direction);
        return icon;
    }

    public void setIcon(Direction direction) {
        if(direction != null) {
            switch (direction) {
                case UP:
                    icon = iconUp;
                    break;
                case DOWN:
                    icon = iconDown;
                    break;
                case LEFT:
                    icon = iconLeft;
                    break;
                default:
                    icon = iconRight;
                    break;
            }
        }
        else
            icon = iconRight;
    }

    @Override
    public void updateObjectView(GameScaler scaler) {
        Point scaledPosition = scaler.getScaledPoint(pacMan.getPosition());
        Dimension scaledSize = scaler.getScaledDimension(getPreferredSize());
        setBounds(scaledPosition.x, scaledPosition.y, scaledSize.width, scaledSize.height);
    }
}

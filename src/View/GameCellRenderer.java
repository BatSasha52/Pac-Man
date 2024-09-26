package View;

import Model.GameModel;
import Utils.CellType;
import Utils.Direction;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Objects;

public class GameCellRenderer extends DefaultTableCellRenderer {
    private ImageIcon pacmanIcon;
    private ImageIcon ghostIcon;
    private ImageIcon wallIcon;
    private ImageIcon dotIcon;
    private GameModel gameModel;
    private PacManView pacManView;

    public GameCellRenderer(GameModel gameModel, PacManView pacManView) {
        this.gameModel = gameModel;
        this.pacManView = pacManView;
        String fileGhost = "./resources/Images/GhostRight.png";
        String fileWall = "./resources/Images/Obstacle.png";
        String fileDot = "./resources/Images/Dot.png";
        try {
            byte[] byteArrayGhost = Files.readAllBytes(Paths.get(fileGhost));
            byte[] byteArrayWall = Files.readAllBytes(Paths.get(fileWall));
            byte[] byteArrayDot = Files.readAllBytes(Paths.get(fileDot));
            ghostIcon = new ImageIcon(byteArrayGhost);
            wallIcon = new ImageIcon(byteArrayWall);
            dotIcon = new ImageIcon(byteArrayDot);
        }  catch (IOException e) {
            throw new RuntimeException(e);
        }
        //ghostIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/GhostRight.png")));
        //wallIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/Obstacle.png")));
        //dotIcon = new ImageIcon(Objects.requireNonNull(getClass().getResource("/Images/Dot.png")));
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (value instanceof CellType) {
            CellType type = (CellType) value;
            setIcon(null); // Clear any previous icon
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
            int cellHeight = wallIcon.getIconHeight();
            switch (type) {
                case PACMAN:
                    Direction pacManDirection = gameModel.getPacMan().getDirection();
                    ImageIcon pacmanIcon = pacManView.getIcon(pacManDirection);
                    setIcon(resizeIcon(pacmanIcon, cellHeight/2, cellHeight/2));
                    break;
                case GHOST:
                    setIcon(resizeIcon(ghostIcon, cellHeight/2, cellHeight/2));
                    break;
                case WALL:
                    setIcon(resizeIcon(wallIcon, cellHeight * 2, cellHeight * 2));
                    break;
                case DOT:
                    setIcon(resizeIcon(dotIcon, cellHeight /8, cellHeight/8));
                    break;
            }
        }
        setText("");
        setBackground(Color.BLACK);
        return this;
    }

    private Icon resizeIcon(ImageIcon icon, int cellWidth, int cellHeight) {
        int originalWidth = icon.getIconWidth();
        int originalHeight = icon.getIconHeight();
        double widthToHeightRatio = (double) originalWidth / originalHeight;

        int newWidth = cellWidth;
        int newHeight = (int) (newWidth / widthToHeightRatio);

        if(newHeight > cellHeight) {
            newHeight = cellHeight;
            newWidth = (int) (newHeight * widthToHeightRatio);
        }

        Image img = icon.getImage();
        Image resizedImage = img.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
        return new ImageIcon(resizedImage);
    }
}

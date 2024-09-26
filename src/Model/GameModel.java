package Model;

import java.awt.Point;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;


import Utils.CellType;
import Utils.Direction;

public class GameModel {
    private PacMan pacMan;
    private List<Ghost> ghosts;
    private CellType[][] grid;
    private List<GameModelListener> listeners = new ArrayList<>();
    private Set<Point> dotPositions = new HashSet<>();
    private int score;
    private boolean isGameOver;
    private boolean isTimeFrozen;
    private int rows;
    private int cols;
    int ghostUpdateCounter;

    public GameModel(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.grid = new CellType[rows][cols];
        this.pacMan = new PacMan(new Point(0, 0), this);
        this.ghosts = new ArrayList<>();

        MazeGenerator mazeGenerator = new MazeGenerator(rows, cols);
        mazeGenerator.generateMaze();
        CellType[][] maze = mazeGenerator.getMaze();

        initializeBoard(maze);
    }

    public interface GameModelListener {
        void modelChanged();
    }

    public void addGameModelListener(GameModelListener listener) {
        listeners.add(listener);
    }
    
    private void notifyListeners() {
        for (GameModelListener listener : listeners) {
            listener.modelChanged();
        }
    }

    private void initializeBoard(CellType[][] maze) {
        // Overlay the maze onto the grid
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // If the maze cell is a pathway, set it as a DOT; otherwise, keep it as a WALL.
                grid[i][j] = (maze[i][j] == CellType.EMPTY) ? CellType.DOT : maze[i][j];
            }
        }

        // Place pacman and ghosts at a random dot positions
        Point pacManPosition = generateRandomPosition(CellType.DOT);
        pacMan.setPosition(pacManPosition);
        grid[pacManPosition.y][pacManPosition.x] = CellType.PACMAN;

        for (int i = 0; i < 4; i++) {
            Point ghostPosition = generateRandomPosition(CellType.DOT);
            ghosts.add(new Ghost(ghostPosition));
            grid[ghostPosition.y][ghostPosition.x] = CellType.GHOST;
        }

        //remember dot initial positions
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == CellType.DOT) {
                    dotPositions.add(new Point(j, i));
                }
            }
        }
    }

    private Point generateRandomPosition(CellType targetType) {
        Random random = new Random();
        int x, y;
        do {
            x = random.nextInt(cols);
            y = random.nextInt(rows);
        } while (grid[y][x] != targetType);
        return new Point(x, y);
    }

    public void updateGame() {

        if (pacMan.getDirection() != null) {
            int oldPositionY = pacMan.getPosition().y;
            int oldPositionX = pacMan.getPosition().x;
            pacMan.move(pacMan.getDirection());
            checkCollisions();

            Point newPosition = pacMan.getPosition();

            grid[oldPositionY][oldPositionX] = CellType.EMPTY;
            grid[newPosition.y][newPosition.x] = CellType.PACMAN;
        }

        if(!isTimeFrozen) {
            ghostUpdateCounter++;
            if (ghostUpdateCounter >= 3) //ghosts move every 3rd update to be slower
                for (Ghost ghost : ghosts) {
                    Point oldPosition = ghost.getPosition();
                    if (dotPositions.contains(oldPosition)) {
                        grid[oldPosition.y][oldPosition.x] = CellType.DOT;
                    } else {
                        grid[oldPosition.y][oldPosition.x] = CellType.EMPTY;
                    }
                    ghost.move(this);
                    ghostUpdateCounter = 0;

                    Point newPosition = ghost.getPosition();
                    if (grid[newPosition.y][newPosition.x] != CellType.PACMAN) {
                        grid[newPosition.y][newPosition.x] = CellType.GHOST;
                    }
                }
        }

        checkGameOver();
        notifyListeners();

    }

    private void checkCollisions() {
        // pacman and dot collision
        if (grid[pacMan.getPosition().y][pacMan.getPosition().x] == CellType.DOT) {
            score += 5;
            grid[pacMan.getPosition().y][pacMan.getPosition().x] = CellType.EMPTY; // Remove the dot
            dotPositions.remove(pacMan.getPosition());
        }

        // pacman and ghost collision
        for (Ghost ghost : ghosts) {
            if (pacMan.getPosition().equals(ghost.getPosition())) {
                pacMan.loseLife();
                score -= 50;
                if (!pacMan.isAlive()) {
                    isGameOver = true;
                } else {
                    Point respawnPosition = generateRandomPosition(CellType.EMPTY);
                    pacMan.setPosition(respawnPosition);
                }
                notifyListeners();
            }
        }
    }

    private void checkGameOver() {
        // Check if all dots are eaten
        boolean allDotsEaten = true;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (grid[i][j] == CellType.DOT) {
                    allDotsEaten = false;
                    break;
                }
            }
            if (!allDotsEaten) {
                break;
            }
        }

        if (allDotsEaten) {
            isGameOver = true;
        }
    }

    public List<Direction> getValidDirections(Point position) {
        List<Direction> validDirections = new ArrayList<>();
        // Check each direction and add it to the list if it's not a wall
        if (!isWall(position.x, position.y - 1)) validDirections.add(Direction.UP);
        if (!isWall(position.x, position.y + 1)) validDirections.add(Direction.DOWN);
        if (!isWall(position.x - 1, position.y)) validDirections.add(Direction.LEFT);
        if (!isWall(position.x + 1, position.y)) validDirections.add(Direction.RIGHT);
        return validDirections;
    }    

    public boolean isWall(int x, int y) {
        if (x < 0 || y < 0 || x >= cols || y >= rows) {
            return true; // Out of bounds is considered a wall
        }
        return grid[y][x] == CellType.WALL;
    }

    public void setPacmanDirection(Direction newDirection) {
        pacMan.setDirection(newDirection);
    }
    public int getRows() {
        return rows;
    }
    public int getCols() {
        return cols;
    }
    public CellType getCellType(int row, int col) {
        return grid[row][col];
    }
    public boolean isGameOver() {
        return isGameOver;
    }
    public int getScore() {
        return score;
    }
    public PacMan getPacMan() {
        return this.pacMan;
    }
}

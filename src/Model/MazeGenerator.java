package Model;

import Utils.CellType;

import java.util.*;

public class MazeGenerator {
    private final int rows;
    private final int cols;
    private final CellType[][] maze;
    private final Random random = new Random();

    public MazeGenerator(int rows, int cols) {
        this.rows = rows;
        this.cols = cols;
        this.maze = new CellType[rows][cols];
    }

    public void generateMaze() {
        // Initialize all cells as walls
        for (int r = 0; r < rows; r++) {
            Arrays.fill(maze[r], CellType.WALL);
        }

        // Start the maze from a random cell
        int startRow = random.nextInt(rows);
        int startCol = random.nextInt(cols);

        // Make sure we start at an odd row and column
        startRow = (startRow % 2 == 0) ? startRow + 1 : startRow;
        startCol = (startCol % 2 == 0) ? startCol + 1 : startCol;

        // Perform the Depth-First Search (DFS) to carve paths
        dfs(startRow, startCol);

        // Ensure at least one exit point in the maze
        if (maze[1][0] == CellType.WALL) {
            maze[1][0] = CellType.EMPTY;
        }

        removeRandomWalls(rows * cols / 6);
    }

    private void dfs(int r, int c) {
        // Mark the current cell as an empty path
        maze[r][c] = CellType.EMPTY;

        // Create a list of potential directions and shuffle them for randomness
        int[][] directions = {{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
        Collections.shuffle(Arrays.asList(directions), random);

        // Explore each direction from the current cell
        for (int[] dir : directions) {
            int nextRow = r + dir[0] * 2;
            int nextCol = c + dir[1] * 2;

            // Check if the next cell is within bounds and is a wall
            if (isInBounds(nextRow, nextCol) && maze[nextRow][nextCol] == CellType.WALL) {
                // Carve a path between the current cell and the next cell
                maze[r + dir[0]][c + dir[1]] = CellType.EMPTY;
                dfs(nextRow, nextCol);
            }
        }
    }
    public void removeRandomWalls(int numberOfWallsToRemove) {
        for (int i = 0; i < numberOfWallsToRemove; i++) {
            int row = 2 + random.nextInt(rows - 4); // Avoid the outer walls
            int col = 2 + random.nextInt(cols - 4); // Avoid the outer walls

            if (maze[row][col] == CellType.WALL) {
                maze[row][col] = CellType.EMPTY;
            }
        }
    }

    private boolean isInBounds(int r, int c) {
        return r > 0 && r < rows - 1 && c > 0 && c < cols - 1;
    }

    public CellType[][] getMaze() {
        return maze;
    }
}

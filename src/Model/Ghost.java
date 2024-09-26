package Model;

import java.awt.Point;
import java.util.List;
import java.util.Random;
import Utils.Direction;

public class Ghost {
    private Point position;
    private Direction direction;
    private Random random = new Random();

    public Ghost(Point initialPosition) {
        this.position = initialPosition;
        this.direction = Direction.values()[random.nextInt(Direction.values().length)];
    }

    public void updateDirection(GameModel gameModel) {
        List<Direction> validDirections = gameModel.getValidDirections(position);
        if (!validDirections.isEmpty()) {
            this.direction = validDirections.get(random.nextInt(validDirections.size()));
        } else {
            this.direction = this.direction;
        }
    }

    public void move(GameModel gameModel) {
        Point newPosition = new Point(position);
        moveInDirection(newPosition, this.direction);

        if (gameModel.isWall(newPosition.x, newPosition.y)) {
            updateDirection(gameModel);
            moveInDirection(newPosition, this.direction); // Try to move again in the new direction
        }

        if (!gameModel.isWall(newPosition.x, newPosition.y)) {
            position.setLocation(newPosition); // Update position if not a wall
        }
    }

    private void moveInDirection(Point position, Direction direction) {
        switch (direction) {
            case UP:
                position.translate(0, -1);
                break;
            case DOWN:
                position.translate(0, 1);
                break;
            case LEFT:
                position.translate(-1, 0);
                break;
            case RIGHT:
                position.translate(1, 0);
                break;
        }
    }

    public Point getPosition() {
        return position;
    }

}
    
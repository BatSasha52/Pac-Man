package Model;
import Utils.Direction;
import java.awt.Point;
    public class PacMan {
        private Point position;
        private Direction direction;
        private int lives;
        private GameModel gameModel; // Reference to the GameModel
    
        public PacMan(Point initialPosition, GameModel gameModel) {
            this.position = initialPosition;
            this.gameModel = gameModel;
            this.lives = 3;
        }

        public void setPosition(Point position) {

            this.position = position;
        }

        public Point getPosition() {

            return position;
        }

        public void setDirection(Direction newDirection) {

            this.direction = newDirection;
        }

        public Direction getDirection() {
            return this.direction;
        }

        public void loseLife() {
            this.lives--;
        }

        public void gainLife() {
            this.lives++;
        }

        public boolean isAlive() {
            return this.lives > 0;
        }

        public int getLives() {
            return this.lives;
        }

        public void move(Direction direction) {
            int newX = position.x;
            int newY = position.y;
        
            switch (direction) {
                case UP:
                    newY -= 1;
                    break;
                case DOWN:
                    newY += 1;
                    break;
                case LEFT:
                    newX -= 1;
                    break;
                case RIGHT:
                    newX += 1;
                    break;
            }

            if (!gameModel.isWall(newX, newY)) {
                position.setLocation(newX, newY);
            }
    }
    }
    
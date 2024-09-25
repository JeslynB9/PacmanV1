package pacman.model.entity.dynamic.ghost;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.level.Level;
import pacman.model.maze.Maze;

import java.util.*;

/**
 * Concrete implementation of Ghost entity in Pac-Man Game
 */
public class GhostImpl implements Ghost {
    private final Layer layer = Layer.FOREGROUND;
    private final Image image;
    private final BoundingBox boundingBox;
    private final Vector2D startingPosition;
    private final Vector2D targetCorner;
    private KinematicState kinematicState;
    private GhostMode ghostMode;
    private Vector2D targetLocation;
    private Direction currentDirection;
    private Direction lastDirection;
    private Set<Direction> possibleDirections;
    private Vector2D playerPosition;
    private Map<GhostMode, Double> speeds;
    private int movementCount; // Tracks how long the ghost has moved in one direction
    private static final int MIN_MOVEMENT_TICKS = 10; // Minimum ticks before direction change
    private Pacman pacman;

    public GhostImpl(Image image, BoundingBox boundingBox, KinematicState kinematicState, GhostMode ghostMode, Vector2D targetCorner, Direction currentDirection, Pacman pacman) {
        this.image = image;
        this.boundingBox = boundingBox;
        this.kinematicState = kinematicState;
        this.startingPosition = kinematicState.getPosition();
        this.ghostMode = ghostMode;
        this.currentDirection = currentDirection;
        this.lastDirection = currentDirection;
        this.movementCount = 0;
        this.possibleDirections = new HashSet<>();
        this.targetCorner = targetCorner;
        this.targetLocation = getTargetLocation();
        this.pacman = pacman;
    }

    @Override
    public void setSpeeds(Map<GhostMode, Double> speeds) {
        this.speeds = speeds;
    }

    @Override
    public Image getImage() {
        return image;
    }

    @Override
    public void update() {
        this.updateDirection();
        this.kinematicState.update();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());
        System.out.println("Target Location: " + targetLocation);
    }

    private void updateDirection() {
        // Increment movement count while continuing in the same direction
        movementCount++;

        // Check if at an intersection
        if (Maze.isAtIntersection(this.possibleDirections)) {
            // Only allow direction change if the ghost has moved enough in the current direction
            if (movementCount >= MIN_MOVEMENT_TICKS) {
                this.targetLocation = getTargetLocation();
                this.currentDirection = selectDirection(possibleDirections);
                movementCount = 0; // Reset movement count after changing direction
            }
        }

        // Apply movement in the current direction
        switch (currentDirection) {
            case LEFT -> this.kinematicState.left();
            case RIGHT -> this.kinematicState.right();
            case UP -> this.kinematicState.up();
            case DOWN -> this.kinematicState.down();
        }
    }

    private Vector2D getTargetLocation() {
        Vector2D playerPosition = null;

        if (pacman != null) {
            // Pacman is available, retrieve its position
            playerPosition = pacman.getKinematicState().getPosition();
        } else {
            // Pacman is not initialized, handle this scenario (perhaps with logging)
            System.out.println("Pacman instance is null!");
        }

        // Return target based on ghostMode: CHASE -> playerPosition, SCATTER -> targetCorner
        return switch (this.ghostMode) {
            case CHASE -> playerPosition;  // Chase mode targets Pacman's current position
            case SCATTER -> this.targetCorner;  // Scatter mode targets the ghost's designated corner
        };
    }



    private Direction selectDirection(Set<Direction> possibleDirections) {
        if (possibleDirections.isEmpty()) {
            return currentDirection;
        }

        Map<Direction, Double> distances = new HashMap<>();

        for (Direction direction : possibleDirections) {
            // Ghosts never choose to reverse travel and ensure targetLocation is not null
            if (direction != currentDirection.opposite() && this.targetLocation != null) {
                distances.put(direction, Vector2D.calculateEuclideanDistance(this.kinematicState.getPotentialPosition(direction), this.targetLocation));
            }
        }

        // Select the direction that will reach the target location fastest
        return distances.isEmpty() ? currentDirection :
                Collections.min(distances.entrySet(), Map.Entry.comparingByValue()).getKey();
    }

    @Override
    public void setGhostMode(GhostMode ghostMode) {
        this.ghostMode = ghostMode;
        this.kinematicState.setSpeed(speeds.get(ghostMode));
    }

    @Override
    public boolean collidesWith(Renderable renderable) {
        return boundingBox.collidesWith(kinematicState.getDirection(), renderable.getBoundingBox());
    }

    @Override
    public void collideWith(Level level, Renderable renderable) {
        if (level.isPlayer(renderable)) {
            level.handleLoseLife();
        }
    }

    @Override
    public Vector2D getPositionBeforeLastUpdate() {
        return this.kinematicState.getPreviousPosition();
    }

    @Override
    public double getHeight() {
        return this.boundingBox.getHeight();
    }

    @Override
    public double getWidth() {
        return this.boundingBox.getWidth();
    }

    @Override
    public Vector2D getPosition() {
        return this.kinematicState.getPosition();
    }

    @Override
    public void setPosition(Vector2D position) {
        this.kinematicState.setPosition(position);
    }

    @Override
    public Layer getLayer() {
        return this.layer;
    }

    @Override
    public BoundingBox getBoundingBox() {
        return this.boundingBox;
    }

    @Override
    public void reset() {
        // Return ghost to starting position
        this.kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(startingPosition)
                .build();
        this.boundingBox.setTopLeft(this.kinematicState.getPosition());

        // Reset ghost state
        this.ghostMode = GhostMode.SCATTER;
        this.currentDirection = Direction.LEFT;
        this.lastDirection = currentDirection;
        this.movementCount = 0;
        this.possibleDirections.clear();
        this.targetLocation = getTargetLocation();
    }

    @Override
    public void setPossibleDirections(Set<Direction> possibleDirections) {
        this.possibleDirections = possibleDirections;
    }

    @Override
    public Direction getDirection() {
        return this.kinematicState.getDirection();
    }

    @Override
    public Vector2D getCenter() {
        return new Vector2D(boundingBox.getMiddleX(), boundingBox.getMiddleY());
    }

}

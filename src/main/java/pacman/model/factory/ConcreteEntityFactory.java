package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;
import pacman.model.entity.dynamic.player.PacmanVisual;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.entity.staticentity.Wall;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.staticentity.collectable.Pellet;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class ConcreteEntityFactory implements EntityFactory {

    @Override
    public Renderable createEntity(char type, int x, int y) {
        // Create the bounding box and image here (you would replace this with actual logic for images and bounding boxes)
        BoundingBox boundingBox = new BoundingBoxImpl(new Vector2D(x * 16, y * 16), 16, 16);
        Image wallImage1 = new Image(getClass().getResourceAsStream("/maze/walls/horizontal.png"));
        Image wallImage2 = new Image(getClass().getResourceAsStream("/maze/walls/vertical.png"));
        Image wallImage3 = new Image(getClass().getResourceAsStream("/maze/walls/upLeft.png"));
        Image wallImage4 = new Image(getClass().getResourceAsStream("/maze/walls/upRight.png"));
        Image wallImage5 = new Image(getClass().getResourceAsStream("/maze/walls/downLeft.png"));
        Image wallImage6 = new Image(getClass().getResourceAsStream("/maze/walls/downRight.png"));
        Image pellet = new Image(getClass().getResourceAsStream("/maze/pellet.png"));
        Image ghostImage = new Image(getClass().getResourceAsStream("/maze/ghosts/ghost.png"));

        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(new Vector2D(x * 16, y * 16))
                .setSpeed(1.0)
                .build();

        // Define ghost's corner targets for scatter mode (example coordinates)
        Vector2D topLeftCorner = new Vector2D(0, 0);
        Vector2D bottomRightCorner = new Vector2D(160, 160);

        // Define ghost mode and speeds for each mode (example values)
        Map<GhostMode, Double> speeds = new HashMap<>();
        speeds.put(GhostMode.CHASE, 1.0);
        speeds.put(GhostMode.SCATTER, 0.75);


        switch (type) {
            case '1': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage1);
            case '2': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage2);
            case '3': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage3);
            case '4': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage4);
            case '5': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage5);
            case '6': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage6);
            case '7': return new Pellet(boundingBox, Renderable.Layer.FOREGROUND, pellet, 100);
            // Add Pacman instantiation
            case 'p': {
                // Load Pacman images for different states (up, down, left, right, closed)
                Map<PacmanVisual, Image> pacmanImages = Map.of(
                        PacmanVisual.UP, new Image(getClass().getResourceAsStream("/maze/pacman/playerUp.png")),
                        PacmanVisual.DOWN, new Image(getClass().getResourceAsStream("/maze/pacman/playerDown.png")),
                        PacmanVisual.LEFT, new Image(getClass().getResourceAsStream("/maze/pacman/playerLeft.png")),
                        PacmanVisual.RIGHT, new Image(getClass().getResourceAsStream("/maze/pacman/playerRight.png")),
                        PacmanVisual.CLOSED, new Image(getClass().getResourceAsStream("/maze/pacman/playerClosed.png"))
                );

                // Create a kinematic state for Pacman
                Vector2D position = new Vector2D(x * 16, y * 16);
                KinematicState kinematicStatePacman = new KinematicStateImpl.KinematicStateBuilder()
                        .setPosition(position)
                        .setSpeed(2.0)  // Set initial speed
                        .build();

                // Create the Pacman instance
                return new Pacman(
                        pacmanImages.get(PacmanVisual.LEFT), // Default to the left-facing image
                        pacmanImages,
                        boundingBox,
                        kinematicState
                );

            }

            case 'g':
                // Create a ghost with an initial mode of SCATTER and target the bottom-right corner
                Ghost ghost = new GhostImpl(
                        ghostImage,
                        boundingBox,
                        kinematicState,
                        GhostMode.SCATTER,
                        bottomRightCorner,
                        Direction.LEFT
                );
                ghost.setSpeeds(speeds);
                return ghost;

            default: return null;
        }
    }
}

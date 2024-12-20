package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.player.PacmanVisual;
import pacman.model.entity.dynamic.physics.KinematicStateImpl;
import pacman.model.entity.dynamic.physics.KinematicState;
import javafx.scene.image.Image;

import java.util.Map;

public class PacmanFactory implements EntityFactory {
    private static Pacman pacman;

    @Override
    public Renderable createEntity(char type, int x, int y) {
        /// Load Pacman images for different states
        Map<PacmanVisual, Image> pacmanImages = Map.of(
                PacmanVisual.UP, new Image(getClass().getResourceAsStream("/maze/pacman/playerUp.png")),
                PacmanVisual.DOWN, new Image(getClass().getResourceAsStream("/maze/pacman/playerDown.png")),
                PacmanVisual.LEFT, new Image(getClass().getResourceAsStream("/maze/pacman/playerLeft.png")),
                PacmanVisual.RIGHT, new Image(getClass().getResourceAsStream("/maze/pacman/playerRight.png")),
                PacmanVisual.CLOSED, new Image(getClass().getResourceAsStream("/maze/pacman/playerClosed.png"))
        );

        // Create bounding box and kinematic state
        BoundingBoxImpl boundingBox = new BoundingBoxImpl(new Vector2D(x * 16 + 4, y * 16 - 4), pacmanImages.get(PacmanVisual.LEFT).getHeight(), pacmanImages.get(PacmanVisual.LEFT).getWidth());
        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(new Vector2D(x * 16 + 4, y * 16 - 4))
                .setSpeed(2.0)  // Set initial speed
                .build();

        // Create a new Pacman instance
        pacman = new Pacman(pacmanImages.get(PacmanVisual.LEFT), pacmanImages, boundingBox, kinematicState);

        return pacman;
    }

    public static Pacman getPacman() {
        return pacman;
    }
}



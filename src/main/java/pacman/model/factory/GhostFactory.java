package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;
import javafx.scene.image.Image;
import java.util.Random;
import pacman.model.entity.dynamic.player.Pacman;

import java.util.Map;
import java.util.HashMap;

public class GhostFactory implements EntityFactory {
    private final Vector2D[] corners = {
            new Vector2D(0, 4),
            new Vector2D(28, 4),
            new Vector2D(0, 34),
            new Vector2D(28, 24)
    };
    private final Pacman pacman; // Store reference to Pacman

    public GhostFactory(Pacman pacman) {
        this.pacman = pacman; // Pass Pacman in constructor
    }

    @Override
    public Renderable createEntity(char type, int x, int y) {
        Image ghostImage = new Image(getClass().getResourceAsStream("/maze/ghosts/ghost.png"));
        BoundingBox boundingBox = new BoundingBoxImpl(new Vector2D(x * 16 + 4, y * 16 - 4), ghostImage.getHeight(), ghostImage.getWidth());
        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(new Vector2D(x * 16 + 4, y * 16 - 4))
                .setSpeed(1.0)
                .build();

        Random random = new Random();
        Vector2D targetCorner = corners[random.nextInt(corners.length)];

        return new GhostImpl(ghostImage, boundingBox, kinematicState, GhostMode.SCATTER, targetCorner, Direction.LEFT, pacman); // Use the passed Pacman
    }
}


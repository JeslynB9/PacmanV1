package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.*;
import javafx.scene.image.Image;

import java.util.Map;
import java.util.HashMap;

public class GhostFactory implements EntityFactory {

    @Override
    public Renderable createEntity(char type, int x, int y) {
        BoundingBox boundingBox = new BoundingBoxImpl(new Vector2D(x * 16, y * 16), 16, 16);
        KinematicState kinematicState = new KinematicStateImpl.KinematicStateBuilder()
                .setPosition(new Vector2D(x * 16, y * 16))
                .setSpeed(1.0)
                .build();

        Image ghostImage = new Image(getClass().getResourceAsStream("/maze/ghosts/ghost.png"));

        // Define ghost mode speeds
        Map<GhostMode, Double> speeds = new HashMap<>();
        speeds.put(GhostMode.CHASE, 1.0);
        speeds.put(GhostMode.SCATTER, 0.75);

        return new GhostImpl(ghostImage, boundingBox, kinematicState, GhostMode.SCATTER, new Vector2D(160, 160), Direction.LEFT);
    }
}

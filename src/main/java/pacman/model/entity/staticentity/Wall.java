package pacman.model.entity.staticentity;

import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;

/**
 * Represents a wall entity in the Pac-Man game.
 * Walls are static entities that block movement.
 */
public class Wall extends StaticEntityImpl {

    private final String wallType;

    public Wall(BoundingBox boundingBox, Layer layer, Image image, String wallType) {
        super(boundingBox, layer, image);
        this.wallType = wallType;
    }

    @Override
    public boolean canPassThrough() {
        return false;
    }
}

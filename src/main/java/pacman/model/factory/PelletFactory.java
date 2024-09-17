package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.collectable.Pellet;
import pacman.model.entity.dynamic.physics.BoundingBox;
import javafx.scene.image.Image;

public class PelletFactory implements EntityFactory {
    @Override
    public Renderable createEntity(char type, int x, int y) {
        BoundingBox boundingBox = new BoundingBoxImpl(new Vector2D(x * 16, y * 16), 16, 16);
        Image pelletImage = new Image(getClass().getResourceAsStream("/maze/pellet.png"));
        return new Pellet(boundingBox, Renderable.Layer.FOREGROUND, pelletImage, 100);
    }
}

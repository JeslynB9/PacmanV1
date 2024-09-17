package pacman.model.factory;

import javafx.scene.image.Image;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBox;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;

public class Wall2Factory implements EntityFactory {
    @Override
    public Renderable createEntity(char type, int x, int y) {
        BoundingBox boundingBox = new BoundingBoxImpl(new Vector2D(x * 16, y * 16), 16, 16);
        Image wallImage = new Image(getClass().getResourceAsStream("/maze/walls/vertical.png"));
        return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage);
    }
}

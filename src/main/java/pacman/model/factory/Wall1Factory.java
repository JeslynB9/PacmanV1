package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.entity.dynamic.physics.BoundingBox;
import javafx.scene.image.Image;

public class Wall1Factory implements EntityFactory {
    @Override
    public Renderable createEntity(char type, int x, int y) {
        Image wallImage = new Image(getClass().getResourceAsStream("/maze/walls/horizontal.png"));
        BoundingBox boundingBox = new BoundingBoxImpl(new Vector2D(x * 16, y * 16), wallImage.getHeight(), wallImage.getWidth());
        return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage);
    }
}

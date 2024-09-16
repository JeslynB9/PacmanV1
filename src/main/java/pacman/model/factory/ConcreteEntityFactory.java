package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
import pacman.model.entity.staticentity.StaticEntityImpl;
import pacman.model.entity.staticentity.Wall;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.staticentity.collectable.Pellet;
import javafx.scene.image.Image;
import pacman.model.entity.dynamic.physics.BoundingBox;

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


        switch (type) {
            case '1': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage1);
            case '2': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage2);
            case '3': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage3);
            case '4': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage4);
            case '5': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage5);
            case '6': return new StaticEntityImpl(boundingBox, Renderable.Layer.FOREGROUND, wallImage6);
//            case 'p': return new Pacman(x, y);
//            case 'g': return new GhostImpl(x, y);
            case '7': return new Pellet(boundingBox, Renderable.Layer.FOREGROUND, pellet, 100);
            default: return null;
        }
    }
}

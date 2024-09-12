package pacman.model.factory;

import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.ghost.GhostImpl;
import pacman.model.entity.dynamic.physics.BoundingBoxImpl;
import pacman.model.entity.dynamic.physics.Vector2D;
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
        Image wallImage = new Image("path/to/wall_image.png");

        switch (type) {
            case '1': return new Wall(boundingBox, Renderable.Layer.FOREGROUND, wallImage, "horizontal");
            case '2': return new Wall(boundingBox, Renderable.Layer.FOREGROUND, wallImage, "vertical");
            case '3': return new Wall(boundingBox, Renderable.Layer.FOREGROUND, wallImage, "up-left");
            case '4': return new Wall(boundingBox, Renderable.Layer.FOREGROUND, wallImage, "up-right");
            case '5': return new Wall(boundingBox, Renderable.Layer.FOREGROUND, wallImage, "down-left");
            case '6': return new Wall(boundingBox, Renderable.Layer.FOREGROUND, wallImage, "down-right");
//            case 'p': return new Pacman(x, y);
//            case 'g': return new GhostImpl(x, y);
            case '7': return new Pellet(boundingBox, Renderable.Layer.FOREGROUND, wallImage, 100);
            default: return null;
        }
    }
}

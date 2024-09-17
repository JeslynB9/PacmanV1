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
        EntityFactory factory;

        switch (type) {
            case '1': factory = new Wall1Factory(); break;
            case '2': factory = new Wall2Factory(); break;
            case '3': factory = new Wall3Factory(); break;
            case '4': factory = new Wall4Factory(); break;
            case '5': factory = new Wall5Factory(); break;
            case '6': factory = new Wall6Factory(); break;
            case '7': factory = new PelletFactory(); break;
            case 'p': factory = new PacmanFactory(); break;
            case 'g': factory = new GhostFactory(); break;

            default: return null;
        }

        Renderable entity = factory.createEntity(type, x, y);
        return entity;
    }

}

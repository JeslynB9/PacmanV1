package pacman.model.factory;

import pacman.model.entity.Renderable;

public interface EntityFactory {
    Renderable createEntity(char type, int x, int y);
}

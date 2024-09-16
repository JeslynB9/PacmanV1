package pacman.model.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import pacman.model.entity.Renderable;
import pacman.model.factory.ConcreteEntityFactory;
import pacman.model.factory.EntityFactory;

/**
 * Responsible for creating renderables and storing them in the Maze
 */
public class MazeCreator {

    private final String fileName;
    private EntityFactory entityFactory;
    public static final int RESIZING_FACTOR = 16;

    public MazeCreator(String fileName, EntityFactory entityFactory){
        this.fileName = fileName;
        this.entityFactory = entityFactory;
    }

    public Maze createMaze(){
        File f = new File(this.fileName);
        Maze maze = new Maze();

        try {
            Scanner scanner = new Scanner(f);

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                char[] row = line.toCharArray();

                // Parse the map and use the factory to create entities
                for (int y = 0; y < row.length; y++) { // Corrected row length
                    for (int x = 0; x < row.length; x++) {
                        char type = row[x]; // Access the character from the row
                        Renderable entity = entityFactory.createEntity(type, x, y); // Use the factory
                        if (entity != null) {
                            maze.addRenderable(entity, type, x, y);
                        }
                    }
                }
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No maze file was found.");
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error");
            System.exit(0);
        }


        return maze;
    }
}

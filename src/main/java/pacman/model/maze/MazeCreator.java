package pacman.model.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import pacman.model.entity.Renderable;
import pacman.model.factory.EntityFactory;

import static java.lang.System.exit;

/**
 * Responsible for creating renderables and storing it in the Maze
 */
public class MazeCreator {

    private final String fileName;
    public static final int RESIZING_FACTOR = 16;
    private final EntityFactory entityFactory;

    public MazeCreator(String fileName, EntityFactory entityFactory){

        this.fileName = fileName;
        this.entityFactory = entityFactory;
    }


    public Maze createMaze(){
        File f = new File(this.fileName);
        Maze maze = new Maze();

        try {
            Scanner scanner = new Scanner(f);


            while (scanner.hasNextLine()){

                String line = scanner.nextLine();
                char[] row = line.toCharArray();

                // Parse the map and use the factory to create entities
                for (int y = 0; y < map.length; y++) {
                    for (int x = 0; x < map[y].length; x++) {
                        char type = map[y][x];
                        Renderable entity = entityFactory.createEntity(type, x, y);
                        if (entity != null) {
                            maze.addRenderable(entity, type, x, y);
                        }
                    }
                }

                y += 1;
            }

            scanner.close();
        }
        catch (FileNotFoundException e){
            System.out.println("No maze file was found.");
            exit(0);
        } catch (Exception e){
            System.out.println("Error");
            exit(0);
        }

        return maze;
    }
}

package pacman.model.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import pacman.model.entity.Renderable;
import pacman.model.factory.EntityFactory;

/**
 * Responsible for creating renderables and storing them in the Maze.
 */
public class MazeCreator {

    private final String fileName;
    private EntityFactory entityFactory;
    public static final int RESIZING_FACTOR = 16;

    /**
     * Constructor to initialize MazeCreator with file name and entity factory.
     *
     * @param fileName      The name of the file containing maze data.
     * @param entityFactory The factory to create entities for the maze.
     */
    public MazeCreator(String fileName, EntityFactory entityFactory) {
        this.fileName = fileName;
        this.entityFactory = entityFactory;
        if (this.entityFactory == null) {
            System.err.println("Warning: EntityFactory is null!");
        }
    }

    /**
     * Creates a Maze by reading data from the file and using the entity factory
     * to generate entities.
     *
     * @return The created Maze.
     */
    public Maze createMaze() {
        if (this.entityFactory == null) {
            throw new IllegalStateException("EntityFactory is not initialized.");
        }

        File f = new File(this.fileName);
        Maze maze = new Maze();

        try {
            Scanner scanner = new Scanner(f);

            int rowIndex = 0;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                char[] row = line.toCharArray();

                for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                    char type = row[columnIndex]; // Access the character from the row
                    Renderable entity = entityFactory.createEntity(type, columnIndex, rowIndex); // Use columnIndex for x and rowIndex for y
                    if (entity != null) {
                        maze.addRenderable(entity, type, columnIndex, rowIndex); // Use columnIndex for x and rowIndex for y
                    }
                }

                rowIndex++;
            }

            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("No maze file was found.");
            e.printStackTrace(); // Print stack trace for debugging
            System.exit(0);
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            System.exit(0);
        }

        return maze;
    }
}

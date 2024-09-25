package pacman.model.maze;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
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
        List<GhostPosition> ghostPositions = new ArrayList<>(); // Store ghost positions for deferred creation

        try (Scanner scanner = new Scanner(f)) {
            // Locate and create Pacman
            int rowIndex = 0;
            boolean pacmanCreated = false;
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                char[] row = line.toCharArray();

                for (int columnIndex = 0; columnIndex < row.length; columnIndex++) {
                    char type = row[columnIndex];

                    if (type == 'p') { // 'p' for Pacman
                        Renderable pacman = entityFactory.createEntity(type, columnIndex, rowIndex);
                        if (pacman != null) {
                            maze.addRenderable(pacman, type, columnIndex, rowIndex);
                            pacmanCreated = true;
                        }
                    } else if (type == 'g') {
                        // Defer ghost creation by saving its position
                        ghostPositions.add(new GhostPosition(type, columnIndex, rowIndex));
                    } else {
                        // Create all other entities except Pacman and Ghosts
                        Renderable entity = entityFactory.createEntity(type, columnIndex, rowIndex);
                        if (entity != null) {
                            maze.addRenderable(entity, type, columnIndex, rowIndex);
                        }
                    }
                }

                rowIndex++;
            }

            if (!pacmanCreated) {
                throw new IllegalStateException("Pacman not found in the maze file.");
            }

            // Create ghosts after Pacman is created
            for (GhostPosition ghostPos : ghostPositions) {
                Renderable ghost = entityFactory.createEntity(ghostPos.type, ghostPos.x, ghostPos.y);
                if (ghost != null) {
                    maze.addRenderable(ghost, ghostPos.type, ghostPos.x, ghostPos.y);
                }
            }

        } catch (FileNotFoundException e) {
            System.err.println("Error: Maze file '" + this.fileName + "' not found.");
            e.printStackTrace(); // Log the full error for debugging
            throw new RuntimeException("Maze file not found.", e);
        } catch (Exception e) {
            System.err.println("Error processing maze file: " + e.getMessage());
            e.printStackTrace(); // Print stack trace for debugging
            throw new RuntimeException("Error processing maze file.", e);
        }

        return maze;
    }

    // Helper class to store ghost positions for deferred creation
    private static class GhostPosition {
        char type;
        int x;
        int y;

        public GhostPosition(char type, int x, int y) {
            this.type = type;
            this.x = x;
            this.y = y;
        }
    }
}

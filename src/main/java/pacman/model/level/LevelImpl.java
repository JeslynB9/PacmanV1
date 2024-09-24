package pacman.model.level;

import org.json.simple.JSONObject;
import pacman.ConfigurationParseException;
import pacman.model.engine.GameModel;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.DynamicEntity;
import pacman.model.entity.dynamic.ghost.Ghost;
import pacman.model.entity.dynamic.ghost.GhostMode;
import pacman.model.entity.dynamic.physics.PhysicsEngine;
import pacman.model.entity.dynamic.player.Controllable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.entity.staticentity.StaticEntity;
import pacman.model.entity.staticentity.collectable.Collectable;
import pacman.model.factory.EntityFactory;
import pacman.model.maze.Maze;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Concrete implement of Pac-Man level
 */
public class LevelImpl implements Level {
    private static final int READY_SCREEN_DURATION = 100; // 100 frames for the READY screen
    private final Maze maze;
    private List<Renderable> renderables;
    private Controllable player;
    private List<Ghost> ghosts;
    private int tickCount;
    private int readyFrameCount;
    private static boolean isReadyScreenActive = true;
    private static boolean isGameOver = false;
    private Map<GhostMode, Integer> modeLengths;
    private int numLives;
    private List<Renderable> collectables;
    private GhostMode currentGhostMode;
    private EntityFactory entityFactory;
    GameModel gameModel = GameModel.getInstance();
    private static LevelImpl currentInstance;

    public LevelImpl(JSONObject levelConfiguration, Maze maze, EntityFactory entityFactory) {
        this.entityFactory = entityFactory;
        this.renderables = new ArrayList<>();
        this.maze = maze;
        this.tickCount = 0;
        this.modeLengths = new HashMap<>();
        this.currentGhostMode = GhostMode.SCATTER;


        initLevel(new LevelConfigurationReader(levelConfiguration));
    }

    public static boolean isReadyScreenActive() {
        return isReadyScreenActive;
    }

    public static void setReadyScreenActive(boolean b) {
        isReadyScreenActive = b;
    }

    public static boolean isGameOver() {
        return isGameOver;
    }

    public static void setGameOver(boolean b) {
        isGameOver = b;
    }

    private void initLevel(LevelConfigurationReader levelConfigurationReader) {
        // Fetch all renderables for the level
        this.renderables = maze.getRenderables();

        Object controllableEntity = maze.getControllable();
        // Set up player
        if (maze.getControllable() == null || !(maze.getControllable() instanceof Controllable)) {
            throw new ConfigurationParseException("Player entity is not controllable");
        }
        this.player = (Controllable) controllableEntity;
        this.player.setSpeed(levelConfigurationReader.getPlayerSpeed());
        setNumLives(maze.getNumLives());

        // Set up ghosts
        this.ghosts = maze.getGhosts().stream()
                .map(element -> (Ghost) element)
                .collect(Collectors.toList());
        Map<GhostMode, Double> ghostSpeeds = levelConfigurationReader.getGhostSpeeds();

        for (Ghost ghost : this.ghosts) {
            ghost.setSpeeds(ghostSpeeds);
            ghost.setGhostMode(this.currentGhostMode);
        }
        this.modeLengths = levelConfigurationReader.getGhostModeLengths();

        // Set up collectables
        this.collectables = new ArrayList<>(maze.getPellets());

    }

    @Override
    public List<Renderable> getRenderables() {
        return this.renderables;
    }

    private List<DynamicEntity> getDynamicEntities() {
        return renderables.stream().filter(e -> e instanceof DynamicEntity).map(e -> (DynamicEntity) e).collect(
                Collectors.toList());
    }

    private List<StaticEntity> getStaticEntities() {
        return renderables.stream().filter(e -> e instanceof StaticEntity).map(e -> (StaticEntity) e).collect(
                Collectors.toList());
    }

    @Override
    public void tick() {
        // Check if the READY screen is still active
        if (isReadyScreenActive) {
            readyFrameCount++;
            System.out.println("ready frame count: " + readyFrameCount);

            // Display the READY screen for 100 frames
            if (readyFrameCount < READY_SCREEN_DURATION) {
                gameModel.notifyReadyScreen();  // Notify the observers to keep showing READY screen
                return;  // Pause the game logic, return early
            }
        }

        readyFrameCount = 0;
        setReadyScreenActive(false);
        gameModel.notifyReadyScreen();

        // Update ghosts based on the ghost mode (SCATTER/CHASE)
        if (tickCount == modeLengths.get(currentGhostMode)) {
            // Change ghost mode logic (e.g., switch between SCATTER and CHASE)
            this.currentGhostMode = GhostMode.getNextGhostMode(currentGhostMode);
            for (Ghost ghost : this.ghosts) {
                ghost.setGhostMode(this.currentGhostMode);
            }
            tickCount = 0;  // Reset tick count after mode switch
        }

        // Update Pac-Man sprite every few ticks
        if (tickCount % Pacman.PACMAN_IMAGE_SWAP_TICK_COUNT == 0) {
            this.player.switchImage();
        }

        // Update all dynamic entities (Pac-Man, Ghosts, etc.)
        List<DynamicEntity> dynamicEntities = getDynamicEntities();
        for (DynamicEntity dynamicEntity : dynamicEntities) {
            maze.updatePossibleDirections(dynamicEntity);  // Check valid directions
            dynamicEntity.update();  // Update entity's state (movement, etc.)
        }

        // Handle collisions between dynamic entities (Pac-Man and ghosts)
        for (int i = 0; i < dynamicEntities.size(); ++i) {
            DynamicEntity dynamicEntityA = dynamicEntities.get(i);
            for (int j = i + 1; j < dynamicEntities.size(); ++j) {
                DynamicEntity dynamicEntityB = dynamicEntities.get(j);
                if (dynamicEntityA.collidesWith(dynamicEntityB)) {
                    dynamicEntityA.collideWith(this, dynamicEntityB);
                    dynamicEntityB.collideWith(this, dynamicEntityA);
                }
            }

            // Handle collisions between dynamic entities and static entities (walls, pellets)
            for (StaticEntity staticEntity : getStaticEntities()) {
                if (dynamicEntityA.collidesWith(staticEntity)) {
                    dynamicEntityA.collideWith(this, staticEntity);
                    PhysicsEngine.resolveCollision(dynamicEntityA, staticEntity);
                }
            }
        }

        tickCount++;  // Increment the game tick counter
    }


    @Override
    public boolean isPlayer(Renderable renderable) {
        return renderable == this.player;
    }

    @Override
    public boolean isCollectable(Renderable renderable) {
        return maze.getPellets().contains(renderable) && ((Collectable) renderable).isCollectable();
    }

    @Override
    public void moveLeft() {
        player.left();
    }

    @Override
    public void moveRight() {
        player.right();
    }

    @Override
    public void moveUp() {
        player.up();
    }

    @Override
    public void moveDown() {
        player.down();
    }

    @Override
    public boolean isLevelFinished() {
        return collectables.isEmpty();
    }

    @Override
    public int getNumLives() {
        return this.numLives;
    }

    private void setNumLives(int numLives) {
        this.numLives = numLives;
    }

    @Override
    public void handleLoseLife() {
        if (numLives > 0) {
            numLives--; // Decrease the number of lives

            System.out.println("Life Lost: " + numLives + " lives remaining");

            // Notify the LifeView to update the displayed lives
            gameModel.loseLife();
            setReadyScreenActive(true);
            gameModel.notifyReadyScreen();
            // Reset all the entities
            for (Ghost ghost : ghosts) {
                ghost.reset();
            }

            player.reset();


            // Check if all lives are lost
            if (numLives == 0) {
                handleGameEnd();
            }
        }
    }

    @Override
    public void handleGameEnd() {
        if (numLives == 0) {
            setGameOver(true);
            destroyPacman(); // Destroy Pac-Man
            destroyGhosts(); // Destroy ghosts
            gameModel.notifyGameOverScreen();

            setReadyScreenActive(false);
            gameModel.notifyReadyScreen();

            // Create a timer to exit the game after 5 seconds
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    System.exit(0); // Exit the game
                }
            }, 5000); // 5000 milliseconds = 5 seconds
        }
    }

    @Override
    public void collect(Collectable collectable) {
        if (collectables.contains(collectable)) {
            collectables.remove(collectable);
//            renderables.remove(collectable);
            System.out.println("Collected: " + collectable);
        }
    }

    public void destroyPacman() {
        if (player != null) {
            // Remove Pac-Man from the renderables
            renderables.remove(player);
            // Optionally, set player to null
            player = null;
        }
    }

    public void destroyGhosts() {
        for (Ghost ghost : ghosts) {
            if (ghost != null) {
                // Remove each ghost from the renderables
                renderables.remove(ghost);
                // Optionally, set ghost to null
                ghost = null;
            }
        }
    }

}

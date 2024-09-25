package pacman.model.engine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import pacman.model.entity.Renderable;
import pacman.model.factory.ConcreteEntityFactory;
import pacman.model.factory.EntityFactory;
import pacman.model.level.Level;
import pacman.model.level.LevelImpl;
import pacman.model.maze.Maze;
import pacman.model.maze.MazeCreator;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Implementation of GameEngine - responsible for coordinating the Pac-Man model
 */
public class GameEngineImpl implements GameEngine {

    private Level currentLevel;
    private int numLevels;
    private int currentLevelNo;
    private int numLives;
    private Maze maze;
    private JSONArray levelConfigs;
    private ConcreteEntityFactory concreteEntityFactory;
    private GameModel gameModel = GameModel.getInstance();
    private boolean gameWon = false;
    private int readyFrameCounter = 0;
    private static final int READY_SCREEN_DURATION = 100;

    public GameEngineImpl(String configPath) {
        this.currentLevelNo = 0;
        this.concreteEntityFactory = new ConcreteEntityFactory();
        GameConfigurationReader gameConfigurationReader = GameConfigurationReader.getInstance(configPath);
        init(gameConfigurationReader);
        gameModel = GameModel.getInstance();
    }

    private void init(GameConfigurationReader gameConfigurationReader) {
        // Set up map
        String mapFile = gameConfigurationReader.getMapFile();
        MazeCreator mazeCreator = new MazeCreator(mapFile, concreteEntityFactory);
        this.maze = mazeCreator.createMaze();
        this.numLives = gameConfigurationReader.getNumLives();
        gameModel.setNumLives(numLives);
        this.maze.setNumLives(gameConfigurationReader.getNumLives());


        // Get level configurations
        this.levelConfigs = gameConfigurationReader.getLevelConfigs();
        this.numLevels = levelConfigs.size();
        if (levelConfigs.isEmpty()) {
            System.exit(0);
        }
    }

    @Override
    public List<Renderable> getRenderables() {
        return this.currentLevel.getRenderables();
    }

    @Override
    public void moveUp() {
        currentLevel.moveUp();
    }

    @Override
    public void moveDown() {
        currentLevel.moveDown();
    }

    @Override
    public void moveLeft() {
        currentLevel.moveLeft();
    }

    @Override
    public void moveRight() {
        currentLevel.moveRight();
    }

    @Override
    public void startGame() {
        startLevel();
    }

    private void startLevel() {
        JSONObject levelConfig = (JSONObject) levelConfigs.get(currentLevelNo);
        // reset renderables to starting state
        maze.reset();
        this.currentLevel = new LevelImpl(levelConfig, maze, concreteEntityFactory);
        gameModel.notifyReadyScreen();
    }

    @Override
    public void tick() {
        if (currentLevelNo != 0) {
            // If the game is in the "ready" state, show the "Ready" screen for 100 frames
            if (readyFrameCounter < READY_SCREEN_DURATION) {
                readyFrameCounter++;  // Decrement the counter
                System.out.println("ready frame count: " + readyFrameCounter);
                if (currentLevel instanceof LevelImpl) {
                    LevelImpl level = (LevelImpl) currentLevel;
                    level.setReadyScreenActive(true);
                    gameModel.notifyReadyScreen();  // Display the "Ready" screen
                    if (readyFrameCounter == READY_SCREEN_DURATION) {
                        level.setReadyScreenActive(false);
                        gameModel.notifyReadyScreen();
                    }
                }
                return;  // Skip the rest of the game logic until the counter reaches 0
            }
        }

        // Normal game tick logic starts here after "Ready" screen is done
        currentLevel.tick();  // Progress the level normally (e.g., move entities, check collisions)

        if (currentLevel.isLevelFinished()) {
            System.out.println("Level finished. Moving to next level...");
            // Move to the next level if available
            if (currentLevelNo + 1 < numLevels) {
                currentLevelNo++;
                System.out.println("Current Level No: " + currentLevelNo);
                gameModel.setNumLives(numLives);
                startLevel();  // Start the next level
                setGameWon(false);
                gameModel.notifyYouWinScreen();
                readyFrameCounter = 0;  // Reset counter for the next level's "Ready" screen
                gameModel.notifyReadyScreen();  // Notify to display the "Ready" screen
            } else {
                // All levels completed, trigger a "game won" event
                setGameWon(true);
                gameModel.notifyYouWinScreen();
                if (currentLevel instanceof LevelImpl) {
                    LevelImpl level = (LevelImpl) currentLevel;
                    level.destroyPacman(); // Destroy Pac-Man
                    level.destroyGhosts(); // Destroy ghosts
                    gameModel.notifyYouWinScreen();
                }

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
    }


    public boolean isGameWon() {
        return gameWon;
    }

    public void setGameWon(boolean b) {
        gameWon = b;
    }

    @Override
    public GameModel getGameModel() {return gameModel;}


}


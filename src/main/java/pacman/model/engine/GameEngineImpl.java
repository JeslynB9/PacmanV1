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

/**
 * Implementation of GameEngine - responsible for coordinating the Pac-Man model
 */
public class GameEngineImpl implements GameEngine {

    private Level currentLevel;
    private int numLevels;
    private final int currentLevelNo;
    private Maze maze;
    private JSONArray levelConfigs;
    private ConcreteEntityFactory concreteEntityFactory;
    private GameModel gameModel = GameModel.getInstance();

    public GameEngineImpl(String configPath) {
        this.currentLevelNo = 0;
        this.concreteEntityFactory = new ConcreteEntityFactory();
        init(new GameConfigurationReader(configPath));
        gameModel = GameModel.getInstance();
    }

    private void init(GameConfigurationReader gameConfigurationReader) {
        // Set up map
        String mapFile = gameConfigurationReader.getMapFile();
        MazeCreator mazeCreator = new MazeCreator(mapFile, concreteEntityFactory);
        this.maze = mazeCreator.createMaze();
        int numLives = gameConfigurationReader.getNumLives();
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
        currentLevel.tick();
    }

    @Override
    public GameModel getGameModel() {return gameModel;}


}


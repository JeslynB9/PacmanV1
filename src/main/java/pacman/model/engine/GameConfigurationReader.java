package pacman.model.engine;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Helper class to read Game Configuration from JSONObject using Singleton pattern.
 */
public class GameConfigurationReader {

    // Static variable to hold the single instance of GameConfigurationReader
    private static GameConfigurationReader instance;

    // Game configuration JSON object
    private JSONObject gameConfig;

    // Private constructor to prevent instantiation from outside
    private GameConfigurationReader(String configPath) {
        JSONParser parser = new JSONParser();

        try {
            this.gameConfig = (JSONObject) parser.parse(new FileReader(configPath));
        } catch (FileNotFoundException e) {
            System.out.println("Config file not found");
            System.exit(0);
        } catch (IOException e) {
            System.out.println("Error reading config file");
            System.exit(0);
        } catch (ParseException e) {
            System.out.println("Error parsing config file");
            System.exit(0);
        }
    }

    // Provide access to the single instance
    public static GameConfigurationReader getInstance(String configPath) {
        if (instance == null) {
            instance = new GameConfigurationReader(configPath);
        }
        return instance;
    }

    /**
     * Gets the path of the map file.
     * @return Path of the map file.
     */
    public String getMapFile() {
        return (String) gameConfig.get("map");
    }

    /**
     * Gets the number of lives of the player.
     * @return Number of lives of the player.
     */
    public int getNumLives() {
        return ((Number) gameConfig.get("numLives")).intValue();
    }

    /**
     * Gets JSONArray of level configurations.
     * @return JSONArray of level configurations.
     */
    public JSONArray getLevelConfigs() {
        return (JSONArray) gameConfig.get("levels");
    }
}

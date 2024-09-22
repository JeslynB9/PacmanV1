package pacman;

import javafx.application.Application;
import javafx.stage.Stage;
import pacman.model.engine.GameEngine;
import pacman.model.engine.GameEngineImpl;
import pacman.model.factory.ConcreteEntityFactory;
import pacman.model.factory.EntityFactory;
import pacman.view.GameWindow;
import pacman.view.ScoreView;
import pacman.view.keyboard.KeyboardInputHandler;

public class App extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("javafx.runtime.version: " + System.getProperty("javafx.runtime.version"));
        EntityFactory entityFactory = new ConcreteEntityFactory();
        GameEngine model = new GameEngineImpl("src/main/resources/config.json");
        ScoreView scoreView = new ScoreView(); // Create ScoreView instance
        GameWindow window = new GameWindow(model, 448, 576);


        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler();

        primaryStage.setTitle("Pac-Man");
        primaryStage.setScene(window.getScene());
        primaryStage.show();

        window.run();
    }
}
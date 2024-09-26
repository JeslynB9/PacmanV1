package pacman.view.observer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pacman.model.engine.GameEngine;
import pacman.model.engine.GameEngineImpl;
import pacman.model.level.LevelImpl;
import pacman.model.observer.Observer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class YouWinView implements Observer {
    private Label youWinLabel;
    private VBox layout;
    private LevelImpl levelImpl;
    private GameEngine gameEngine;


    public YouWinView(GameEngine gameEngine) {
        this.gameEngine = gameEngine;
        try {
            FileInputStream fontFile = new FileInputStream("src/main/resources/maze/PressStart2P-Regular.ttf");
            Font customFont = Font.loadFont(fontFile, 16);


            youWinLabel = new Label("YOU WIN!");
            youWinLabel.setStyle("-fx-text-fill: white;");

            if (customFont != null) {
                youWinLabel.setFont(customFont);
            } else {
                System.out.println("Failed to load custom font.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        layout = new VBox(youWinLabel);
        layout.setPadding(new Insets(320, 0, 0, 160));
        layout.setAlignment(Pos.CENTER);  // Center alignment for VBox
    }

    @Override
    public void update(int score, int lives) {
        // No need to implement
    }

    @Override
    public void updateReadyScreen(boolean readyScreenActive) {
        // No need to implement
    }

    @Override
    public void updateGameOverScreen(boolean isGameOver) {
        // No need to implement

    }

    @Override
    public void updateYouWinScreen() {
        if (gameEngine instanceof GameEngineImpl) {
            GameEngineImpl gameEngineImpl = (GameEngineImpl) gameEngine;

            if (gameEngineImpl.isGameWon()) {
                System.out.println("YOU WIN screen is active");
                Platform.runLater(() -> layout.getChildren().setAll(youWinLabel));
            } else {
                layout.getChildren().remove(youWinLabel);
                System.out.println("YOU WIN screen is no longer active.");
            }
        } else {
            // Handle case if the gameEngine is not of type GameEngineImpl
            System.out.println("GameEngine is not an instance of GameEngineImpl.");
        }

    }

    public VBox getView() {
        return layout;
    }

}

package pacman.view.observer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pacman.model.level.LevelImpl;
import pacman.model.observer.Observer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GameOverView implements Observer {
    private static GameOverView instance;
    private Label gameOverLabel;
    private VBox layout;
    private LevelImpl levelImpl;
    public GameOverView() {
        try {
            FileInputStream fontFile = new FileInputStream("src/main/resources/maze/PressStart2P-Regular.ttf");
            Font customFont = Font.loadFont(fontFile, 16);


            gameOverLabel = new Label("GAME OVER");
            gameOverLabel.setStyle("-fx-text-fill: red;");

            if (customFont != null) {
                gameOverLabel.setFont(customFont);
            } else {
                System.out.println("Failed to load custom font.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        layout = new VBox(gameOverLabel);
        layout.setPadding(new Insets(320, 0, 0, 155));
        layout.setAlignment(Pos.CENTER);  // Center alignment for VBox
    }

    @Override
    public void update(int score, int lives) {
        // No need to implement for score and lives
    }

    @Override
    public void updateReadyScreen(boolean readyScreenActive) {
        // No need to implement
    }

    @Override
    public void updateGameOverScreen(boolean isGameOver) {
        // Show the GAME OVER screen
        if (levelImpl.isGameOver()) {
            System.out.println("GAME OVER screen is active");
            Platform.runLater(() -> layout.getChildren().setAll(gameOverLabel));
        } else {
            layout.getChildren().remove(gameOverLabel);
            System.out.println("GAME OVER screen is no longer active.");
        }

    }

    @Override
    public void updateYouWinScreen() {
        // No need to implement
    }


    public VBox getView() {
        return layout;
    }

    public static GameOverView getInstance() {
        if (instance == null) {
            instance = new GameOverView();
        }
        return instance;
    }
}

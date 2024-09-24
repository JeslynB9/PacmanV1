package pacman.view.observer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pacman.model.observer.Observer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ScoreView implements Observer {
    private int score;
    private Label scoreLabel;

    public ScoreView() {
        try {
            FileInputStream fontFile = new FileInputStream("src/main/resources/maze/PressStart2P-Regular.ttf");
            Font customFont = Font.loadFont(fontFile, 16);

            scoreLabel = new Label("0");

            if (customFont != null) {
                scoreLabel.setFont(customFont);
            } else {
                System.out.println("Failed to load custom font.");
            }

            scoreLabel.setStyle("-fx-text-fill: white;");

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int score, int numLives) {
        this.score = score;
        System.out.println("Updating score view: " + score);
        Platform.runLater(() -> scoreLabel.setText(String.valueOf(score)));
    }

    @Override
    public void updateReadyScreen(boolean readyScreenActive) {

    }

    @Override
    public void updateGameOverScreen(boolean isGameOver) {

    }

    @Override
    public void updateYouWinScreen() {
        // No need to implement
    }



    public VBox getView() {
        VBox layout = new VBox(scoreLabel);
        VBox.setMargin(scoreLabel, new Insets(20, 0, 0, 10));
        return layout;
    }
}

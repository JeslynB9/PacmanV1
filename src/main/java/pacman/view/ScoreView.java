package pacman.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import pacman.model.observer.Observer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class ScoreView implements Observer {
    private static ScoreView instance;
    private int score;
    private Label scoreLabel; // Label to display the score

    public ScoreView() {
        try {
            // Load the custom font from the specified file path
            FileInputStream fontFile = new FileInputStream("src/main/resources/maze/PressStart2P-Regular.ttf");
            Font customFont = Font.loadFont(fontFile, 16); // Load the font with size 16

            // Initialize the label
            scoreLabel = new Label("0");

            // Set the custom font to the label (check if the font loaded correctly)
            if (customFont != null) {
                scoreLabel.setFont(customFont);
            } else {
                System.out.println("Failed to load custom font.");
            }

            scoreLabel.setStyle("-fx-text-fill: white;"); // Text color set to white

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Font file not found.");
        }
    }

    @Override
    public void update(int score, int numLives) {
        this.score = score;
        scoreLabel.setText(String.valueOf(score)); // Update the label text
        System.out.println(score);
    }

    public static ScoreView getInstance() {
        if (instance == null) {
            instance = new ScoreView();
        }
        return instance;
    }

    public VBox getView() {
        VBox layout = new VBox(scoreLabel); // Create a layout to hold the label
        VBox.setMargin(scoreLabel, new Insets(20, 0, 0, 10));
        return layout; // Return the layout for inclusion in the main scene
    }

    public void incrementScore(int points) {
        score += points;
        Platform.runLater(() -> update(score, 0));
    }
}

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

public class ReadyView implements Observer {
    private static ReadyView instance;
    private Label readyLabel;
    private VBox layout;
    private LevelImpl levelImpl;
    public ReadyView() {
        try {
            FileInputStream fontFile = new FileInputStream("src/main/resources/maze/PressStart2P-Regular.ttf");
            Font customFont = Font.loadFont(fontFile, 16);


            readyLabel = new Label("READY!");
            readyLabel.setStyle("-fx-text-fill: yellow;");

            if (customFont != null) {
                readyLabel.setFont(customFont);
            } else {
                System.out.println("Failed to load custom font.");
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        layout = new VBox(readyLabel);
        layout.setPadding(new Insets(320, 0, 0, 180));
        layout.setAlignment(Pos.CENTER);  // Center alignment for VBox
    }

    @Override
    public void update(int score, int lives) {
        // No need to implement for score and lives
    }

    @Override
    public void updateGameOverScreen(boolean isGameOver) {
        // No need to implement
    }

    @Override
    public void updateYouWinScreen() {
        // No need to implement
    }

    @Override
    public void updateReadyScreen(boolean readyScreenActive) {
        // Show the READY screen
        if (levelImpl.isReadyScreenActive()) {
            Platform.runLater(() -> layout.getChildren().setAll(readyLabel));
        } else {
            layout.getChildren().remove(readyLabel);
//            System.out.println("READY screen is no longer active.");

        }
    }

    public VBox getView() {
        return layout;
    }

}
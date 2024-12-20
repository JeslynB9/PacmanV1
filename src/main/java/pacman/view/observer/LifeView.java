package pacman.view.observer;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import pacman.model.observer.Observer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class LifeView implements Observer {
    private int numLives;
    private List<ImageView> lifeIcons;
    private HBox lifeBox;

    public LifeView() {
        numLives = 3;
        lifeIcons = new ArrayList<>();
        lifeBox = new HBox(10);

        initializeLifeIcons();
    }

    private void initializeLifeIcons() {
        try {
            Image pacmanSprite = new Image(new FileInputStream("src/main/resources/maze/pacman/playerRight.png"));

            for (int i = 0; i < numLives; i++) {
                ImageView lifeIcon = new ImageView(pacmanSprite);
                lifeIcon.setFitHeight(pacmanSprite.getHeight());
                lifeIcon.setFitWidth(pacmanSprite.getWidth());
                lifeIcons.add(lifeIcon);
            }
            lifeBox.getChildren().addAll(lifeIcons);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(int score, int numLives) {
        this.numLives = numLives;
        Platform.runLater(() -> updateLives(numLives));
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
        // No need to implement
    }


    private void updateLives(int numLives) {
        lifeBox.getChildren().clear();

        for (int i = 0; i < numLives; i++) {
            lifeBox.getChildren().add(lifeIcons.get(i));
        }
    }

    public HBox getView() {
        lifeBox.setPadding(new Insets(545, 0, 0, 10));
        return lifeBox;
    }
}

package pacman.view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import pacman.model.observer.Observer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

public class LifeView implements Observer {
    private static LifeView instance;
    private int numLives;
    private List<ImageView> lifeIcons; // List of ImageViews for the lives
    private HBox lifeBox; // HBox to display the lives

    public LifeView() {
        numLives = 3; // Pac-Man starts with 3 lives
        lifeIcons = new ArrayList<>();
        lifeBox = new HBox(10); // Create an HBox with 10px spacing between icons

        initializeLifeIcons();
    }

    private void initializeLifeIcons() {
        try {
            // Load the Pac-Man sprite
            Image pacmanSprite = new Image(new FileInputStream("src/main/resources/maze/pacman/playerRight.png"));

            // Initialize life icons with 3 Pac-Man sprites
            for (int i = 0; i < numLives; i++) {
                ImageView lifeIcon = new ImageView(pacmanSprite);
                lifeIcon.setFitHeight(pacmanSprite.getHeight()); // Set the height of the sprite
                lifeIcon.setFitWidth(pacmanSprite.getWidth());  // Set the width of the sprite
                lifeIcons.add(lifeIcon);   // Add to the list of life icons
            }

            // Add all life icons to the HBox
            lifeBox.getChildren().addAll(lifeIcons);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.out.println("Pac-Man sprite file not found.");
        }
    }


    @Override
    public void update(int score, int numLives) {
        this.numLives = numLives;

        // Update the life display by removing icons as lives decrease
        Platform.runLater(() -> updateLives(numLives));
    }

    private void updateLives(int numLives) {
        // Clear the HBox and add only the number of lives left
        lifeBox.getChildren().clear();

        for (int i = 0; i < numLives; i++) {
            lifeBox.getChildren().add(lifeIcons.get(i));
        }
    }

    public static LifeView getInstance() {
        if (instance == null) {
            instance = new LifeView();
        }
        return instance;
    }

//    public VBox getLifeContainer() {
//        VBox lifeContainer = new VBox();
//        lifeContainer.setPadding(new Insets(0, 0, 50, 0)); // Add padding at the bottom (50px)
//
//        // Add the lifeBox to the VBox
//        lifeContainer.getChildren().add(lifeBox);
//
//        return lifeContainer;
//    }


    public HBox getView() {
        lifeBox.setPadding(new Insets(545, 0, 0, 10));
        return lifeBox; // Return the HBox to be included in the main scene
    }

    public void loseLife() {
        if (numLives > 0) {
            numLives--;
            Platform.runLater(() -> updateLives(numLives)); // Update the view on the JavaFX Application thread
        }
    }

    public int getNumLives() {
        return numLives;
    }
}

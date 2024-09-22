package pacman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane; // Keep using Pane
import javafx.util.Duration;
import pacman.model.engine.GameEngine;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.view.background.BackgroundDrawer;
import pacman.view.background.StandardBackgroundDrawer;
import pacman.view.entity.EntityView;
import pacman.view.entity.EntityViewImpl;
import pacman.view.keyboard.KeyboardInputHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for managing the Pac-Man Game View
 */
public class GameWindow {

    public static final File FONT_FILE = new File("src/main/resources/maze/PressStart2P-Regular.ttf");

    private final Scene scene;
    private final Pane pane;
    private final GameEngine model;
    private final List<EntityView> entityViews;
    private Pacman pacman;

    public GameWindow(GameEngine model, int width, int height) {
        this.model = model;
        ScoreView scoreView = ScoreView.getInstance();
        LifeView liveView = LifeView.getInstance();

        pane = new Pane();
        scene = new Scene(pane, width, height);

        entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler();
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);

        BackgroundDrawer backgroundDrawer = new StandardBackgroundDrawer();
        backgroundDrawer.draw(model, pane);

        // Add ScoreView to the Pane (for simplicity)
        pane.getChildren().add(scoreView.getView()); // Add ScoreView to the pane
        pane.getChildren().add(liveView.getView());
    }

    public Scene getScene() {
        return scene;
    }

    public void run() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(34),
                t -> this.draw()));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        model.startGame();
    }

    private void draw() {
        model.tick();

        List<Renderable> entities = model.getRenderables();

        for (EntityView entityView : entityViews) {
            entityView.markForDelete();
        }

        for (Renderable entity : entities) {
            boolean notFound = true;
            for (EntityView view : entityViews) {
                if (view.matchesEntity(entity)) {
                    notFound = false;
                    view.update();
                    break;
                }
            }
            if (notFound) {
                EntityView entityView = new EntityViewImpl(entity);
                entityViews.add(entityView);
                pane.getChildren().add(entityView.getNode());
            }
        }

        for (EntityView entityView : entityViews) {
            if (entityView.isMarkedForDelete()) {
                pane.getChildren().remove(entityView.getNode());
            }
        }

        entityViews.removeIf(EntityView::isMarkedForDelete);
    }
}

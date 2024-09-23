package pacman.view;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.layout.Pane; // Keep using Pane
import javafx.util.Duration;
import pacman.model.engine.GameEngine;
import pacman.model.engine.GameModel;
import pacman.model.entity.Renderable;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.level.Level;
import pacman.model.level.LevelImpl;
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
        ReadyView readyView = ReadyView.getInstance();
        GameOverView gameOverView = GameOverView.getInstance();
        GameModel.getInstance().registerObservers(ScoreView.getInstance());
        GameModel.getInstance().registerObservers(LifeView.getInstance());
        GameModel.getInstance().registerObservers(ReadyView.getInstance());
        GameModel.getInstance().registerObservers(GameOverView.getInstance());


        pane = new Pane();
        scene = new Scene(pane, width, height);

        entityViews = new ArrayList<>();

        KeyboardInputHandler keyboardInputHandler = new KeyboardInputHandler();
        scene.setOnKeyPressed(keyboardInputHandler::handlePressed);

        BackgroundDrawer backgroundDrawer = new StandardBackgroundDrawer();
        backgroundDrawer.draw(model, pane);

        // Add views
        pane.getChildren().add(scoreView.getView());
        pane.getChildren().add(liveView.getView());
        pane.getChildren().add(readyView.getView());
        pane.getChildren().add(gameOverView.getView());
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
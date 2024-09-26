package pacman.model.engine;

import javafx.application.Platform;
import pacman.model.level.LevelImpl;
import pacman.model.observer.Observer;
import pacman.model.observer.Subject;

import java.util.ArrayList;
import java.util.List;

public class GameModel implements Subject {
    private List<Observer> observers;
    private int score;
    private int numLives;
    private static GameModel instance;

    public GameModel() {
        observers = new ArrayList<>();
        score = 0;
        numLives = getNumLives();

        notifyGameOverScreen();
        notifyYouWinScreen();
    }

    public static GameModel getInstance() {
        if (instance == null) {
            instance = new GameModel();
        }
        return instance;
    }

    // Attach observer to the list
    public void registerObservers(Observer observer) {
        System.out.println("Registering observer with GameModel: " + this);
        if (!observers.contains(observer)) {
            observers.add(observer);
            System.out.println("Observer added: " + observer);
        }
        System.out.println("Total observers: " + observers.size());
    }

    // Detach observer from the list
    public void removeObservers(Observer observer) {
        observers.remove(observer);
        System.out.println("Observer removed: " + observer);
        System.out.println("Total observers after removal: " + observers.size());
    }

    // Notify all observers of the state change
    public void notifyObservers() {
        System.out.println("Notifying observers: score = " + score + ", lives = " + numLives);
        if (observers.isEmpty()) {
            System.out.println("No observers to notify!");
        }
        Platform.runLater(() -> {
            for (Observer observer : observers) {
                observer.update(score, numLives);
            }
        });
    }

    public void notifyReadyScreen() {
        Platform.runLater(() -> {
            for (Observer observer : observers) {
                observer.updateReadyScreen(isReadyScreenActive());
            }
        });
    }

    public void notifyGameOverScreen() {
        Platform.runLater(() -> {
            for (Observer observer : observers) {
                observer.updateGameOverScreen(isGameOver());
            }
        });
    }

    public void notifyYouWinScreen() {
        Platform.runLater(() -> {
            for (Observer observer : observers) {
                observer.updateYouWinScreen();
            }
        });
    }

    public boolean isGameOver() {
        return LevelImpl.isGameOver();
    }

    public boolean isReadyScreenActive() {
        return LevelImpl.isReadyScreenActive();
    }

    // The score changes
    public void increaseScore(int points) {
        score += points;
        System.out.println("Score increased: " + score);
        notifyObservers(); // Notify observers about the updated score
    }

    // A life is lost
    public void loseLife() {
        if (numLives > 0) {
            numLives--;
            System.out.println("Lives: " + numLives);
            notifyObservers(); // Notify observers about the updated lives
        }
    }

    public void setNumLives(int numLives) {
        this.numLives = numLives;
        notifyObservers(); // Notify observers about the updated lives
    }


    public int getNumLives() {
        return numLives;
    }
}

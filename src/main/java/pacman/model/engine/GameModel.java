package pacman.model.engine;

import javafx.application.Platform;
import pacman.model.observer.Observer;

import java.util.ArrayList;
import java.util.List;

public class GameModel {
    private List<Observer> observers;
    private int score;
    private int numLives;
    private static GameModel instance;

    public GameModel() {
        observers = new ArrayList<>();
        score = 0;
        numLives = 3; // Pac-Man starts with 3 lives
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
    private void notifyObservers() {
        System.out.println("Notifying observers: score = " + score + ", lives = " + numLives);
        System.out.println("Observers list before notifying: " + observers); // Log before notifying
        if (observers.isEmpty()) {
            System.out.println("No observers to notify!");
        }
        Platform.runLater(() -> {
            for (Observer observer : observers) {
                observer.update(score, numLives);
            }
        });
    }


    // Simulate the game's logic where the score changes
    public void increaseScore(int points) {
        score += points;
        System.out.println("Score increased: " + score);
        notifyObservers(); // Notify observers about the updated score
    }

    // Simulate the game's logic where a life is lost
    public void loseLife() {
        if (numLives > 0) {
            numLives--;
            System.out.println("Lives: " + numLives);
            notifyObservers(); // Notify observers about the updated lives
        }
    }

    public void startGame() {
        // Game initialization logic
        notifyObservers(); // Initial notification to observers
    }

    public int getScore() {
        return score;
    }

    public int getNumLives() {
        return numLives;
    }

    // Update logic for every frame (call tick() during gameplay)
    public void tick() {
        // Game tick logic
    }
}

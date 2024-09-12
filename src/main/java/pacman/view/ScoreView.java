package pacman.view;

import pacman.model.observer.Observer;

public class ScoreView implements Observer {
    @Override
    public void update(int score, int numLives) {
        System.out.println("Score: " + score + " Lives: " + numLives);
        // Update UI accordingly (e.g., using JavaFX labels)
    }
}

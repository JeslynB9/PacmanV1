package pacman.model.observer;

public interface Observer {

    void update(int score, int numLives);
    void updateReadyScreen(boolean readyScreenActive);
    void updateGameOverScreen(boolean isGameOver);
    void updateYouWinScreen();
}

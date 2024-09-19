//package pacman.model.engine;
//
//import pacman.model.observer.Subject;
//import pacman.model.observer.Observer;
//import java.util.ArrayList;
//import java.util.List;
//
//public class GameModel implements Subject {
//    private List<Observer> observers;
//    private int score;
//    private int numLives;
//
//    public GameModel(int numLives) {
//        this.numLives = numLives;
//        this.score = 0;
//        this.observers = new ArrayList<>();
//    }
//
//    @Override
//    public void registerObserver(Observer o) {
//        observers.add(o);
//    }
//
//    @Override
//    public void removeObserver(Observer o) {
//        observers.remove(o);
//    }
//
//    @Override
//    public void notifyObservers() {
//        for (Observer observer : observers) {
//            observer.update(score, numLives);
//        }
//    }
//
//    public void increaseScore(int points) {
//        score += points;
//        notifyObservers();
//    }
//
//    public void decreaseLives() {
//        numLives--;
//        notifyObservers();
//    }
//
//    public int getNumLives() {
//        return numLives;
//    }
//}

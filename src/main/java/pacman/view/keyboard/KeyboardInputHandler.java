package pacman.view.keyboard;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pacman.model.command.Command;
import pacman.model.command.MoveUpCommand;
import pacman.model.command.MoveDownCommand;
import pacman.model.entity.dynamic.player.Pacman;

/**
 * Responsible for handling keyboard input from player
 */
public class KeyboardInputHandler {
    private Pacman pacman;

    public KeyboardInputHandler(Pacman pacman) {
        this.pacman = pacman;
    }

    public void handlePressed(KeyEvent keyEvent) {
        KeyCode keyCode = keyEvent.getCode();
        Command command = null;
        switch (keyCode) {
            case LEFT:
                break;
            case RIGHT:
                break;
            case DOWN:
                break;
            case UP:
                command = new MoveUpCommand(pacman);
                break;
        };
    }
}

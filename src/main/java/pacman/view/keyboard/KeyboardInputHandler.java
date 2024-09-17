package pacman.view.keyboard;

import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import pacman.model.command.Command;
import pacman.model.command.CommandFactory;
import pacman.model.command.MoveUpCommand;
import pacman.model.command.MoveDownCommand;
import pacman.model.entity.dynamic.player.Pacman;

/**
 * Responsible for handling keyboard input from player
 */
public class KeyboardInputHandler {

    public KeyboardInputHandler() {
    }

    public void handlePressed(KeyEvent keyEvent) {
        System.out.println("Key Pressed: " + keyEvent.getCode());
        KeyCode keyCode = keyEvent.getCode();
        Command command = null;
        CommandFactory CommandFactory = new CommandFactory();

        switch (keyCode) {
            case LEFT:
                command = CommandFactory.createMoveLeftCommand();
                break;
            case RIGHT:
                command = CommandFactory.createMoveRightCommand();
                break;
            case DOWN:
                command = CommandFactory.createMoveDownCommand();
                break;
            case UP:
                command = CommandFactory.createMoveUpCommand();
                break;
        }

        if (command != null) {
            command.execute();
        }
    }
}

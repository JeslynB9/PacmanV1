package pacman.model.command;
import pacman.model.entity.dynamic.player.Pacman;
import pacman.model.factory.PacmanFactory;

public class CommandFactory {
    private static Pacman pacman = PacmanFactory.getPacman();

    public static void setPacman(Pacman p) {
        pacman = p;
    }

    public static Command createMoveUpCommand() {
        return new MoveUpCommand(pacman);
    }

    public static Command createMoveDownCommand() {
        return new MoveDownCommand(pacman);
    }

    public static Command createMoveLeftCommand() {
        return new MoveLeftCommand(pacman);
    }

    public static Command createMoveRightCommand() {
        return new MoveRightCommand(pacman);
    }
}


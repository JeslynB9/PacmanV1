package pacman.model.command;

import pacman.model.entity.dynamic.player.Pacman;

public class MoveRightCommand implements Command {
    private Pacman pacman;

    public MoveRightCommand(Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void execute() {
        pacman.right();
    }
}

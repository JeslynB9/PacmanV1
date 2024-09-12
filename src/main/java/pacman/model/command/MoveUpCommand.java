package pacman.model.command;

import pacman.model.entity.dynamic.player.Pacman;

public class MoveUpCommand implements Command {
    private Pacman pacman;

    public MoveUpCommand(Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void execute() {
        pacman.up();
    }
}

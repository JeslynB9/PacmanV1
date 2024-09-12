package pacman.model.command;

import pacman.model.entity.dynamic.player.Pacman;

public class MoveDownCommand implements Command {
    private Pacman pacman;

    public MoveDownCommand(Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void execute() {
        pacman.down();
    }
}

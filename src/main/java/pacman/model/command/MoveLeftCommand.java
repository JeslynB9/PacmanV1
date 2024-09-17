package pacman.model.command;

import pacman.model.entity.dynamic.player.Pacman;

public class MoveLeftCommand implements Command {
    private Pacman pacman;

    public MoveLeftCommand(Pacman pacman) {
        this.pacman = pacman;
    }

    @Override
    public void execute() {
        pacman.left();
    }
}

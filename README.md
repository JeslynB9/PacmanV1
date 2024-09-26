# Pacman Version 1
## How to Run
There are two steps to run the game.
1. Build the game using:
```
gradle build
```
This may take some time if this is the first time the game is being built.

2. Run the game using:
```
gradle run
```

## Design Patterns

### Observer Pattern
- Subject
  - Subject
- Observer
  - Observer
- ConcreteSubject
  - GameModel
- Concrete Observer
  - GameOverView
  - LifeView
  - ReadyView
  - ScoreView
  - YouWinView

### Factory Pattern
- Product: 
  - Ghost
  - Controllable
  - Collectable
  - StaticEntity
- ConcreteProduct:
  - GhostImpl
  - Pacman
  - Pellet
  - Wall
- Factory:
  - ConcreteEntityFactory
- ConcreteFactory:
  - GhostFactory
  - PacmanFactory
  - PelletFactory
  - Wall1Factory
  - Wall2Factory
  - Wall3Factory
  - Wall4Factory
  - Wall5Factory
  - Wall6Factory

### Command Pattern
- Command
  - Command
- ConcreteCommand
  - MoveDownCommand
  - MoveLeftCommand
  - MoveRightCommand
  - MoveUpCommand
- Client
  - CommandFactory
- Invoker
  - KeyBoardInputHandler
- Receiver
  - Pacman

### Singleton Pattern
- GameModel
- GameConfigurationReader

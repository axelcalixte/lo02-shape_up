package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class BasicAI implements PlayerStrategy {
  private GameState gs;
  private final int playerID;

  public BasicAI(int playerID) {
    this.playerID = playerID;
  }

  @Override
  public void update(GameState up) {
    this.gs = up;
  }

  @Override
  public int getPlayerID() {
    return this.playerID;
  }

  @Override
  public void canMoveOrPlay(Consumer<Coordinates> onPlay, BiConsumer<Coordinates, Coordinates> onMove) {
    var from = gs.board.getOccupiedPositions().iterator().next();
    var to = gs.board.getPlayablePositions().iterator().next();
    onMove.accept(from, to);
  }

  @Override
  public void canPlay(Consumer<Coordinates> onPlay) {
    var to = gs.board.getPlayablePositions().iterator().next();
    onPlay.accept(to);
  }

  @Override
  public void canFinishTurn(Runnable finish, BiConsumer<Coordinates, Coordinates> proceed) {
    finish.run();
  }

  @Override
  public void turnFinished(Runnable onFinish) {
    onFinish.run();
  }

  @Override
  public void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish) {
    onFinish.run();
  }
}

package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;
import shapeup.ui.UI;

import java.util.List;
import java.util.function.BiConsumer;

public class RealPlayer implements PlayerStrategy {
  private final UI ui;
  private final int playerID;

  public RealPlayer(UI ui, int playerID) {
    this.ui = ui;
    this.playerID = playerID;
  }

  @Override
  public void update(GameState gs) {
    ui.update(gs);
  }

  @Override
  public int getPlayerID() {
    return this.playerID;
  }

  @Override
  public void canMoveOrPlay(BiConsumer<Card, Coordinates> onPlay, BiConsumer<Coordinates, Coordinates> onMove) {
    ui.moveOrPlay(playerID, onPlay, onMove);
  }

  @Override
  public void canPlay(BiConsumer<Card, Coordinates> onPlay) {
    ui.play(playerID, onPlay);
  }

  @Override
  public void canFinishTurn(Runnable finish, BiConsumer<Coordinates, Coordinates> move) {
    ui.canFinishTurn(playerID, finish, move);
  }

  @Override
  public void turnFinished(Runnable onFinish) {
    ui.turnFinished(playerID, onFinish);
  }

  @Override
  public void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish) {
    ui.roundFinished(scores, hiddenCard, onFinish);
  }
}

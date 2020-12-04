package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;
import shapeup.ui.UI;
import shapeup.util.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
  public CompletableFuture<MovedOrPlayed> canMoveOrPlay() {
    var future = new CompletableFuture<MovedOrPlayed>();
    ui.moveOrPlay(
            playerID,
            (from, to) -> future.complete(MovedOrPlayed.moved(from, to)),
            (coord, card) -> future.complete(MovedOrPlayed.played(coord, card))
    );
    return future;
  }

  @Override
  public CompletableFuture<Tuple<Card, Coordinates>> canPlay() {
    var future = new CompletableFuture<Tuple<Card, Coordinates>>();
    ui.play(playerID, (card, coord) -> future.complete(new Tuple<>(card, coord)));
    return future;
  }

  @Override
  public CompletableFuture<Tuple<Coordinates, Coordinates>> canMove() {
    var future = new CompletableFuture<Tuple<Coordinates, Coordinates>>();
    ui.move(playerID, (from, to) -> future.complete(new Tuple<>(from, to)));
    return future;
  }

  @Override
  public CompletableFuture<Boolean> canFinishTurn() {
    var future = new CompletableFuture<Boolean>();
    ui.canFinishTurn(playerID, future::complete);
    return future;
  }

  @Override
  public CompletableFuture<Void> turnFinished() {
    var future = new CompletableFuture<Void>();
    ui.turnFinished(playerID, () -> future.complete(null));
    return future;
  }

  @Override
  public CompletableFuture<Void> roundFinished(List<Integer> scores, Card hiddenCard) {
    var future = new CompletableFuture<Void>();
    ui.roundFinished(scores, hiddenCard, () -> future.complete(null));
    return future;
  }
}

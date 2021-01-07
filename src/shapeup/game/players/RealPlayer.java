package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;
import shapeup.ui.UIController;
import shapeup.util.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class RealPlayer implements PlayerStrategy {
  private final UIController uiController;
  private final int playerID;

  public RealPlayer(UIController uiController, int playerID) {
    this.uiController = uiController;
    this.playerID = playerID;
  }

  @Override
  public void update(GameState gs) {
    uiController.update(gs);
  }

  @Override
  public int getPlayerID() {
    return this.playerID;
  }

  @Override
  public CompletableFuture<MovedOrPlayed> canMoveOrPlay() {
    var future = new CompletableFuture<MovedOrPlayed>();
    uiController.moveOrPlay(
            playerID,
            (from, to) -> future.complete(MovedOrPlayed.moved(from, to)),
            (coord, card) -> future.complete(MovedOrPlayed.played(coord, card))
    );
    return future;
  }

  @Override
  public CompletableFuture<Tuple<Card, Coordinates>> canPlay() {
    var future = new CompletableFuture<Tuple<Card, Coordinates>>();
    uiController.play(playerID, (card, coord) -> future.complete(new Tuple<>(card, coord)));
    return future;
  }

  @Override
  public CompletableFuture<Tuple<Coordinates, Coordinates>> canMove() {
    var future = new CompletableFuture<Tuple<Coordinates, Coordinates>>();
    uiController.move(playerID, (from, to) -> future.complete(new Tuple<>(from, to)));
    return future;
  }

  @Override
  public CompletableFuture<Boolean> canFinishTurn() {
    var future = new CompletableFuture<Boolean>();
    uiController.canFinishTurn(playerID, future::complete);
    return future;
  }

  @Override
  public CompletableFuture<Void> turnFinished() {
    var future = new CompletableFuture<Void>();
    uiController.turnFinished(playerID, () -> future.complete(null));
    return future;
  }

  @Override
  public CompletableFuture<Void> roundFinished(List<Integer> scores, Card hiddenCard) {
    var future = new CompletableFuture<Void>();
    uiController.roundFinished(scores, hiddenCard, () -> future.complete(null));
    return future;
  }

  @Override
  public CompletableFuture<Void> gameFinished(List<Integer> scores) {
    var future = new CompletableFuture<Void>();
    uiController.gameFinished(scores, () -> future.complete(null));
    return future;
  }
}

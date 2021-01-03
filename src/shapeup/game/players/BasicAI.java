package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;
import shapeup.util.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;

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
  public CompletableFuture<MovedOrPlayed> canMoveOrPlay() {
    var to = gs.board.getPlayablePositions().iterator().next();
    var card = gs.playerStates[playerID].getHand().get(0);
    return CompletableFuture.completedFuture(MovedOrPlayed.played(to, card));
  }

  @Override
  public CompletableFuture<Tuple<Card, Coordinates>> canPlay() {
    var to = gs.board.getPlayablePositions().iterator().next();
    var card = gs.playerStates[playerID].getHand().get(0);
    return CompletableFuture.completedFuture(new Tuple<>(card, to));
  }

  @Override
  public CompletableFuture<Tuple<Coordinates, Coordinates>> canMove() {
    var from = gs.board.getOccupiedPositions().iterator().next();
    var to = gs.board.getMovablePositions(from).iterator().next();

    return CompletableFuture.completedFuture(new Tuple<>(from, to));
  }

  @Override
  public CompletableFuture<Boolean> canFinishTurn() {
    return CompletableFuture.completedFuture(true);
  }

  @Override
  public CompletableFuture<Void> turnFinished() {
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<Void> roundFinished(List<Integer> scores, Card hiddenCard) {
    return CompletableFuture.completedFuture(null);
  }

  @Override
  public CompletableFuture<Void> gameFinished(List<Integer> scores) {
    return CompletableFuture.completedFuture(null);
  }
}

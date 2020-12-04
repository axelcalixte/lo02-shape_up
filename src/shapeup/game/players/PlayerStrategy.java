package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;
import shapeup.util.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PlayerStrategy {
  void update(GameState up);

  int getPlayerID();

  CompletableFuture<MovedOrPlayed> canMoveOrPlay();

  CompletableFuture<Tuple<Card, Coordinates>> canPlay();

  CompletableFuture<Tuple<Coordinates, Coordinates>> canMove();

  CompletableFuture<Boolean> canFinishTurn();

  CompletableFuture<Void> turnFinished();

  CompletableFuture<Void> roundFinished(List<Integer> scores, Card hiddenCard);
}

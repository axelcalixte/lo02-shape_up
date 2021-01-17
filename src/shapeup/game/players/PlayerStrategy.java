package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;
import shapeup.util.Tuple;

import java.util.List;
import java.util.concurrent.CompletableFuture;

/**
 * Either a real player or an AI.
 * This is what links the model and the controller.
 * <p>
 * Uses futures so the model only sees an imperative, synchronous API.
 */
public interface PlayerStrategy {
  /**
   * Notify this about a game state change.
   *
   * @param up the new game state
   */
  void update(GameState up);

  /**
   * Which player this strategy corresponds to.
   *
   * @return the player's ID
   */
  int getPlayerID();

  /**
   * Asks the player for their next move, which could be moving a card or playing a card.
   *
   * @return the move
   */
  CompletableFuture<MovedOrPlayed> canMoveOrPlay();

  /**
   * Asks the player where they want to play a card, and which card.
   *
   * @return the card and the position
   */
  CompletableFuture<Tuple<Card, Coordinates>> canPlay();

  /**
   * Asks the player from and to where they want to move a card.
   *
   * @return the starting and ending position
   */
  CompletableFuture<Tuple<Coordinates, Coordinates>> canMove();

  /**
   * Asks the player whether they want to finish their turn.
   *
   * @return the answer
   */
  CompletableFuture<Boolean> canFinishTurn();

  /**
   * Notifies the player that the turn is finished. The returned future should be completed by the player and
   * waited on by the caller.
   *
   * @return an empty future
   */
  CompletableFuture<Void> turnFinished();

  /**
   * Notifies the player that the round is finished. The returned future should be completed by the player and
   * waited on by the caller.
   *
   * @return an empty future
   */
  CompletableFuture<Void> roundFinished(List<Integer> scores, Card hiddenCard);

  /**
   * Notifies the player that the game is finished. The returned future should be completed by the player and
   * waited on by the caller.
   *
   * @return an empty future
   */
  CompletableFuture<Void> gameFinished(List<Integer> scores);
}

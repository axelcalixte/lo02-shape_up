package shapeup.ui;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * A controller for a Shape Up! UI. This is what {@link shapeup.game.players.PlayerStrategy} interfaces with.
 * Callbacks <i>must</i> be called only once.
 *
 * @see shapeup.game.players.PlayerStrategy
 */
public interface UIController {
  /**
   * A change happened in the game state and the UI should reflect it.
   */
  void update(GameState gs);

  /**
   * Indicates that the player can move or play a card and sets callbacks for when this happens.
   * Once a callback has been called, the other must not be called.
   *
   * @param playerID the player in question
   * @param onMove   if the player moves a card
   * @param onPlay   if the player plays a card
   */
  void moveOrPlay(int playerID, BiConsumer<Coordinates, Coordinates> onMove, BiConsumer<Coordinates, Card> onPlay);

  /**
   * Indicates that the player can play a card and sets a callback for when this happens.
   *
   * @param playerID the player in question
   * @param onPlay   when the player plays a card
   */
  void play(int playerID, BiConsumer<Card, Coordinates> onPlay);

  /**
   * Indicates that the player can move a card and sets a callback for when this happens.
   *
   * @param playerID the player in question
   * @param move     when the player moves a card
   */
  void move(int playerID, BiConsumer<Coordinates, Coordinates> move);

  /**
   * Indicates that the player can choose to finish their turn and sets a callback for when the choice happens.
   *
   * @param playerID       the player in question
   * @param onShouldFinish when the player decides whether to finish their turn
   */
  void canFinishTurn(int playerID, Consumer<Boolean> onShouldFinish);

  /**
   * Indicates that the current turn is finished and sets a callback for when the player decided that the game can
   * continue.
   *
   * @param playerID          the player in question
   * @param onFinishConfirmed when the player wants to continue
   */
  void turnFinished(int playerID, Runnable onFinishConfirmed);

  /**
   * Indicates that the current round is finished and sets a callback for when someone decided that the game can
   * continue.
   *
   * @param scores     the round's scores
   * @param hiddenCard the round's hidden card
   * @param onFinish   when someone decided the game can continue
   */
  void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish);

  /**
   * Indicates that the game is finished and sets a callback for when the someone decided that the game can
   * end.
   *
   * @param scores   the game's scores
   * @param onFinish when someone decided the game can end
   */
  void gameFinished(List<Integer> scores, Runnable onFinish);
}

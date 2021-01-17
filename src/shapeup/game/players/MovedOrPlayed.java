package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.boards.Coordinates;

import java.util.function.BiConsumer;

/**
 * The result of a game action where the player could have either moved a card or played.
 * Interface inspired by {@link java.util.Optional#ifPresentOrElse}.
 * See {@link PlayerStrategy#canMoveOrPlay}.
 * <p>
 * Example :
 * <pre>
 * var mop = MovedOrPlayed.moved(from, to);
 * mop.ifPlayed((card, coord) -> System.out.println("This won't run."))
 *    .ifMoved((from, to) -> System.out.println("This will run."));
 * </pre>
 */
public class MovedOrPlayed {
  private final Coordinates from;
  private final Coordinates to;
  private final Card card;
  private final Coordinates coord;

  private final MoveOrPlay type;

  /**
   * The type of action.
   */
  public enum MoveOrPlay {Move, Play}

  /**
   * Private for naming reasons.
   */
  private MovedOrPlayed(Coordinates from, Coordinates to) {
    this.type = MoveOrPlay.Move;
    this.from = from;
    this.to = to;
    this.card = null;
    this.coord = null;
  }

  /**
   * Private for naming reasons.
   */
  private MovedOrPlayed(Card card, Coordinates coord) {
    this.type = MoveOrPlay.Play;
    this.card = card;
    this.coord = coord;
    this.from = null;
    this.to = null;
  }

  /**
   * Consctructs a new {@link MovedOrPlayed} where the player moved a card.
   * See {@link shapeup.game.boards.Board#moveCard}
   * *
   *
   * @param from the starting position
   * @param to   the ending position
   * @return the new {@link MovedOrPlayed}
   */
  public static MovedOrPlayed moved(Coordinates from, Coordinates to) {
    return new MovedOrPlayed(from, to);
  }

  /**
   * Consctructs a new {@link MovedOrPlayed} where the player played.
   * See {@link shapeup.game.boards.Board#playCard}
   *
   * @param coord the coordinates
   * @param card  the card
   * @return the new {@link MovedOrPlayed}
   */
  public static MovedOrPlayed played(Coordinates coord, Card card) {
    return new MovedOrPlayed(card, coord);
  }

  /**
   * Runs the given action if the player moved a card.
   *
   * @param action the action
   * @return {@code this}
   */
  public MovedOrPlayed ifMoved(BiConsumer<Coordinates, Coordinates> action) {
    if (this.type == MoveOrPlay.Move) {
      action.accept(from, to);
    }

    return this;
  }

  /**
   * Runs the given action if the player played.
   *
   * @param action the action
   * @return {@code this}
   */
  public MovedOrPlayed ifPlayed(BiConsumer<Card, Coordinates> action) {
    if (this.type == MoveOrPlay.Play) {
      action.accept(card, coord);
    }

    return this;
  }
}

package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.boards.Coordinates;
import shapeup.util.Tuple;

import java.util.function.BiConsumer;

public class MovedOrPlayed {
  private final Coordinates from;
  private final Coordinates to;
  private final Card card;
  private final Coordinates coord;

  private final MoveOrPlay type;

  public enum MoveOrPlay {Move, Play}

  private MovedOrPlayed(Coordinates from, Coordinates to) {
    this.type = MoveOrPlay.Move;
    this.from = from;
    this.to = to;
    this.card = null;
    this.coord = null;
  }

  private MovedOrPlayed(Card card, Coordinates coord) {
    this.type = MoveOrPlay.Play;
    this.card = card;
    this.coord = coord;
    this.from = null;
    this.to = null;
  }

  public static MovedOrPlayed moved(Coordinates from, Coordinates to) {
    return new MovedOrPlayed(from, to);
  }

  public static MovedOrPlayed played(Coordinates coord, Card card) {
    return new MovedOrPlayed(card, coord);
  }

  public MovedOrPlayed ifMoved(BiConsumer<Coordinates, Coordinates> action) {
    if (this.type == MoveOrPlay.Move) {
      action.accept(from, to);
    }

    return this;
  }

  public MovedOrPlayed ifPlayed(BiConsumer<Card, Coordinates> action) {
    if (this.type == MoveOrPlay.Play) {
      action.accept(card, coord);
    }

    return this;
  }
}

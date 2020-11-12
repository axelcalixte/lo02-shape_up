package shapeup.game.boards;

import shapeup.game.Card;

import java.util.Objects;

public class OccupiedPosition {
  public final Coordinates coordinates;
  public final Card card;

  public OccupiedPosition(Coordinates coordinates, Card card) {
    this.coordinates = coordinates;
    this.card = card;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    OccupiedPosition that = (OccupiedPosition) o;

    if (!Objects.equals(coordinates, that.coordinates)) return false;
    return Objects.equals(card, that.card);
  }

  @Override
  public int hashCode() {
    int result = coordinates != null ? coordinates.hashCode() : 0;
    result = 31 * result + (card != null ? card.hashCode() : 0);
    return result;
  }
}

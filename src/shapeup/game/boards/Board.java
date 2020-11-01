package shapeup.game.boards;

import shapeup.game.Card;
import shapeup.util.Tuple;

public interface Board {
  void moveCard(Coordinates from, Coordinates to);
  void playCard(Card card, Coordinates coordinates);

  Coordinates[] getFreePositions();
  Tuple<Coordinates, Card> getOccupiedPositions();
}

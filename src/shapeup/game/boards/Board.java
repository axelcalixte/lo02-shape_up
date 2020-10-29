package shapeup.game.boards;

import shapeup.game.Card;
import shapeup.util.Tuple;

public interface Board<C extends Coordinates> {
  void moveCard(C from, C to);
  void playCard(Card card, C coordinates);

  Coordinates[] getFreePositions();
  Tuple<C, Card> getOccupiedPositions();
}

package shapeup.game.boards;

import shapeup.game.Card;

public interface Board {
  void moveCard(Coordinates from, Coordinates to);
  void playCard(Card card, Coordinates coordinates);

  Coordinates[] getFreePositions();
  OccupiedPosition[] getOccupiedPositions();
}

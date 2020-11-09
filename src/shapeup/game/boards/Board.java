package shapeup.game.boards;

import shapeup.game.Card;

public interface Board {
  void moveCard(GridCoordinates from, GridCoordinates to);
  void playCard(Card card, GridCoordinates coordinates);

  GridCoordinates[] getFreePositions();
  OccupiedPosition[] getOccupiedPositions();
}

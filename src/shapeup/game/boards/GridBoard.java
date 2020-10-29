package shapeup.game.boards;

import shapeup.game.Board;
import shapeup.game.Card;
import shapeup.game.Coordinates;
import shapeup.util.Tuple;

public class GridBoard implements Board<GridCoordinates> {
  public GridBoard() {}

  @Override
  public void moveCard(GridCoordinates from, GridCoordinates to) {

  }

  @Override
  public void playCard(Card card, GridCoordinates coordinates) {

  }

  @Override
  public Coordinates[] getFreePositions() {
    return new Coordinates[0];
  }

  @Override
  public Tuple<GridCoordinates, Card> getOccupiedPositions() {
    return null;
  }
}

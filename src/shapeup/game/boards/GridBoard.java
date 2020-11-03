package shapeup.game.boards;

import shapeup.game.Card;

public class GridBoard implements Board {
  public GridBoard() { }

  @Override
  public void moveCard(Coordinates from, Coordinates to) {

  }

  @Override
  public void playCard(Card card, Coordinates coordinates) {

  }

  @Override
  public Coordinates[] getFreePositions() {
    return new Coordinates[0];
  }

  @Override
  public OccupiedPosition[] getOccupiedPositions() {
    return null;
  }
}

package shapeup.game.boards;

public class GridCoordinates implements Coordinates {
  /**
   * Package-private because only boards should create coordinates.
   */
  GridCoordinates() { }

  @Override
  public boolean isAdjacentTo(Coordinates other) {
    return false;
  }

  @Override
  public boolean equals(Coordinates other) {
    return false;
  }
}

package shapeup.game.boards;

public class GridCoordinates {
  protected int x;
  protected int y;

  public GridCoordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isAdjacentTo(GridCoordinates other) {
    int xDiff = Math.abs(other.x - this.x);
    int yDiff = Math.abs(other.y - this.y);
    return xDiff <= 1 && yDiff <= 1 && (xDiff == 1 ^ yDiff == 1);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    GridCoordinates that = (GridCoordinates) o;

    if (x != that.x) return false;
    return y == that.y;
  }

  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    return result;
  }

  @Override
  public String toString() {
    return "GridCoordinates{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}

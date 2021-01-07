package shapeup.game.boards;

/**
 * A class representing a pair of cartesian coordinates for the boards of the Shape Up! game.
 */
public class Coordinates {
  protected final int x;
  protected final int y;

  /**
   * Constructs a pair of cartesian Coordinates.
   *
   * @param x - an int for the horizontal value of the coordinates.
   * @param y - an int for the vertical value of the coordinates.
   */
  public Coordinates(int x, int y) {
    this.x = x;
    this.y = y;
  }

  /**
   * Returns the x value of the coordinates.
   *
   * @return the x value of the coordinates.
   */
  public int getX() {
    return x;
  }

  /**
   * Returns the y value of the coordinates.
   *
   * @return the y value of the coordinates.
   */
  public int getY() {
    return y;
  }

  /**
   * Indicates whether some other object is "equal to" a coordinate.
   *
   * @param o - the object to compare the coordinates to.
   * @return true if this coordinate is the same as the obj argument; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Coordinates that = (Coordinates) o;

    if (x != that.x) return false;
    return y == that.y;
  }

  /**
   * Returns the hash of the coordinates.
   *
   * @return an int representing the hash.
   */
  @Override
  public int hashCode() {
    int result = x;
    result = 31 * result + y;
    return result;
  }

  /**
   * Returns a string representation of the coordinates.
   *
   * @return a string representation of the coordinate.
   */
  @Override
  public String toString() {
    return "GridCoordinates{" +
            "x=" + x +
            ", y=" + y +
            '}';
  }
}

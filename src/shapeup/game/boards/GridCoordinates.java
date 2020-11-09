package shapeup.game.boards;

public class GridCoordinates {
  /**
   * Package-private because only boards should create coordinates.
   */
  protected int x;
  protected int y;

  GridCoordinates() {
  this.x = x;
  this.y = y;
  }

  public boolean isAdjacentTo(GridCoordinates other) {
    boolean adj = false;
    if (Math.abs(other.x - this.x) == 1 || Math.abs(other.y - this.y) == 1) {
      adj = true;
    }
    return adj;
  }

  public boolean equals(GridCoordinates other) {
    boolean equ = false;
    if (other.x == this.x && other.y == this.y){
      equ = true;
    }
    return equ;
  }
}

package shapeup.game.boards;

public interface Coordinates {
  boolean isAdjacentTo(Coordinates other);
  boolean equals(Coordinates other);
}

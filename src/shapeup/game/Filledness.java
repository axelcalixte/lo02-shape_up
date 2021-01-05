package shapeup.game;

/**
 * An enumeration for a card's filledness.
 */
public enum Filledness {
  FILLED, HOLLOW;

  /**
   * Returns true if the value of the filledness is FILLED; false if the value of the filledness is HOLLOW.
   *
   * @return true if FILLED; false if HOLLOW.
   */
  public boolean isFilled() {
    return switch (this) {
      case FILLED -> true;
      case HOLLOW -> false;
    };
  }
}

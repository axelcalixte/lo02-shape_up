package shapeup.game;

public enum Filledness {
  FILLED, HOLLOW;

  public boolean isFilled() {
    return switch (this) {
      case FILLED -> true;
      case HOLLOW -> false;
    };
  }
}

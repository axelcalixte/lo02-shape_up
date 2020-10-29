package shapeup.game;

public final class Card {
  private final Color color;
  private final Shape shape;
  private final Filledness filledness;

  Card(Color color, Shape shape, Filledness filledness) {
    this.color = color;
    this.shape = shape;
    this.filledness = filledness;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    if (color != card.color) return false;
    if (shape != card.shape) return false;
    return filledness == card.filledness;
  }

  public Color getColor() {
    return color;
  }

  public Shape getShape() {
    return shape;
  }

  public Filledness getFilledness() {
    return filledness;
  }
}

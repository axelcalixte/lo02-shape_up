package shapeup.game;

/**
 * A Shape Up! game card.
 */
public final class Card {
  private final Color color;
  private final Shape shape;
  private final Filledness filledness;

  /**
   * Creates a new card instance.
   *
   * @param color      an enum for the card's Color.
   * @param shape      an enum for the card's Shape.
   * @param filledness an enum for the card's Filledness.
   */
  public Card(Color color, Shape shape, Filledness filledness) {
    this.color = color;
    this.shape = shape;
    this.filledness = filledness;
  }

  /**
   * Indicates whether some other object is "equal to" a card.
   *
   * @param o the object to compare the card to.
   * @return true if this card is the same as the obj argument; false otherwise.
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    Card card = (Card) o;

    if (color != card.color) return false;
    if (shape != card.shape) return false;
    return filledness == card.filledness;
  }

  /**
   * Returns the color of the card.
   *
   * @return the color of the card.
   */
  public Color getColor() {
    return color;
  }

  /**
   * Returns the shape of the card.
   *
   * @return the shape of the card.
   */
  public Shape getShape() {
    return shape;
  }

  /**
   * Returns the filledness of the card.
   *
   * @return the filledness of the card.
   */
  public Filledness getFilledness() {
    return filledness;
  }

  /**
   * Returns a string representation of the card.
   *
   * @return a string representation of the card.
   */
  @Override
  public String toString() {
    return "Card{" +
            "color=" + color +
            ", shape=" + shape +
            ", filledness=" + filledness +
            '}';
  }
}

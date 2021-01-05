package shapeup.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

/**
 * A Shape Up! deck class.
 */
public final class Deck {
  private final ArrayList<Card> cards = new ArrayList<>(Shape.values().length * Color.values().length * Filledness.values().length);

  /**
   * Creates a new deck instance.
   */
  public Deck() {
    for (Shape s : Shape.values()) {
      for (Color c : Color.values()) {
        for (Filledness f : Filledness.values()) {
          this.cards.add(new Card(c, s, f));
        }
      }
    }
    Collections.shuffle(this.cards);
  }

  /**
   * Returns an Optional containing a card if the deck isn't empty; an empty Optional instance otherwise.
   *
   * @return an Optional containing a card if the deck isn't empty; an empty Optional instance otherwise.
   */
  public Optional<Card> drawCard() {
    if (this.cards.size() == 0) {
      return Optional.empty();
    }
    Card drawnCard = this.cards.remove(this.cards.size() - 1);
    return Optional.of(drawnCard);
  }

  /**
   * Returns the number of cards left in the deck.
   *
   * @return an int equals to the number of cards left in the deck.
   */
  public int cardsLeft() {
    return this.cards.size();
  }


  /**
   * Returns a string representation of the deck.
   *
   * @return a string representation of the deck.
   */
  @Override
  public String toString() {
    return "Deck{" +
            cards +
            '}';
  }
}
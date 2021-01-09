package shapeup.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A class representing a Shape Up! player.
 */
public final class PlayerState {
  private Card victoryCard;
  private final int playerID;
  private final List<Card> hand;

  /**
   * Constructs an instance of PlayerState.
   *
   * @param playerID - an int representing the player's ID.
   */
  public PlayerState(int playerID) {
    this.playerID = playerID;
    this.hand = new ArrayList<>();
  }

  /**
   * Gives a card to the player.
   *
   * @param c - a card to give
   */
  public void giveCard(Card c) {
    this.hand.add(c);
  }

  /**
   * Gives the player their victory card.
   *
   * @param c - a card to give.
   */
  public void giveVictoryCard(Card c) {
    this.victoryCard = c;
  }

  public Optional<Card> takeCard(int idx) {
    if (idx < 0 || idx >= this.hand.size()) {
      return Optional.empty();
    }
    Card cardTaken = this.hand.remove(idx);
    return Optional.of(cardTaken);
  }

  /**
   * Returns either the player's victory card or an empty Optional.
   *
   * @return an instance of a card or an empty Optional.
   */
  public Optional<Card> getVictoryCard() {
    return Optional.ofNullable(victoryCard)
            .or(() -> hand.size() > 0
                    ? Optional.of(hand.get(0))
                    : Optional.empty()
            );
  }

  /**
   * Returns the player's ID.
   *
   * @return an int representing the player's ID.
   */
  public int getPlayerID() {
    return this.playerID;
  }

  /**
   * The player's hand.
   *
   * @return the hand
   */
  public List<Card> getHand() {
    return this.hand;
  }
}

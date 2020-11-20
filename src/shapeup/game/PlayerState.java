package shapeup.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PlayerState {
  private Card victoryCard;
  private final int playerID;
  private final List<Card> hand;

  public PlayerState(int playerID) {
    this.playerID = playerID;
    this.hand = new ArrayList<>();
  }

  public void giveCard(Card c) {
    this.hand.add(c);
  }

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

  public Optional<Card> getVictoryCard() {
    return Optional.ofNullable(victoryCard)
            .or(() -> hand.size() > 0
                    ? Optional.of(hand.get(0))
                    : Optional.empty()
            );
  }

  public int getPlayerID() {
    return this.playerID;
  }

  public List<Card> getHand() {
    return this.hand;
  }
}

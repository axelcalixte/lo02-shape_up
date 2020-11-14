package shapeup.game;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class PlayerState {
  private Card victoryCard;
  private int playerID;
  private List<Card> hand;

  public PlayerState(int playerID) {
    this.playerID = playerID;
    this.hand = new ArrayList<>();
  }

  public void giveCard(Card c) {}

  public void giveVictoryCard(Card c) {}

  public Optional<Card> takeCard(int idx) {
    return Optional.empty();
  }

  public Optional<Card> getVictoryCard() {
    return Optional.ofNullable(victoryCard);
  }

  public int getPlayerID() {
    return this.playerID;
  }

  public List<Card> getHand() {
    return this.hand;
  }
}

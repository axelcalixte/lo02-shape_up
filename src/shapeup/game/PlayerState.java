package shapeup.game;

import java.util.Optional;

public final class PlayerState {
  private Card victoryCard;
  private int playerID;
  private Card[] hand;

  public PlayerState(int playerID) {
    this.playerID = playerID;
    this.hand = new Card[3];
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
    return playerID;
  }

  public Card[] getHand() {
    return hand;
  }
}

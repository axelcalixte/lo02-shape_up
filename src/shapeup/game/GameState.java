package shapeup.game;

import shapeup.game.boards.GridBoard;

public class GameState {
  public final PlayerState[] playerStates;
  public final GridBoard board;
  public final Deck deck;

  public GameState(PlayerState[] playerStates, GridBoard board, Deck deck) {
    this.playerStates = playerStates;
    this.board = board;
    this.deck = deck;
  }
}

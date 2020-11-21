package shapeup.game;

import shapeup.game.boards.Board;

public class GameState {
  public final PlayerState[] playerStates;
  public final Board board;
  public final Deck deck;

  public GameState(PlayerState[] playerStates, Board board, Deck deck) {
    this.playerStates = playerStates;
    this.board = board;
    this.deck = deck;
  }
}

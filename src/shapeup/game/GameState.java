package shapeup.game;

import shapeup.game.boards.Board;

/**
 * A GameState class containing a Shape Up! game's objects.
 * <p>
 * Should be used when one wants to pass the game state as an argument.
 */
public class GameState {
  public final PlayerState[] playerStates;
  public final Board board;
  public final Deck deck;

  /**
   * Creates a new GameState instance.
   *
   * @param playerStates the players for the game.
   * @param board        the board of the game.
   * @param deck         the deck of the game.
   */
  public GameState(PlayerState[] playerStates, Board board, Deck deck) {
    this.playerStates = playerStates;
    this.board = board;
    this.deck = deck;
  }
}

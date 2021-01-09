package shapeup.game.scores;

import shapeup.game.Card;
import shapeup.game.boards.CircleBoard;
import shapeup.game.boards.GridBoard;

/**
 * Counts the scores for a game of Shape Up!.
 */
public interface ScoreCounterVisitor {
  /**
   * Count the given victory card's score on a Gridboard.
   * @param board the board
   * @param victoryCard the victory card
   * @return the score
   */
  int countGridBoard(GridBoard board, Card victoryCard);

  /**
   * Count the given victory card's score on a CircleBoard.
   * @param board the board
   * @param victoryCard the victory card
   * @return the score
   */
  int countCircleBoard(CircleBoard board, Card victoryCard);
}

package shapeup.game.boards;

import shapeup.game.Card;
import shapeup.game.scores.ScoreCounterVisitor;

import java.util.Optional;
import java.util.Set;

/**
 * A Shape Up! game board.
 */
public interface Board {
  /**
   * Puts a cord on the board at the specified position.
   * @param card the card
   * @param coordinates the position
   */
  void playCard(Card card, Coordinates coordinates);

  /**
   * Moves a cord from one position to another.
   * @param from the starting position
   * @param to the ending position
   */
  void moveCard(Coordinates from, Coordinates to);

  /**
   * Gets the card at the given coordinates, or none if this space is empty.
   * Doesn't remove the card.
   * @param coordinates the coordinates
   * @return the card
   */
  Optional<Card> getCard(Coordinates coordinates);

  /**
   * All the positions where a card can be placed according to the game rules.
   * @return the positions
   */
  Set<Coordinates> getPlayablePositions();

  /**
   * All the positions where the card at a given position can be moved. Different from
   * {@link Board#getPlayablePositions()} because the positions adjacent to this card only are not valid
   * targets for moving it.
   * @param from the position
   * @return the positions the corresponding card can be moved to
   */
  Set<Coordinates> getMovablePositions(Coordinates from);

  /**
   * All the positions where a card is present.
   * @return the positions
   */
  Set<Coordinates> getOccupiedPositions();

  /**
   * Calculates the score for the given victory card using the given visitor.
   * @param scoreCounter the visitor
   * @param victoryCard the victory card
   * @return the score
   */
  int acceptScoreCounter(ScoreCounterVisitor scoreCounter, Card victoryCard);

  /**
   * Whether these two positions are adjacent.
   * @param a the first position
   * @param b the second position
   * @return whether these two positions are adjacent
   */
  boolean areAdjacent(Coordinates a, Coordinates b);

  /**
   * @return the minimum x coordinate, as far as the view is concerned
   */
  int displayMinX();

  /**
   * @return the maximum x coordinate, as far as the view is concerned
   */
  int displayMaxX();

  /**
   * @return the minimum y coordinate, as far as the view is concerned
   */
  int displayMinY();

  /**
   * @return the maximum y coordinate, as far as the view is concerned
   */
  int displayMaxY();
}

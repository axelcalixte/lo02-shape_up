package shapeup.game.boards;

import shapeup.game.Card;
import shapeup.game.scores.ScoreCounterVisitor;
import shapeup.ui.BoardDisplayer;

import java.util.*;

// TODO: extract interface

/**
 * A Shape Up! game board.
 * Implements the standard rules.
 */
public final class GridBoard implements Board {
  /**
   * MUST be greater than <pre>maxWidth</pre>
   */
  public static final int maxLength = 5;
  /**
   * MUST be lower than <pre>maxLength</pre>
   */
  public static final int maxWidth = 3;

  /**
   * The cards currently in play.
   * A map is easier to handle than a two-dimensionnal array given the game rules.
   */
  private final Map<Coordinates, Card> cards;

  /**
   * Creates an empty game board.
   */
  public GridBoard() {
    this.cards = new HashMap<>();
  }

  /**
   * Moves the card at <pre>from</pre> to the <pre>to</pre> position.
   *
   * @param from the starting position.
   * @param to   the end position.
   * @throws IllegalArgumentException when there is no card at <pre>from</pre> or <pre>to</pre> is an illegal position.
   */
  @Override
  public void moveCard(Coordinates from, Coordinates to) {
    if (!this.isMovable(from, to)) {
      throw new IllegalArgumentException();
    }

    this.cards.put(to, this.cards.remove(from));
  }

  /**
   * Puts a card on the board.
   *
   * @param card        the card to be played.
   * @param coordinates where it should be put.
   * @throws IllegalArgumentException when the move is illegal.
   */
  @Override
  public void playCard(Card card, Coordinates coordinates) {
    if (!this.isPlayable(coordinates))
      throw new IllegalArgumentException();

    this.cards.put(coordinates, card);
  }

  /**
   * Gets a card.
   *
   * @param coordinates where.
   * @return the card, or <pre>Optional.empty()</pre> if there is no card.
   */
  @Override
  public Optional<Card> getCard(Coordinates coordinates) {
    return Optional.ofNullable(this.cards.get(coordinates));
  }

  /**
   * Computes the position that would be valid inputs to <pre>playCard</pre>.
   *
   * @return the positions' coordinates.
   */
  @Override
  public Set<Coordinates> getPlayablePositions() {
    var freePositions = new HashSet<Coordinates>();

    if (this.cards.size() == 0) {
      freePositions.add(new Coordinates(0, 0));
    } else {
      int maxX = maxX(this.cards);
      int maxY = maxY(this.cards);
      for (int x = minX(this.cards) - 1; x <= maxX + 1; ++x) {
        for (int y = minY(this.cards) - 1; y <= maxY + 1; ++y) {

          var coordinates = new Coordinates(x, y);

          if (this.isPlayable(coordinates)) {
            freePositions.add(coordinates);
          }
        }
      }
    }

    return freePositions;
  }

  /**
   * Computes the position that would be valid inputs to <pre>moveCard(from)</pre>.
   *
   * @return the positions' coordinates.
   */
  @Override
  public Set<Coordinates> getMovablePositions(Coordinates from) {
    var freePositions = new HashSet<Coordinates>();

    int maxX = maxX(this.cards);
    int maxY = maxY(this.cards);
    for (int x = minX(this.cards) - 1; x <= maxX + 1; ++x) {
      for (int y = minY(this.cards) - 1; y <= maxY + 1; ++y) {

        var to = new Coordinates(x, y);

        if (this.isMovable(from, to)) {
          freePositions.add(to);
        }
      }
    }

    return freePositions;
  }

  /**
   * Computes the positions at which there are cards.
   *
   * @return the positions' coordinates.
   */
  @Override
  public Set<Coordinates> getOccupiedPositions() {
    return new HashSet<>(this.cards.keySet());
  }

  private boolean isPlayable(Coordinates coordinates) {
    return isPlayableImpl(coordinates, this.cards);
  }

  private boolean isMovable(Coordinates from, Coordinates to) {
    var withoutFrom = new HashMap<>(this.cards);
    withoutFrom.remove(from);
    return isPlayableImpl(to, withoutFrom);
  }

  private static boolean isPlayableImpl(Coordinates coordinates, Map<Coordinates, Card> cards) {
    if (cards.size() == 0) return true;

    // Musn't be occupied
    if (cards.containsKey(coordinates))
      return false;

    // Must be adjacent to an existing card
    if (cards.entrySet().stream().noneMatch(entry -> adjacent(entry.getKey(), coordinates)))
      return false;

    // Musn't be out of bound
    int maxX = maxX(cards);
    int minX = minX(cards);
    int maxY = maxY(cards);
    int minY = minY(cards);

    int xSize = maxX - minX;
    int ySize = maxY - minY;

    int newXSize = Math.max(xSize, Math.max(maxX - coordinates.x, coordinates.x - minX));
    int newYSize = Math.max(ySize, Math.max(maxY - coordinates.y, coordinates.y - minY));

    if (newXSize > maxLength || newYSize > maxLength) return false;

    if (newXSize > maxWidth && newYSize > maxWidth) return false;

    return true;
  }

  public static boolean adjacent(Coordinates a, Coordinates b) {
    int xDiff = Math.abs(a.x - b.x);
    int yDiff = Math.abs(a.y - b.y);
    return xDiff <= 1 && yDiff <= 1 && (xDiff == 1 ^ yDiff == 1);
  }

  @Override
  public int acceptScoreCounter(ScoreCounterVisitor scoreCounter, Card victoryCard) {
    return scoreCounter.countGridBoard(this, victoryCard);
  }

  @Override
  public BoardDisplayer displayer() {
    return new BoardDisplayer(this);
  }

  public int maxX() {
    return maxX(this.cards);
  }

  public int minX() {
    return minX(this.cards);
  }

  public int maxY() {
    return maxY(this.cards);
  }

  public int minY() {
    return minY(this.cards);
  }

  public static int maxX(Map<Coordinates, Card> cards) {
    return cards.keySet().stream().mapToInt(card -> card.x).max().orElse(0);
  }

  public static int minX(Map<Coordinates, Card> cards) {
    return cards.keySet().stream().mapToInt(card -> card.x).min().orElse(0);
  }

  public static int maxY(Map<Coordinates, Card> cards) {
    return cards.keySet().stream().mapToInt(card -> card.y).max().orElse(0);
  }

  public static int minY(Map<Coordinates, Card> cards) {
    return cards.keySet().stream().mapToInt(card -> card.y).min().orElse(0);
  }

  @Override
  public String toString() {
    return "GridBoard{" + cards + '}';
  }
}

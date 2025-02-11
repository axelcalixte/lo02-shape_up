package shapeup.game.boards;

import shapeup.game.Card;
import shapeup.game.scores.ScoreCounterVisitor;

import java.util.*;

import static java.lang.Math.abs;

/**
 * A circular board, made of a center, and two concentric circles.
 * x == part of the board
 * <pre>
 * |x| |x| |x|
 * | |x|x|x| |
 * |x|x|x|x|x|
 * | |x|x|x| |
 * |x| |x| |x|
 * </pre>
 */
public class CircleBoard implements Board {
  /**
   * Shouldn't be changed. Used by the score counter.
   */
  public static final int SIZE = 5;
  /**
   * Shouldn't be changed. Used by the score counter.
   */
  public static final int CENTER = SIZE / 2;

  /**
   * The cards currently in play.
   * A map is easier to handle than a two-dimensionnal array given the game rules.
   */
  private final Map<Coordinates, Card> cards;

  /**
   * Creates an empty game board.
   */
  public CircleBoard() {
    this.cards = new HashMap<>();
  }

  /**
   * Puts a cord on the board at the specified position.
   *
   * @param card        the card
   * @param coordinates the position
   */
  @Override
  public void playCard(Card card, Coordinates coordinates) {
    if (!this.isPlayable(coordinates))
      throw new IllegalArgumentException();

    this.cards.put(coordinates, card);
  }

  /**
   * Moves a cord from one position to another.
   *
   * @param from the starting position
   * @param to   the ending position
   */
  @Override
  public void moveCard(Coordinates from, Coordinates to) {
    if (!this.isMovable(from, to)) {
      throw new IllegalArgumentException();
    }

    this.cards.put(to, this.cards.remove(from));
  }

  /**
   * Gets the card at the given coordinates, or none if this space is empty.
   * Doesn't remove the card.
   *
   * @param coordinates the coordinates
   * @return the card
   */
  @Override
  public Optional<Card> getCard(Coordinates coordinates) {
    return Optional.ofNullable(this.cards.get(coordinates));
  }

  /**
   * All the positions where a card can be placed according to the game rules.
   * For this board, this means positions on the concentric circles with a card
   * next to then on the circle or another circle.
   *
   * @return the positions
   */
  @Override
  public Set<Coordinates> getPlayablePositions() {
    var freePositions = new HashSet<Coordinates>();

    for (int x = 0; x < CircleBoard.SIZE; x++) {
      for (int y = 0; y < CircleBoard.SIZE; y++) {
        var coords = new Coordinates(x, y);
        if (this.isPlayable(coords)) freePositions.add(coords);
      }
    }

    return freePositions;
  }

  /**
   * All the positions where the card at a given position can be moved. Different from
   * {@link Board#getPlayablePositions()} because the positions adjacent to this card only are not valid
   * targets for moving it.
   *
   * @param from the position
   * @return the positions the corresponding card can be moved to
   */
  @Override
  public Set<Coordinates> getMovablePositions(Coordinates from) {
    var freePositions = new HashSet<Coordinates>();

    for (int x = 0; x < CircleBoard.SIZE; x++) {
      for (int y = 0; y < CircleBoard.SIZE; y++) {
        var to = new Coordinates(x, y);
        if (this.isMovable(from, to)) freePositions.add(to);
      }
    }

    return freePositions;
  }

  /**
   * All the positions where a card is present.
   *
   * @return the positions
   */
  @Override
  public Set<Coordinates> getOccupiedPositions() {
    return new HashSet<>(this.cards.keySet());
  }

  /**
   * Calculates the score for the given victory card using the given visitor.
   * Calls {@link ScoreCounterVisitor#countCircleBoard}.
   *
   * @param scoreCounter the visitor
   * @param victoryCard  the victory card
   * @return the score
   */
  @Override
  public int acceptScoreCounter(ScoreCounterVisitor scoreCounter, Card victoryCard) {
    return scoreCounter.countCircleBoard(this, victoryCard);
  }

  /**
   * Whether these two positions are adjacent.
   *
   * @param a the first position
   * @param b the second position
   * @return whether these two positions are adjacent
   */
  @Override
  public boolean areAdjacent(Coordinates a, Coordinates b) {
    return adjacent(a, b);
  }

  /**
   * @return the minimum x coordinate, as far as the view is concerned
   */
  @Override
  public int displayMinX() {
    return 0;
  }

  /**
   * @return the maximum x coordinate, as far as the view is concerned
   */
  @Override
  public int displayMaxX() {
    return CircleBoard.SIZE - 1;
  }

  /**
   * @return the minimum y coordinate, as far as the view is concerned
   */
  @Override
  public int displayMinY() {
    return 0;
  }

  /**
   * @return the maximum y coordinate, as far as the view is concerned
   */
  @Override
  public int displayMaxY() {
    return CircleBoard.SIZE - 1;
  }

  /**
   * Whether a card can be played at the coordinates.
   *
   * @param coordinates the coordinates
   * @return whether a card can be played
   */
  private boolean isPlayable(Coordinates coordinates) {
    return isPlayableImpl(coordinates, this.cards);
  }

  /**
   * Whether a card can be moved between these coordinates.
   *
   * @param from start
   * @param to   end
   * @return whether a card can be moved
   */
  private boolean isMovable(Coordinates from, Coordinates to) {
    var withoutFrom = new HashMap<>(this.cards);
    withoutFrom.remove(from);
    return isPlayableImpl(to, withoutFrom);
  }

  /**
   * Implementation of {@link CircleBoard#isPlayable}.
   */
  private static boolean isPlayableImpl(Coordinates coordinates, Map<Coordinates, Card> cards) {
    if (cards.size() == 0)
      return isCenter(coordinates);

    // Musn't be occupied
    if (cards.containsKey(coordinates)) return false;

    // Must be in bounds
    boolean inBounds = isCenter(coordinates) || inFirstCircle(coordinates) || inSecondCircle(coordinates);
    if (!inBounds)
      return false;

    // Must be adjacent to an existing card
    return cards.entrySet().stream().anyMatch(entry -> adjacent(entry.getKey(), coordinates));
  }

  /**
   * According to the CircleBoard adjacency rules.
   */
  private static boolean adjacent(Coordinates a, Coordinates b) {
    if (isCenter(a)) return inFirstCircle(b);
    if (isCenter(b)) return inFirstCircle(a);

    int xDiff = abs(a.x - b.x);
    int yDiff = abs(a.y - b.y);

    // Check for line/column adjacency.
    if (GridBoard.adjacent(a, b))
      return true;

    // Check for diagonal adjacency.
    boolean bothInADiagonal;
    bothInADiagonal = abs(a.x - CENTER) == abs(a.y - CENTER) && abs(b.x - CENTER) == abs(b.y - CENTER);
    boolean diagonallyAdjacent = xDiff == 1 && yDiff == 1;
    if (bothInADiagonal && diagonallyAdjacent)
      return true;

    // Check for second circle adjacency.
    if (inSecondCircle(a)
            && inSecondCircle(b)
            && (xDiff == 2 && yDiff == 0 || xDiff == 0 && yDiff == 2))
      return true;
    else
      return false;
  }

  private static boolean isCenter(Coordinates coordinates) {
    return coordinates.x == CircleBoard.CENTER && coordinates.y == CircleBoard.CENTER;
  }

  /**
   * @param coordinates the coordinates
   * @return whether the coordinates are part of the board's first concentric circle
   */
  public static boolean inFirstCircle(Coordinates coordinates) {
    return !isCenter(coordinates)
            && abs(coordinates.x - CircleBoard.CENTER) <= 1
            && abs(coordinates.y - CircleBoard.CENTER) <= 1;
  }

  /**
   * @param coordinates the coordinates
   * @return whether the coordinates are part of the board's first concentric circle
   */
  public static boolean inSecondCircle(Coordinates coordinates) {
    final int x = abs(coordinates.x - CircleBoard.CENTER);
    final int y = abs(coordinates.y - CircleBoard.CENTER);

    if (isCenter(coordinates)) return false;
    if (inFirstCircle(coordinates)) return false;
    if (x > 2 || y > 2) return false;
    if (x == 1 || y == 1) return false;
    return true;
  }

  /**
   * @param coordinates the coordinates
   * @return the coordinates' angle relative to the center of the board
   */
  public static double angle(Coordinates coordinates) {
    double x = coordinates.x - CENTER;
    double y = coordinates.y - CENTER;

    // https://fr.wikipedia.org/wiki/Coordonn%C3%A9es_polaires#Conversion_entre_syst%C3%A8me_polaire_et_cart%C3%A9sien
    if (x > 0 && y >= 0)
      return Math.atan(y / x);
    else if (x > 0 && y < 0)
      return Math.atan(y / x) + 2 * Math.PI;
    else if (x < 0)
      return Math.atan(y / x) + Math.PI;
    else if (x == 0 && y > 0)
      return Math.PI / 2;
    else if (x == 0 && y < 0)
      return 3 * Math.PI / 2;
    else // x == 0 && y == 0
      return 0;
  }
}

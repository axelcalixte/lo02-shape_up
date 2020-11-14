package shapeup.game.boards;

import shapeup.game.Card;
import shapeup.game.Color;
import shapeup.game.Filledness;
import shapeup.game.Shape;
import shapeup.ui.BoardDisplayer;
import shapeup.ui.GridBoardDisplayer;

import java.util.*;

// TODO: extract interface
/**
 * A Shape Up! game board.
 * Implements the standard rules.
 */
public final class GridBoard {
  public static void main(String[] args) {
    var gb = new GridBoard();

    Runnable print = () -> {
      System.out.println(gb);
      System.out.println(gb.getOccupiedPositions());
      System.out.println(gb.getPlayablePositions());
      System.out.println("-----------------------");
    };

    print.run();

    gb.playCard(new Card(Color.BLUE, Shape.CIRCLE, Filledness.HOLLOW), gb.getPlayablePositions().iterator().next());

    print.run();

    gb.playCard(new Card(Color.BLUE, Shape.CIRCLE, Filledness.HOLLOW), gb.getPlayablePositions().iterator().next());

    print.run();

    gb.playCard(new Card(Color.BLUE, Shape.CIRCLE, Filledness.HOLLOW), gb.getPlayablePositions().iterator().next());

    print.run();
  }

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
  private final Map<GridCoordinates, Card> cards;

  /**
   * Creates an empty game board.
   */
  public GridBoard() {
    this.cards = new HashMap<>();
  }

  /**
   * @return the board's display name.
   */
  public String name() {
    return "GridBoard";
  }

  /**
   * Moves the card at <pre>from</pre> to the <pre>to</pre> position.
   *
   * @param from the starting position.
   * @param to   the end position.
   * @throws IllegalArgumentException when there is no card at <pre>from</pre> or <pre>to</pre> is an illegal position.
   */
  public void moveCard(GridCoordinates from, GridCoordinates to) {
    // Needs to be removed before isPlayable() check
    var movedCard = this.cards.remove(from);

    if (!this.isPlayable(to)) {
      this.cards.put(from, movedCard);
      throw new IllegalArgumentException();
    }

    this.cards.put(to, movedCard);
  }

  /**
   * Puts a card on the board.
   *
   * @param card        the card to be played.
   * @param coordinates where it should be put.
   * @throws IllegalArgumentException when the move is illegal.
   */
  public void playCard(Card card, GridCoordinates coordinates) {
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
  public Optional<Card> getCard(GridCoordinates coordinates) {
    return Optional.ofNullable(this.cards.get(coordinates));
  }

  /**
   * Computes the position that would be valid inputs to <pre>playCard</pre>.
   *
   * @return the positions' coordinates.
   */
  public Set<GridCoordinates> getPlayablePositions() {
    var freePositions = new HashSet<GridCoordinates>();

    if (this.cards.size() == 0) {
      freePositions.add(new GridCoordinates(0, 0));
    } else {
      int maxX = this.maxX();
      int maxY = this.maxY();
      for (int x = minX() - 1; x <= maxX + 1; ++x) {
        for (int y = this.minY() - 1; y <= maxY + 1; ++y) {

          var coordinates = new GridCoordinates(x, y);

          if (this.isPlayable(coordinates)) {
            freePositions.add(coordinates);
          }
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
  public Set<GridCoordinates> getOccupiedPositions() {
    return this.cards.keySet();
  }

  public BoardDisplayer getDisplayer() {
    return new GridBoardDisplayer(this);
  }

  // TODO: Make public ?
  private boolean isPlayable(GridCoordinates coordinates) {
    if (this.cards.size() == 0) return true;

    // Musn't be occupied
    if (this.cards.containsKey(coordinates))
      return false;

    // Must be adjacent to an existing card
    if (this.cards.entrySet().stream().noneMatch(entry -> entry.getKey().isAdjacentTo(coordinates)))
      return false;

    // Musn't be out of bound
    int maxX = this.maxX();
    int minX = this.minX();
    int maxY = this.maxY();
    int minY = this.minY();

    int xSize = maxX - minX;
    int ySize = maxY - minY;

    int newXSize = Math.max(xSize, Math.max(maxX - coordinates.x, coordinates.x - minX));
    int newYSize = Math.max(ySize, Math.max(maxY - coordinates.y, coordinates.y - minY));

    if (newXSize > maxLength || newYSize > maxLength) return false;

    if (newXSize > maxWidth && newYSize > maxWidth) return false;

    return true;
  }

  private int maxX() {
    return this.cards.keySet().stream().mapToInt(card -> card.x).max().orElse(0);
  }

  private int minX() {
    return this.cards.keySet().stream().mapToInt(card -> card.x).min().orElse(0);
  }

  private int maxY() {
    return this.cards.keySet().stream().mapToInt(card -> card.y).max().orElse(0);
  }

  private int minY() {
    return this.cards.keySet().stream().mapToInt(card -> card.y).min().orElse(0);
  }

  @Override
  public String toString() {
    return "GridBoard{" + cards + '}';
  }
}

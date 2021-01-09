package shapeup.game.scores;

import shapeup.game.Card;
import shapeup.game.Color;
import shapeup.game.Filledness;
import shapeup.game.Shape;
import shapeup.game.boards.Board;
import shapeup.game.boards.CircleBoard;
import shapeup.game.boards.Coordinates;
import shapeup.game.boards.GridBoard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Score counter implementing the classic Shape Up! rules.
 */
public class NormalScoreCounter implements ScoreCounterVisitor {

  @Override
  public int countGridBoard(GridBoard board, Card victoryCard) {
    var lines = gatherGridboardLines(board);

    return countBoard(board, victoryCard, lines);
  }

  @Override
  public int countCircleBoard(CircleBoard board, Card victoryCard) {
    var lines = gatherCircleBoardLines(board);

    return countBoard(board, victoryCard, lines);
  }

  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private int countBoard(Board board, Card victoryCard, List<List<Coordinates>> lines) {
    var shapeScore = lines.stream()
            .map(line -> countScoreForLine(line, board, victoryCard, Card::getShape, NormalScoreCounter::shapeGroupToScore))
            .reduce(Integer::sum).get();
    var fillednessScore = lines.stream()
            .map(line -> countScoreForLine(line, board, victoryCard, Card::getFilledness, NormalScoreCounter::fillednessGroupToScore))
            .reduce(Integer::sum).get();
    var colorScore = lines.stream()
            .map(line -> countScoreForLine(line, board, victoryCard, Card::getColor, NormalScoreCounter::colorGroupToScore))
            .reduce(Integer::sum).get();

    return shapeScore + fillednessScore + colorScore;
  }

  /**
   * Gathers all the rows and columns into a list of lines.
   *
   * @param board the board whose lines should be retrieved.
   * @return the lines.
   */
  private static List<List<Coordinates>> gatherCircleBoardLines(CircleBoard board) {
    final var firstCircle = new ArrayList<Coordinates>();
    final var secondCircle = new ArrayList<Coordinates>();
    final var centerColumn = new ArrayList<Coordinates>();
    final var centerRow = new ArrayList<Coordinates>();
    // Top-left to bottom-right
    final var diagonalTlToBr = new ArrayList<Coordinates>();
    // Bottom-left to top-right
    final var diagonalBlToTr = new ArrayList<Coordinates>();

    final int maxIdx = CircleBoard.SIZE - 1;

    var occupiedPositions = board.getOccupiedPositions();
    for (var op : occupiedPositions) {
      if (CircleBoard.inFirstCircle(op))
        firstCircle.add(op);

      if (CircleBoard.inSecondCircle(op))
        secondCircle.add(op);

      if (op.getX() == CircleBoard.CENTER)
        centerColumn.add(op);

      if (op.getY() == CircleBoard.CENTER)
        centerRow.add(op);

      if (op.getX() == op.getY())
        diagonalTlToBr.add(op);

      if (maxIdx - op.getX() == op.getY() || op.getX() == maxIdx - op.getY())
        diagonalBlToTr.add(op);
    }

    firstCircle.sort(Comparator.comparingDouble(CircleBoard::angle));
    secondCircle.sort(Comparator.comparingDouble(CircleBoard::angle));
    centerColumn.sort(Comparator.comparingInt(Coordinates::getY));
    centerRow.sort(Comparator.comparingInt(Coordinates::getX));
    diagonalBlToTr.sort(Comparator.comparingInt(Coordinates::getX));
    diagonalTlToBr.sort(Comparator.comparingInt(Coordinates::getX));

    return List.of(
            firstCircle,
            secondCircle,
            centerColumn,
            centerRow,
            diagonalTlToBr,
            diagonalBlToTr
    );
  }

  /**
   * Gathers all the rows and columns into a list of lines.
   *
   * @param board the board whose lines should be retrieved.
   * @return the lines.
   */
  private static List<List<Coordinates>> gatherGridboardLines(GridBoard board) {
    List<List<Coordinates>> lines = new ArrayList<>();

    int maxX = board.maxX();
    int minX = board.minX();

    for (int col = minX; col <= maxX; ++col) {
      final int thisColIdx = col;
      var thisColumn = board
              .getOccupiedPositions().stream()
              .filter(coord -> coord.getX() == thisColIdx)
              .sorted(Comparator.comparingInt(Coordinates::getY));
      lines.add(thisColumn.collect(Collectors.toList()));
    }

    int maxY = board.maxY();
    int minY = board.minY();

    for (int row = minY; row <= maxY; ++row) {
      final int thisRowIdx = row;
      var thisRow = board
              .getOccupiedPositions().stream()
              .filter(coord -> coord.getY() == thisRowIdx)
              .sorted(Comparator.comparingInt(Coordinates::getX));
      lines.add(thisRow.collect(Collectors.toList()));
    }

    return lines;
  }

  /**
   * Count the score for the specified line of cards, victory card, and card attribute.
   *
   * @param line         the cards that should be counted.
   * @param board        the board the cards are part of, used for coord -> card and its adjacency impl.
   * @param victoryCard  the victory card.
   * @param getter       used to retrieve the attribute to be compared in each card.
   * @param groupToScore used to convert the size of a card group to a score.
   * @return the score for this line.
   */
  @SuppressWarnings("OptionalGetWithoutIsPresent")
  private static int countScoreForLine(
          List<Coordinates> line,
          Board board,
          Card victoryCard,
          Function<Card, Object> getter,
          Function<Integer, Integer> groupToScore
  ) {
    var streaks = new ArrayList<Integer>();

    streaks.add(1);
    for (int i = 1; i < line.size(); i++) {
      var lastCoord = line.get(i - 1);
      var currentCoord = line.get(i);

      var lastCard = board.getCard(lastCoord).get();
      var currentCard = board.getCard(currentCoord).get();

      var isPartOfLastStreak =
              board.areAdjacent(lastCoord, currentCoord)
                      && getter.apply(lastCard).equals(getter.apply(currentCard))
                      && getter.apply(currentCard).equals(getter.apply(victoryCard));

      if (isPartOfLastStreak) {
        var lastStreak = streaks.remove(streaks.size() - 1);
        streaks.add(lastStreak + 1);
      } else {
        streaks.add(1);
      }
    }

    return streaks.stream()
            .map(groupToScore)
            .reduce(Integer::sum)
            .get();
  }

  private static int shapeGroupToScore(int shapeGroup) {
    switch (shapeGroup) {
      case 2:
        return 1;
      case 3:
        return 2;
      case 4:
        return 3;
      case 5:
        return 4;
      default:
        return 0;
    }
  }

  private static int fillednessGroupToScore(int shapeGroup) {
    switch (shapeGroup) {
      case 3:
        return 3;
      case 4:
        return 4;
      case 5:
        return 5;
      default:
        return 0;
    }
  }

  private static int colorGroupToScore(int shapeGroup) {
    switch (shapeGroup) {
      case 3:
        return 4;
      case 4:
        return 5;
      case 5:
        return 6;
      default:
        return 0;
    }
  }
}

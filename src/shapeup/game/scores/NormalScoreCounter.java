package shapeup.game.scores;

import shapeup.game.Card;
import shapeup.game.Color;
import shapeup.game.Filledness;
import shapeup.game.Shape;
import shapeup.game.boards.Coordinates;
import shapeup.game.boards.GridBoard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public class NormalScoreCounter implements ScoreCounterVisitor {
  public static void main(String[] args) {
    var board = new GridBoard();

    board.playCard(new Card(Color.RED, Shape.CIRCLE, Filledness.HOLLOW), new Coordinates(0, 0));
    board.playCard(new Card(Color.RED, Shape.SQUARE, Filledness.FILLED), new Coordinates(1, 0));
    board.playCard(new Card(Color.RED, Shape.TRIANGLE, Filledness.FILLED), new Coordinates(2, 0));

    board.playCard(new Card(Color.GREEN, Shape.CIRCLE, Filledness.FILLED), new Coordinates(1, 1));

    board.playCard(new Card(Color.BLUE, Shape.CIRCLE, Filledness.FILLED), new Coordinates(1, 2));

    board.playCard(new Card(Color.RED, Shape.CIRCLE, Filledness.HOLLOW), new Coordinates(1, 3));
    board.playCard(new Card(Color.BLUE, Shape.CIRCLE, Filledness.HOLLOW), new Coordinates(0, 3));
    board.playCard(new Card(Color.BLUE, Shape.TRIANGLE, Filledness.HOLLOW), new Coordinates(2, 3));

    var victoryCard1 = new Card(Color.RED, Shape.CIRCLE, Filledness.FILLED);
    var victoryCard2 = new Card(Color.BLUE, Shape.TRIANGLE, Filledness.HOLLOW);

    var score1 = board.acceptScoreCounter(new NormalScoreCounter(), victoryCard1);
    var score2 = board.acceptScoreCounter(new NormalScoreCounter(), victoryCard2);

    System.out.println(score1 + " " + score2);
    assert score1 == 13;
    assert score2 == 3;
  }

  @Override
  public int countGridBoard(GridBoard board, Card victoryCard) {
    var lines = gatherLines(board);

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

  private static List<List<Coordinates>> gatherLines(GridBoard board) {
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

  private static int countScoreForLine(List<Coordinates> line, GridBoard board, Card victoryCard, Function<Card, Object> getter, Function<Integer, Integer> groupToScore) {
    var streaks = new ArrayList<Integer>();

    streaks.add(1);
    for (int i = 1; i < line.size(); i++) {
      var lastCoord = line.get(i - 1);
      var currentCoord = line.get(i);

      var lastCard = board.getCard(lastCoord).get();
      var currentCard = board.getCard(currentCoord).get();

      var isPartOfLastStreak =
              lastCoord.isAdjacentTo(currentCoord)
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
    return switch (shapeGroup) {
      case 2 -> 1;
      case 3 -> 2;
      case 4 -> 3;
      case 5 -> 4;
      default -> 0;
    };
  }

  private static int fillednessGroupToScore(int shapeGroup) {
    return switch (shapeGroup) {
      case 3 -> 3;
      case 4 -> 4;
      case 5 -> 5;
      default -> 0;
    };
  }

  private static int colorGroupToScore(int shapeGroup) {
    return switch (shapeGroup) {
      case 3 -> 4;
      case 4 -> 5;
      case 5 -> 6;
      default -> 0;
    };
  }
}

package shapeup.ui;

import shapeup.game.boards.Board;
import shapeup.game.boards.Coordinates;

import javax.swing.*;
import java.util.HashSet;

public class BoardDisplayer {
  private final Integer xSize;
  private final Integer ySize;

  public BoardDisplayer(Board board) {
    this.xSize = null;
    this.ySize = null;
  }

  public BoardDisplayer(Board board, int xSize, int ySize) {
    this.xSize = xSize;
    this.ySize = ySize;
  }

  public void terminalDisplay(Board board) {
    var occupiedPositions = board.getOccupiedPositions();
    var playablePositions = board.getPlayablePositions();

    var allPositions = new HashSet<>(occupiedPositions);
    allPositions.addAll(playablePositions);

    int minX, maxX, minY, maxY;
    if (xSize == null || ySize == null) {
      // SAFETY: allPositions can't be empty
      minX = allPositions.stream().mapToInt(Coordinates::getX).min().getAsInt();
      maxX = allPositions.stream().mapToInt(Coordinates::getX).max().getAsInt();
      minY = allPositions.stream().mapToInt(Coordinates::getY).min().getAsInt();
      maxY = allPositions.stream().mapToInt(Coordinates::getY).max().getAsInt();
    } else {
      minX = 0;
      maxX = this.xSize - 1;
      minY = 0;
      maxY = this.ySize - 1;
    }

    // First row : coordinates
    System.out.print("|y\\x");
    for (int col = minX; col <= maxX; col++)
      System.out.printf("|%3d", col);
    System.out.println("|");

    for (int row = minY; row <= maxY; row++) {
      // First col : coordinates
      System.out.printf("|%3d", row);

      for (int col = minX; col <= maxX; col++) {
        var coord = new Coordinates(col, row);

        if (occupiedPositions.contains(coord)) {
          var card = board.getCard(coord).get();
          System.out.print("| " + TerminalUI.fancyCardString(card));
        } else if (playablePositions.contains(coord)) {
          System.out.print("|< >");
        } else {
          System.out.print("|   ");
        }
      }
      System.out.println('|');
    }
  }

  public void graphicalDisplay(JComponent component) {
  }
}

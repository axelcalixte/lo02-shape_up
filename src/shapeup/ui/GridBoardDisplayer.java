package shapeup.ui;

import shapeup.game.boards.GridBoard;
import shapeup.game.boards.GridCoordinates;

import javax.swing.*;
import java.util.HashSet;

public class GridBoardDisplayer implements BoardDisplayer {
  private final GridBoard board;

  public GridBoardDisplayer(GridBoard board) {
    this.board = board;
  }

  @Override
  public void terminalDisplay() {
    var occupiedPositions = this.board.getOccupiedPositions();
    var playablePositions = this.board.getPlayablePositions();

    var allPositions = new HashSet<>(occupiedPositions);
    allPositions.addAll(playablePositions);

    // SAFETY: allPositions can't be empty
    int minX = allPositions.stream().mapToInt(GridCoordinates::getX).min().getAsInt();
    int maxX = allPositions.stream().mapToInt(GridCoordinates::getX).max().getAsInt();
    int minY = allPositions.stream().mapToInt(GridCoordinates::getY).min().getAsInt();
    int maxY = allPositions.stream().mapToInt(GridCoordinates::getY).max().getAsInt();

    for (int row = minY; row <= maxY; row++) {
      for (int col = minX; col <= maxX; col++) {
        var coord = new GridCoordinates(col, row);

        if (occupiedPositions.contains(coord)) {
          var card = this.board.getCard(coord).get();
          System.out.print("|" + TerminalUI.fancyCardString(card));
        } else if (playablePositions.contains(coord)) {
          System.out.print("|<>");
        } else {
          System.out.print("|  ");
        }
      }
      System.out.println('|');
    }
  }

  @Override
  public void graphicalDisplay(JComponent component) {
  }
}

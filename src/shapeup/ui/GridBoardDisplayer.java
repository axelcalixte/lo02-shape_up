package shapeup.ui;

import shapeup.game.boards.GridBoard;

import javax.swing.*;

public class GridBoardDisplayer implements BoardDisplayer {
  private final GridBoard board;

  public GridBoardDisplayer(GridBoard board) {
    this.board = board;
  }

  @Override
  public void terminalDisplay() {
    System.out.println(board.toString());
  }

  @Override
  public void graphicalDisplay(JComponent component) {
  }
}

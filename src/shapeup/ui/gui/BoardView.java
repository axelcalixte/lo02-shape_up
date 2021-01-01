package shapeup.ui.gui;

import shapeup.game.boards.Board;
import shapeup.game.boards.Coordinates;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Collection;
import java.util.HashMap;
import java.util.function.Consumer;

public class BoardView extends JPanel {
  private Board board;
  private boolean displayPlayable;
  private Coordinates displayMovableFrom;

  private final HashMap<Coordinates, JPanel> cardViews;
  private final HashMap<Coordinates, JPanel> playableViews;
  private final HashMap<Coordinates, JPanel> movableViews;
  private final HashMap<Coordinates, JPanel> emptySquareViews;

  public BoardView() {
    super();
    setBackground(Color.BLACK);
    setPreferredSize(new Dimension(700, 700));
    board = null;
    cardViews = new HashMap<>();
    playableViews = new HashMap<>();
    movableViews = new HashMap<>();
    emptySquareViews = new HashMap<>();
  }

  public void setDisplayPlayable(boolean displayPlayable) {
    this.displayPlayable = displayPlayable;
    render();
  }

  public void setDisplayMovableFrom(Coordinates displayMovableFrom) {
    this.displayMovableFrom = displayMovableFrom;
    render();
  }

  public void update(Board board) {
    this.board = board;
    render();
  }

  private void render() {
    if (board == null) {
      return;
    }

    int minXSquare = board.displayMinX();
    int maxXSquare = board.displayMaxX();
    int minYSquare = board.displayMinY();
    int maxYSquare = board.displayMaxY();

    int xSquares = maxXSquare - minXSquare + 1;
    int ySquares = maxYSquare - minXSquare + 1;

    var occupiedPositions = board.getOccupiedPositions();
    var playablePositions = displayPlayable ? board.getPlayablePositions() : null;
    var movablePositions = displayMovableFrom != null ? board.getMovablePositions(displayMovableFrom) : null;

    removeAll();
    cardViews.clear();
    emptySquareViews.clear();
    movableViews.clear();
    playableViews.clear();
    setLayout(new GridLayout(ySquares, xSquares));

    for (int y = minYSquare; y <= maxYSquare; y++) {
      for (int x = minXSquare; x <= maxXSquare; x++) {
        var currentSquare = new Coordinates(x, y);

        JPanel squareView;
        if (occupiedPositions.contains(currentSquare)) {
          squareView = new CardView(board.getCard(currentSquare).get());
          cardViews.put(currentSquare, squareView);
        } else if (playablePositions != null && playablePositions.contains(currentSquare)) {
          squareView = new JPanel();
          squareView.setBackground(Color.BLUE);
          playableViews.put(currentSquare, squareView);
        } else if (movablePositions != null && movablePositions.contains(currentSquare)) {
          squareView = new JPanel();
          squareView.setBackground(Color.RED);
          movableViews.put(currentSquare, squareView);
        } else {
          squareView = new JPanel();
          squareView.setBackground(Color.BLACK);
          emptySquareViews.put(currentSquare, squareView);
        }
        add(squareView);
      }
    }

  }

  public void setOneShotCardListener(Consumer<Coordinates> listener) {
    setOneShotMouseListeners(cardViews, listener);
  }

  public void removeCardListener() {
    removeMouseListeners(cardViews.values());
  }

  public void setOneShotPlayableListener(Consumer<Coordinates> listener) {
    setOneShotMouseListeners(playableViews, listener);
  }

  public void removePlayableListener() {
    removeMouseListeners(playableViews.values());
  }

  public void setOneShotMovableListener(Consumer<Coordinates> listener) {
    setOneShotMouseListeners(movableViews, listener);
  }

  public void removeMovableListener() {
    removeMouseListeners(movableViews.values());
  }

  private static void setOneShotMouseListeners(HashMap<Coordinates, JPanel> panels, Consumer<Coordinates> listener) {
    for (var square : panels.entrySet()) {
      var coord = square.getKey();
      square.getValue().addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          removeMouseListeners(panels.values());
          listener.accept(coord);
        }
      });
    }
  }

  private static void removeMouseListeners(Collection<JPanel> panels) {
    for (var p : panels)
      for (var ml : p.getMouseListeners())
        p.removeMouseListener(ml);
  }
}

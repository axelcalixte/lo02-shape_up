package shapeup.game.scores;

import shapeup.game.Card;
import shapeup.game.boards.CircleBoard;
import shapeup.game.boards.GridBoard;

public interface ScoreCounterVisitor {
  int countGridBoard(GridBoard board, Card victoryCard);

  int countCircleBoard(CircleBoard board, Card victoryCard);
}

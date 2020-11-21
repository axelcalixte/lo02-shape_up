package shapeup.game.scores;

import shapeup.game.Card;
import shapeup.game.boards.GridBoard;

public interface ScoreCounterVisitor {
  int countGridBoard(GridBoard board, Card victoryCard);
  // int countCircleBoard(GridBoard board, Card victoryCard);
}

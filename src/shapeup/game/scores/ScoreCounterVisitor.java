package shapeup.game.scores;

import shapeup.game.Card;
import shapeup.game.boards.GridBoard;

public interface ScoreCounterVisitor {
  int countGridBoard(GridBoard board, Card victoryCard);
  // Map<Card, Integer> countCircularBoard(Card[] victoryCards);
}

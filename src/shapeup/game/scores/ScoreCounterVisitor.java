package shapeup.game.scores;

import shapeup.game.Card;
import shapeup.game.boards.GridBoard;

import java.util.Map;

public interface ScoreCounterVisitor {
  Map<Card, Integer> countGridBoard(GridBoard board, Card[] victoryCards);
  // Map<Card, Integer> countCircularBoard(Card[] victoryCards);
}

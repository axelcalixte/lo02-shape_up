package shapeup.game.boards;

import shapeup.game.Card;
import shapeup.game.scores.ScoreCounterVisitor;
import shapeup.ui.BoardDisplayer;

import java.util.Optional;
import java.util.Set;

public interface Board {
  void playCard(Card card, Coordinates coordinates);

  void moveCard(Coordinates from, Coordinates to);

  Optional<Card> getCard(Coordinates coordinates);

  Set<Coordinates> getPlayablePositions();

  Set<Coordinates> getMovablePositions(Coordinates from);

  Set<Coordinates> getOccupiedPositions();

  int acceptScoreCounter(ScoreCounterVisitor scoreCounter, Card victoryCard);

  boolean areAdjacent(Coordinates a, Coordinates b);

  BoardDisplayer displayer();
}

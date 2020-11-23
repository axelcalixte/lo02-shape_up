package shapeup.ui;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;

import java.util.List;
import java.util.function.BiConsumer;

public interface UI {
  /**
   * A change happened in the game state and the UI should reflect it.
   */
  void update(GameState gs);

  void moveOrPlay(int playerID, BiConsumer<Card, Coordinates> onPlay, BiConsumer<Coordinates, Coordinates> onMove);

  void play(int playerID, BiConsumer<Card, Coordinates> onPlay);

  void canFinishTurn(int playerID, Runnable onFinish, BiConsumer<Coordinates, Coordinates> onMove);

  void turnFinished(int playerID, Runnable onFinish);

  void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish);
}

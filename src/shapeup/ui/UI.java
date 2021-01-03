package shapeup.ui;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface UI {
  /**
   * A change happened in the game state and the UI should reflect it.
   */
  void update(GameState gs);

  void moveOrPlay(int playerID, BiConsumer<Coordinates, Coordinates> onMove, BiConsumer<Coordinates, Card> onPlay);

  void play(int playerID, BiConsumer<Card, Coordinates> onPlay);

  void move(int playerID, BiConsumer<Coordinates, Coordinates> move);

  void canFinishTurn(int playerID, Consumer<Boolean> onShouldFinish);

  void turnFinished(int playerID, Runnable onFinishConfirmed);

  void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish);

  void gameFinished(List<Integer> scores, Runnable onFinish);
}

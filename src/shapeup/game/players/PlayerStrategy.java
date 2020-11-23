package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;

import java.util.List;
import java.util.function.BiConsumer;

public interface PlayerStrategy {
  void update(GameState up);
  int getPlayerID();

  void canMoveOrPlay(BiConsumer<Card, Coordinates> onPlay, BiConsumer<Coordinates, Coordinates> onMove);

  void canPlay(BiConsumer<Card, Coordinates> onPlay);

  void canFinishTurn(Runnable finish, BiConsumer<Coordinates, Coordinates> proceed);

  void turnFinished(Runnable onFinish);

  void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish);
}

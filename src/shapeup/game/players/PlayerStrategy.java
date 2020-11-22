package shapeup.game.players;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;

import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public interface PlayerStrategy {
  void update(GameState up);
  int getPlayerID();

  void canMoveOrPlay(Consumer<Coordinates> onPlay, BiConsumer<Coordinates, Coordinates> onMove);

  void canPlay(Consumer<Coordinates> onPlay);

  void canFinishTurn(Runnable finish, BiConsumer<Coordinates, Coordinates> continue_);

  void turnFinished(Runnable onFinish);

  void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish);
}

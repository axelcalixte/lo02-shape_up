package shapeup.ui;

import shapeup.game.Action;
import shapeup.game.GameState;

import java.util.LinkedHashMap;

public interface UI {
  /**
   * A change happened in the game state and the UI should reflect it.
   */
  void update(GameState gs);

  Action askPlayersAction(int playerID, BoardDisplayer bd, Action[] possibleActions);

  void victoryScreen(int winner, LinkedHashMap<Integer, Integer> scores);
}

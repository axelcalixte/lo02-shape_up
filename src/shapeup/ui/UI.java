package shapeup.ui;

import shapeup.game.Action;
import shapeup.game.Deck;
import shapeup.game.PlayerState;

import java.util.LinkedHashMap;

public interface UI {
  /**
   * A change happened in the game state and the UI should reflect it.
   */
  void notifyOfGameStateChange();

  Action askPlayersAction(int playerID, BoardDisplayer boardDisplayer, PlayerState[] pss, Deck deck, Action[] possibleActions);

  void victoryScreen(int winner, LinkedHashMap<Integer, Integer> scores);
}

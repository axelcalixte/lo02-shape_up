package shapeup.game;

import shapeup.game.boards.GridBoard;
import shapeup.ui.BoardDisplayer;
import shapeup.ui.GridBoardDisplayer;
import shapeup.ui.UI;

import java.util.Arrays;

public final class GameController {
  private final UI ui;

  private final PlayerState[] playerStates;
  private final GridBoard board;
  private final BoardDisplayer boardDisplayer;
  private final Deck deck;

  public GameController(UI ui) {
    this.ui = ui;
    this.playerStates = new PlayerState[]{
            new PlayerState(0),
            new PlayerState(1),
    };

    this.board = new GridBoard();
    this.boardDisplayer = new GridBoardDisplayer(this.board);
    this.deck = new Deck();
  }

  public void startGame() {
    boolean keepPlaying;
    do {
      keepPlaying = this.gameTurn();
    } while (keepPlaying);
  }

  /**
   * Play out an entire game turn, including all players.
   *
   * @return whether there should be another turn.
   */
  private boolean gameTurn() {
    for (var player : this.playerStates) {
      this.playerTurn(player.getPlayerID());
    }
    return deck.cardsLeft() == 0 && Arrays.stream(this.playerStates).allMatch(ps -> ps.getHand().size() <= 1);
  }

  /**
   * Play out a player's turn.
   *
   * @param playerID their ID
   */
  private void playerTurn(int playerID) {
    ui.askPlayersAction(playerID, this.boardDisplayer, this.playerStates, this.deck, new Action[]{
    });
  }
}

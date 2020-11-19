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
    this.updateUI();
  }

  public void updateUI() {
    this.ui.update(new GameState(playerStates, board, deck));
  }

  public void startGame() {
    this.deck.drawCard();
    System.out.println("La carte cachée a été retirée de la pile.\n");

    for (PlayerState ps : this.playerStates) {
      ps.giveVictoryCard(this.deck.drawCard().get());
      System.out.println("La carte victoire du joueur n°" + ps.getPlayerID() + " a été attribuée.\n");
    }

    //TODO: let the players choose who starts

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
    var possibleActions = new Action[]{
            new Action() {
              public String name() {
                return "Placer une carte sur le plateau";
              }

              public void run() {
                //TODO: Do you want to move a card before placing your card ?
                //TODO: Move a card already on the board
                //TODO: Choose the card you want to play
                //TODO: Where do you want to place the card
                //TODO: Place the card
              }
            },

            new Action() {
              public String name() {
                return "Consulter sa carte victoire";
              }

              public void run() {
                for (PlayerState ps : playerStates) {
                  if (playerID == ps.getPlayerID()) {
                    System.out.println("Votre carte victoire :\n" + ps.getVictoryCard());
                  }
                }
              }
            }
    };
    this.updateUI();
    var action = ui.askPlayersAction(playerID, this.boardDisplayer, possibleActions);
    action.run();
  }
}

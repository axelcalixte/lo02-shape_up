package shapeup.game;

import shapeup.game.boards.GridBoard;
import shapeup.game.boards.GridCoordinates;
import shapeup.ui.*;

import java.util.Arrays;
import java.util.Scanner;

public final class GameController {
  private final UI ui;

  private final PlayerState[] playerStates;
  private final GridBoard board;
  private final BoardDisplayer boardDisplayer;
  private final Deck deck;

  private GridCoordinates from;
  private GridCoordinates to;
  private Card cardToBePlayed;

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
    var that = this;
    var possibleActions = new Action[]{
            new Action() {
              public String name() {
                return "Déplacer une carte du plateau";
              }

              public void run() {
                that.moveCard(playerID, false);
              }
            },

            new Action() {
              public String name() {
                return "Placer une carte sur le plateau";
              }

              public void run() {
                that.playCard(playerID, false);
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

  /**
   * Moves a card on a the deck
   *
   * @param playerID
   * @param alreadyPlayedCard
   */
  private void moveCard(int playerID, boolean alreadyPlayedCard) {
    from = new GridCoordinates(-1, -1);
    to = new GridCoordinates(-1, -1);
    var possibleActions = new Action[]{
            new Action() {
              public String name() {
                return "Choisir une place occupée par une carte";
              }

              public void run() {
                var occupied = board.getOccupiedPositions();
                while (!occupied.contains(from)) {
                  for (GridCoordinates gc : occupied) {
                    System.out.println(gc);
                  }
                  int x = (new Scanner(System.in)).nextInt();
                  int y = (new Scanner(System.in)).nextInt();
                  from = new GridCoordinates(x, y);
                }
              }
            },

            new Action() {
              public String name() {
                return "Choisir une place libre du plateau";
              }

              public void run() {
                var playable = board.getPlayablePositions();
                while (!playable.contains(to)) {
                  for (GridCoordinates gc : playable) {
                    System.out.println("gc");
                  }
                  int x = (new Scanner(System.in)).nextInt();
                  int y = (new Scanner(System.in)).nextInt();
                  to = new GridCoordinates(x, y);
                }

              }
            },

            new Action() {
              public String name() {
                return "Bouger la carte sélectionnée";
              }

              public void run() {
                board.moveCard(from, to);
              }
            }
    };
    this.updateUI();
    var action = ui.askPlayersAction(playerID, this.boardDisplayer, possibleActions);
    action.run();

    if (!alreadyPlayedCard) {
      this.playCard(playerID, true);
    }
  }

  /**
   * Play a card on the deck
   *
   * @param playerID
   * @param alreadyMovedCard
   */
  private void playCard(int playerID, boolean alreadyMovedCard) {
    var possibleActions = new Action[]{
            new Action() {
              public String name() {
                return "Choisir une carte à jouer";
              }

              public void run() {
                for (PlayerState ps : playerStates) {
                  if (playerID == ps.getPlayerID()) {
                    while (ps.getHand().) {
                      for (Card ca : ps.getHand()) {
                        System.out.println(ca);
                      }
                      Color cl;
                      while (cl != "GREEN" || cl != "BLUE" || cl != "RED") {
                        System.out.print("Choisir la couleur de votre carte à placer");
                      }
                      Shape sh;
                      Filledness fd;
                      cardToBePlayed = new Card(cl, sh, fd);
                    }
                  }
                }
              }
            },

            new Action() {
              public String name() {
                return "Choisir une place libre du plateau";
              }

              public void run() {
                var playable = board.getPlayablePositions();
                while (!playable.contains(to)) {
                  for (GridCoordinates gc : playable) {
                    System.out.println("gc");
                  }
                  int x = (new Scanner(System.in)).nextInt();
                  int y = (new Scanner(System.in)).nextInt();
                  to = new GridCoordinates(x, y);
                }

              }
            },

            new Action() {
              public String name() {
                return "Jouer la carte sur le plateau";
              }

              public void run() {
                board.playCard(cardToBePlayed, to);
              }
            }
    };

    this.updateUI();
    var action = ui.askPlayersAction(playerID, this.boardDisplayer, possibleActions);
    action.run();

    if (!alreadyMovedCard) {
      this.moveCard(playerID, true);
    }
  }
}

package shapeup;

import shapeup.game.Game;
import shapeup.game.SupplierAction;
import shapeup.game.boards.Board;
import shapeup.game.boards.CircleBoard;
import shapeup.game.boards.GridBoard;
import shapeup.game.players.PlayerType;
import shapeup.ui.UIType;
import shapeup.ui.tui.TUIMenu;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.function.Supplier;


/**
 * The ShapeUp class holds the main method which starts the program.
 */
public class ShapeUp {
  public static void main(String[] args) {
    var playerTypes = new ArrayList<PlayerType>(3);
    var uiTypes = new ArrayList<UIType>(3);
    for (int i = 0; i < 2; i++) {
      var playerType = playerType(i);
      playerTypes.add(playerType);
      if (playerType == PlayerType.REAL_PLAYER) uiTypes.add(uiType(i));
      else uiTypes.add(UIType.TUI);
    }
    if (thirdPlayer()) {
      var playerType = playerType(2);
      playerTypes.add(playerType);
      if (playerType == PlayerType.REAL_PLAYER) uiTypes.add(uiType(2));
      else uiTypes.add(UIType.TUI);
    }

    var boardCtor = boardType();
    boolean advanced = advancedShapeUp();
    int nbRounds = numberOfRounds();

    new Game(boardCtor, playerTypes, uiTypes, advanced).startGame(nbRounds);
  }

  /**
   * Asks for a player's choice of interface to play on.
   *
   * @param playerID - an int representing a player.
   * @return the choice of interface for the player.
   */
  public static UIType uiType(int playerID) {
    return TUIMenu.displayValueMenu(
            String.format("Shape Up! Choisissez le type d'interface pour le joueur %d :", playerID),
            List.of(
                    new SupplierAction<>() {
                      public String name() {
                        return "Ligne de commande";
                      }

                      public UIType get() {
                        return UIType.TUI;
                      }
                    },
                    new SupplierAction<>() {
                      public String name() {
                        return "Graphique";
                      }

                      public UIType get() {
                        return UIType.GUI;
                      }
                    }
            )
    );
  }

  /**
   * Asks for a player's type.
   *
   * @param playerID - an int representing a player
   * @return the PlayerType
   */
  public static PlayerType playerType(int playerID) {
    return TUIMenu.displayValueMenu(
            String.format("Choisissez le type de joueur pour le joueur %d :", playerID),
            List.of(
                    new SupplierAction<>() {
                      public String name() {
                        return "Joueur réel";
                      }

                      public PlayerType get() {
                        return PlayerType.REAL_PLAYER;
                      }
                    },
                    new SupplierAction<>() {
                      public String name() {
                        return "IA basique";
                      }

                      public PlayerType get() {
                        return PlayerType.BASIC_AI;
                      }
                    }
            )
    );
  }

  /**
   * Asks if the game needs to include a third player.
   *
   * @return true if a third player is included; false otherwise.
   */
  public static boolean thirdPlayer() {
    return TUIMenu.displayValueMenu(
            "Troisième joueur ?",
            List.of(
                    new SupplierAction<>() {
                      public String name() {
                        return "Oui";
                      }

                      public Boolean get() {
                        return true;
                      }
                    },
                    new SupplierAction<>() {
                      public String name() {
                        return "Non";
                      }

                      public Boolean get() {
                        return false;
                      }
                    }
            )
    );
  }

  /**
   * Asks for the board type to be played on during the entire game.
   *
   * @return a new instance of a GridBoard or CircleBoard.
   */
  public static Supplier<Board> boardType() {
    return TUIMenu.displayValueMenu(
            "Type de plateau :",
            List.of(
                    new SupplierAction<>() {
                      public String name() {
                        return "Classique";
                      }

                      public Supplier<Board> get() {
                        return GridBoard::new;
                      }
                    },
                    new SupplierAction<>() {
                      public String name() {
                        return "Circulaire";
                      }

                      public Supplier<Board> get() {
                        return CircleBoard::new;
                      }
                    }
            )
    );
  }

  /**
   * Asks for the use of advanced Shape Up! game rules.
   *
   * @return true if the rules are to be used; false if not.
   */
  public static boolean advancedShapeUp() {
    return TUIMenu.displayValueMenu(
            "Utiliser les règles Advanced Shape Up! ?",
            List.of(
                    new SupplierAction<>() {
                      public String name() {
                        return "Oui";
                      }

                      public Boolean get() {
                        return true;
                      }
                    },
                    new SupplierAction<>() {
                      public String name() {
                        return "Non";
                      }

                      public Boolean get() {
                        return false;
                      }
                    }
            )
    );
  }

  /**
   * Asks for the number of rounds for a Shape Up! game.
   *
   * @return an int representing the number of rounds to be played.
   */
  public static int numberOfRounds() {
    System.out.print("Entrez le nombre de rounds de cette partie : ");
    while (true) {
      try {
        int rounds = new Scanner(System.in).nextInt();
        if (rounds > 0) return rounds;
      } catch (Exception ignored) {
      }
      System.out.println("Entrez un entier supérieur à 0 : ");
    }
  }
}

package shapeup;

import shapeup.game.Game;
import shapeup.game.SupplierAction;
import shapeup.game.boards.Board;
import shapeup.game.boards.CircleBoard;
import shapeup.game.boards.GridBoard;
import shapeup.game.players.PlayerType;
import shapeup.ui.UI;
import shapeup.ui.gui.GraphicalUI;
import shapeup.ui.tui.TUIMenu;
import shapeup.ui.tui.TerminalUI;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ShapeUp {
  public static void main(String[] args) {
    var uiCtor = uiType();

    var playerTypes = new ArrayList<PlayerType>(3);
    for (int i = 0; i < 2; i++)
      playerTypes.add(playerType(i));
    if (thirdPlayer())
      playerTypes.add(playerType(2));

    var boardCtor = boardType();

    boolean advanced = advancedShapeUp();

    new Game(uiCtor, boardCtor, playerTypes, advanced).startGame();
//    new Game(GraphicalUI::new, GridBoard::new, List.of(PlayerType.REAL_PLAYER, PlayerType.REAL_PLAYER), false).startGame();
  }

  public static Supplier<UI> uiType() {
    return TUIMenu.displayValueMenu(
            "Shape Up! Choisissez le type d'interface :",
            List.of(
                    new SupplierAction<>() {
                      public String name() {
                        return "Ligne de commande";
                      }

                      public Supplier<UI> get() {
                        return TerminalUI::new;
                      }
                    },
                    new SupplierAction<>() {
                      public String name() {
                        return "Graphique";
                      }

                      public Supplier<UI> get() {
                        return GraphicalUI::new;
                      }
                    }
            )
    );
  }

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
}

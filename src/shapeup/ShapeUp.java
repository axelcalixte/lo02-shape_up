package shapeup;

import shapeup.game.GameController;
import shapeup.game.MenuAction;
import shapeup.game.boards.CircleBoard;
import shapeup.game.boards.GridBoard;
import shapeup.ui.TUIMenu;
import shapeup.ui.TerminalUI;

import java.util.List;

public class ShapeUp {
  public static void main(String[] args) {
    mainMenu().run();
  }

  public static MenuAction mainMenu() {
    return TUIMenu.displayMenu(
            "Shape Up !",
            List.of(new MenuAction() {
              public String name() {
                return "Commencer une partie à 2 joueurs, règles classiques, plateau classique.";
              }

              public void run() {
                new GameController(TerminalUI::new, GridBoard::new, false).startGame();
              }
            }, new MenuAction() {
              public String name() {
                return "Commencer une partie à 1 joueur contre une IA, règles classiques, plateau classique.";
              }

              public void run() {
                new GameController(TerminalUI::new, GridBoard::new, true).startGame();
              }
            }, new MenuAction() {
              public String name() {
                return "Commencer une partie à 2 joueurs, règles classiques, plateau circulaire.";
              }

              public void run() {
                new GameController(TerminalUI::new, CircleBoard::new, false).startGame();
              }
            }, /* hella bugged new MenuAction() {
              public String name() {
                return "Commencer une partie à 1 joueur contre une IA, règles classiques, plateau circulaire.";
              }

              public void run() {
                new GameController(TerminalUI::new, CircleBoard::new, true).startGame();
              }
            },*/ new MenuAction() {
              public String name() {
                return "Quitter";
              }

              public void run() {
              }
            })
    );
  }
}

package shapeup;

import shapeup.game.GameController;
import shapeup.game.MenuAction;
import shapeup.ui.TUIMenu;
import shapeup.ui.TerminalUI;

import java.util.Arrays;

public class ShapeUp {
  public static void main(String[] args) {
    mainMenu().run();
  }

  public static MenuAction mainMenu() {
    return TUIMenu.displayMenu(
            "Shape Up !",
            Arrays.asList(
                    new MenuAction() {
                      public String name() {
                        return "Commencer une partie à 2 joueurs, règles classiques.";
                      }

                      public void run() {
                        new GameController(TerminalUI::new).startGame();
                      }
                    },
                    new MenuAction() {
                      public String name() {
                        return "Quitter";
                      }

                      public void run() {
                      }
                    }
            )
    );
  }
}

package shapeup;

import shapeup.game.Action;
import shapeup.game.GameController;
import shapeup.ui.TUIMenu;
import shapeup.ui.TerminalUI;

public class ShapeUp {
  public static void main(String[] args) {
    mainMenu().run();
  }

  public static Action mainMenu() {
    return TUIMenu.displayMenu("Shape Up !",
            new Action[]{
                    new Action() {
                      public String name() {
                        return "Commencer une partie à 2 joueurs, règles classiques.";
                      }

                      public void run() {
                        new GameController(new TerminalUI()).startGame();
                      }
                    },

                    new Action() {
                      public String name() {
                        return "Quitter";
                      }

                      public void run() {
                      }
                    }
            });
  }
}

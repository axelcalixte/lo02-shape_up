package shapeup;

import shapeup.ui.TUIMenu;

public class ShapeUp {
  public static void main(String[] args) {
    TUIMenu.displayMenu("Shape Up !",
            new TUIMenu.MenuAction[]{
                    new TUIMenu.MenuAction() {
                      public String name() {
                        return "Commencer une partie à 2 joueurs, règles classiques.";
                      }

                      public void run() {
                        System.out.println("Pas encore implémenté !");
                      }
                    },

                    new TUIMenu.MenuAction() {
                      public String name() {
                        return "Quitter";
                      }

                      public void run() {
                        System.exit(0);
                      }
                    }
            }).run();
  }
}

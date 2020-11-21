package shapeup.ui;

import shapeup.game.MenuAction;

import java.util.List;
import java.util.Scanner;

public class TUIMenu {
  private TUIMenu() {
  }

  /**
   * Displays a menu on stdout.
   *
   * @param title   the menu's title
   * @param actions the actions the user will choose between
   * @return the chosen action
   */
  public static MenuAction displayMenu(String title, List<MenuAction> actions) {
    // Title display
    for (int i = 0; i < title.length(); ++i) {
      System.out.print('-');
    }
    System.out.print('\n');
    System.out.println(title);
    for (int i = 0; i < title.length(); ++i) {
      System.out.print('-');
    }
    System.out.print('\n');

    for (int i = 0; i < actions.size(); ++i) {
      System.out.printf("(%d) - %s\n", i + 1, actions.get(i).name());
    }

    // Strange stuff happens if we close this scanner ¯\_(ツ)_/¯.
    // TODO: actually read documentation.
    var scanner = new Scanner(System.in);
    while (true) {
      if (actions.size() == 1)
        System.out.print("Choisissez 1 : ");
      else
        System.out.printf("Choisissez [%d-%d] : ", 1, actions.size());

      try {
        int choice = scanner.nextInt();
        if (1 <= choice && choice <= actions.size()) {
          return actions.get(choice - 1);
        }
      } catch (Exception ignored) {
      }

      System.out.printf("\nChoix invalide. Doit être un entier entre %d et %d compris.\n", 0, actions.size() - 1);
    }
  }
}

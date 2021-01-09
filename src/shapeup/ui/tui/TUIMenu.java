package shapeup.ui.tui;

import shapeup.game.MenuAction;
import shapeup.game.SupplierAction;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Utility class for displaying menues on the terminal.
 */
public class TUIMenu {
  private TUIMenu() {
  }

  /**
   * Displays a menu on stdout, allowing the user to choose between several actions.
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
    // TODO: actually read the documentation.
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

  /**
   * Displays a menu on stdout, allowing the user to choose between several values.
   *
   * @param title   the menu's title
   * @param actions the actions providing the values the user will choose between
   * @return the chosen value
   */
  public static <T> T displayValueMenu(String title, List<SupplierAction<T>> actions) {
    var t = new Object() {
      T val = null;
    };

    TUIMenu.displayMenu(
            title,
            actions.stream()
                    .map(action ->
                            new MenuAction() {
                              public String name() {
                                return action.name();
                              }

                              public void run() {
                                t.val = action.get();
                              }
                            }
                    )
                    .collect(Collectors.toList())
    ).run();

    return t.val;
  }
}

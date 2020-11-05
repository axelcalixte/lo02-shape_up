package shapeup.ui;

import java.awt.*;
import java.util.LinkedHashMap;
import java.util.Scanner;

public class TUIMenu {
  private TUIMenu() {}

  public interface MenuAction {
    String name();
    void run();
  }

  /**
   * Displays a menu on stdout.
   * @param title the menu's title
   * @param actions the actions the user will choose between
   * @return the chosen action
   */
  public static MenuAction displayMenu(String title, MenuAction[] actions) {
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

    for (int i = 0; i < actions.length; ++i) {
      System.out.printf("- %s (%d)\n", actions[i].name(), i);
    }

    while (true) {
      System.out.printf("Choisissez [%d-%d]: ", 0, actions.length - 1);
      try {
        int choice = (new Scanner(System.in)).nextInt();
        if (0 <= choice && choice < actions.length) {
          return actions[choice];
        }
      } catch (Exception ignored) {
      }

      System.out.printf("\nChoix invalide. Doit être un entier entre %d et %d compris.\n", 0, actions.length - 1);
    }
  }
}

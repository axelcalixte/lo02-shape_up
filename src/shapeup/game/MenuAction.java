package shapeup.game;

/**
 * Action to be run when a menu option is chosen.
 * Can be tought of as a {@link Runnable} with a {@link MenuAction#name}.
 */
public interface MenuAction {
  /**
   * The action's name used for display.
   *
   * @return the name
   */
  String name();

  /**
   * The action.
   */
  void run();
}

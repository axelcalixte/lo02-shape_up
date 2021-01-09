package shapeup.game;

/**
 * Action to be run when a menu option is chosen.
 * Running it returns a value.
 * Can be tought of as a {@link java.util.function.Supplier} with a {@link SupplierAction#name}.
 *
 * @param <T> the type of the returned value
 */
public interface SupplierAction<T> {
  /**
   * The action's name used for display.
   *
   * @return the name
   */
  String name();

  /**
   * The action.
   *
   * @return the value
   */
  T get();
}

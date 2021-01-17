package shapeup.util;

/**
 * A simple generic tuple, used for multiple returns.
 *
 * @param <A> the first element's type
 * @param <B> the second element's type
 */
public class Tuple<A, B> {
  /**
   * The first element. {@code public final} for easy access and immutability.
   */
  public final A a;
  /**
   * The second element. {@code public final} for easy access and immutability.
   */
  public final B b;

  /**
   * Constructs a new tuple.
   *
   * @param a the first element
   * @param b the second element
   */
  public Tuple(A a, B b) {
    this.a = a;
    this.b = b;
  }
}

package shapeup.game;

public interface SupplierAction<T> {
  String name();

  T get();
}

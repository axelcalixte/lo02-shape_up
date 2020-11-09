package shapeup.game.boards;

import shapeup.game.Card;

public class GridBoard implements Board {

  private int sizeOfGridBoard;
  public GridBoard() { }

  @Override
  public void moveCard(GridCoordinates from, GridCoordinates to) {
  to.x = from.x;
  to.y = from.y;
  }

  @Override
  public void playCard(Card card, GridCoordinates coordinates) {

  }

  @Override
  public GridCoordinates[] getFreePositions() {
    GridCoordinates[] arr = new GridCoordinates[sizeOfGridBoard];
    int k = 0;
    for (int i = 0; i < sizeOfGridBoard; ++i){
      for (int j = 0; j < sizeOfGridBoard; ++j){
        // rajouter la condition dans le cas où la coordonnée [x,y] n'est pas allouée à une carte du plateau
        arr[k].x = i;
        arr[k].y = j;
        k++;
      }
    }
    return arr;
  }

  @Override
  public OccupiedPosition[] getOccupiedPositions() {
    return null;
  }
}

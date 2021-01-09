package shapeup.ui.gui;

import shapeup.game.Card;

import javax.swing.*;
import java.awt.*;

/**
 * View for a Shape Up! card.
 */
public class CardView extends JPanel {
  private Card card;

  /**
   * Constructs a new {@link CardView} for the given card.
   *
   * @param card the card
   */
  public CardView(Card card) {
    this.card = card;
    invalidate();
  }

  /**
   * Sets the displayed card and updates the view.
   *
   * @param card the card
   */
  public void setCard(Card card) {
    this.card = card;
    revalidate();
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g);
    if (card == null) return;

    var graphics = (Graphics2D) g;

    var filled = card.getFilledness().isFilled();
    var size = getSize();
    var minDimension = Math.min(size.height, size.width);
    size.height = minDimension;
    size.width = minDimension;

    final int margin = minDimension / 10;
    final int doubleMargin = margin * 2;

    graphics.setColor(color2Color(card.getColor()));
    graphics.setStroke(new BasicStroke(2));

    switch (card.getShape()) {
      case CIRCLE -> {
        int x = margin;
        int y = margin;
        int width = size.width - doubleMargin;
        int height = size.height - doubleMargin;
        if (filled)
          graphics.fillOval(x, y, width, height);
        else
          graphics.drawOval(x, y, width, height);
      }
      case TRIANGLE -> {
        int topX = size.width / 2;
        int topY = margin;
        int bottomLeftX = margin;
        int bottomLeftY = size.height - margin;
        int bottomRightX = size.width - margin;
        int bottomRightY = size.height - margin;

        if (filled)
          graphics.fillPolygon(new int[]{topX, bottomLeftX, bottomRightX}, new int[]{topY, bottomLeftY, bottomRightY}, 3);
        else
          graphics.drawPolygon(new int[]{topX, bottomLeftX, bottomRightX}, new int[]{topY, bottomLeftY, bottomRightY}, 3);
      }
      case SQUARE -> {
        int x = margin;
        int y = margin;
        int width = size.width - doubleMargin;
        int height = size.height - doubleMargin;
        if (filled)
          graphics.fillRect(x, y, width, height);
        else
          graphics.drawRect(x, y, width, height);
      }
    }
  }

  /**
   * Converts a Shape Up! {@link shapeup.game.Color} to an AWT {@link Color}.
   *
   * @param color the Shape Up! color
   * @return an AWT color
   */
  static Color color2Color(shapeup.game.Color color) {
    return switch (color) {
      case RED -> Color.RED;
      case GREEN -> Color.GREEN;
      case BLUE -> Color.BLUE;
    };
  }
}

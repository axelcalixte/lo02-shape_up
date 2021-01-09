package shapeup.ui.gui;

import shapeup.game.Deck;

import javax.swing.*;

/**
 * View for a Shape Up! deck.
 */
public class DeckView extends JPanel {
  JLabel label;

  /**
   * Constructs a new {@link DeckView}.
   */
  public DeckView() {
    label = new JLabel();
    add(label);
  }

  /**
   * Updates the view with a new deck.
   *
   * @param deck the new deck
   */
  public void update(Deck deck) {
    var message = deck.cardsLeft() + " cartes.";
    label.setText(message);
  }
}

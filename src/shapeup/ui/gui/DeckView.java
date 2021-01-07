package shapeup.ui.gui;

import shapeup.game.Deck;

import javax.swing.*;

public class DeckView extends JPanel {
  JLabel label;

  public DeckView() {
    label = new JLabel();
    add(label);
  }

  public void update(Deck deck) {
    var message = deck.cardsLeft() + " cartes.";
    label.setText(message);
  }
}

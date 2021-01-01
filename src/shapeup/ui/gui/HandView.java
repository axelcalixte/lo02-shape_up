package shapeup.ui.gui;

import shapeup.game.Card;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class HandView extends JPanel {
  private final HashMap<CardView, Card> cards;
  private final JPanel cardsPanel;

  public HandView() {
    setPreferredSize(new Dimension(600, 100));
    setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
    cardsPanel = new JPanel();
    cardsPanel.setLayout(new BoxLayout(cardsPanel, BoxLayout.LINE_AXIS));
    cards = new HashMap<>();

    add(new JLabel("Votre main :"));
    add(cardsPanel);
  }

  public void update(List<Card> hand) {
    cardsPanel.removeAll();
    cards.clear();

    for (var card : hand) {
      var cardView = new CardView(card);
      cardView.setPreferredSize(new Dimension(100, 100));
      cards.put(cardView, card);
      cardsPanel.add(cardView);
    }
  }

  public void setOneShotCardListener(Consumer<Card> onCardClick) {
    for (var entry : cards.entrySet()) {
      entry.getKey().addMouseListener(new MouseAdapter() {
        @Override
        public void mouseClicked(MouseEvent e) {
          super.mouseClicked(e);
          removeCardListener();
          onCardClick.accept(entry.getValue());
        }
      });
    }
  }

  public void removeCardListener() {
    for (var cardview : cards.keySet())
      for (var ml : cardview.getMouseListeners())
        cardview.removeMouseListener(ml);
  }
}

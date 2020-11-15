package shapeup.game;

import java.util.ArrayList;
import java.util.Collections;

public final class Deck {
    private final ArrayList<Card> cards = new ArrayList<>();

    public Deck() {
        this.generateDeck();
    }

    public void generateDeck() {
        for (Shape s : Shape.values()
        ) {
            for (Color c : Color.values()
            ) {
                for (Filledness f : Filledness.values()
                ) {
                    this.cards.add(new Card(c, s, f));
                }
            }
        }
        Collections.shuffle(this.cards);
    }

    public Card drawCard() {
        if (this.cards.size() == 0) {
            return null;
        }
        return this.cards.remove(this.cards.size() - 1);
    }

    public int cardsLeft() {
        return this.cards.size();
    }

    @Override
    public String toString() {
        return "Deck{" +
                 cards +
                '}';
    }

    public static void main(String[] args) {
        Deck deck = new Deck();
        Card drawn = deck.drawCard();
        Card drawn2 = deck.drawCard();
        System.out.println(drawn);
        System.out.println(drawn2);
        System.out.println(deck.cardsLeft());
        System.out.println(deck);
    }
}
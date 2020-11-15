package shapeup.game;

import javax.swing.text.html.Option;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

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

    public Optional<Card> drawCard() {
        if (this.cards.size() == 0) {
            return Optional.empty();
        }
        Card drawnCard = this.cards.remove(this.cards.size() - 1);
        return Optional.of(drawnCard);
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
}
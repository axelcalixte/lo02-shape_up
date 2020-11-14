package shapeup.ui;

import shapeup.game.Action;
import shapeup.game.Card;
import shapeup.game.Deck;
import shapeup.game.PlayerState;

import java.util.LinkedHashMap;

public class TerminalUI implements UI {
  private BoardDisplayer boardDisplayer;

  public TerminalUI() {
  }

  @Override
  public void notifyOfGameStateChange() {
    // Useless for the terminal UI for now.
  }

  @Override
  public Action askPlayersAction(int playerID, BoardDisplayer boardDisplayer, PlayerState[] pss, Deck deck, Action[] possibleActions) {
    TerminalUI.drawGame(playerID, boardDisplayer, pss, deck);
    return TUIMenu.displayMenu("Choisissez votre prochaine action :", possibleActions);
  }

  private static void drawGame(int forPlayerID, BoardDisplayer boardDisplayer, PlayerState[] pss, Deck deck) {
    System.out.println("---");
    boardDisplayer.terminalDisplay();
    System.out.println("---");
    TerminalUI.deckDisplay(deck);
    System.out.println("---");
    for (var ps : pss) {
      if (ps.getPlayerID() == forPlayerID) {
        System.out.println("Your hand:");
        for (var card : ps.getHand()) {
          TerminalUI.cardDisplay(card);
        }
        break;
      }
    }
    System.out.println("---");
  }

  private static void deckDisplay(Deck deck) {
    System.out.printf("%d cartes restantes.\n", deck.cardsLeft());
  }

  private static void cardDisplay(Card card) {
    switch (card.getFilledness()) {
      case FILLED -> System.out.print("Filled ");
      case HOLLOW -> System.out.print("Hollow ");
    }

    switch (card.getColor()) {
      case RED -> System.out.print("red ");
      case GREEN -> System.out.print("green ");
      case BLUE -> System.out.print("blue ");
    }

    switch (card.getShape()) {
      case CIRCLE -> System.out.println("circle.");
      case SQUARE -> System.out.print("square.");
      case TRIANGLE -> System.out.print("triangle.");
    }
  }

  @Override
  public void victoryScreen(int winner, LinkedHashMap<Integer, Integer> scores) {
    System.out.println("---");
    System.out.printf("Le joueur %d a gagné", winner);
    scores.forEach((player, score) ->
            System.out.println("Joueur" + player + " : " + score + ".")
    );
    System.out.println("---");
  }
}

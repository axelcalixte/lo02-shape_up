package shapeup.ui;

import shapeup.game.*;

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
    for (var ps : pss) {
      if (ps.getPlayerID() == forPlayerID && !ps.getHand().isEmpty()) {
        System.out.println("---");
        System.out.println("Votre main :");
        for (var card : ps.getHand()) {
          TerminalUI.fancyCardString(card);
        }
        break;
      }
    }
    System.out.println("---");
  }

  private static void deckDisplay(Deck deck) {
    System.out.printf("%d cartes restantes.\n", deck.cardsLeft());
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

  public static final char FILLED_CIRCLE = '\u25cf';
  public static final char HOLLOW_CIRCLE = '\u25cb';

  public static final char FILLED_SQUARE = '\u25a0';
  public static final char HOLLOW_SQUARE = '\u25a1';

  public static final char FILLED_TRIANGLE = '\u25b2';
  public static final char HOLLOW_TRIANGLE = '\u25b3';

  public static String fancyCardString(Card c) {
    char color = colorToChar(c.getColor());
    char shape = switch (c.getFilledness()) {
      case HOLLOW -> switch (c.getShape()) {
        case CIRCLE -> HOLLOW_CIRCLE;
        case SQUARE -> HOLLOW_SQUARE;
        case TRIANGLE -> HOLLOW_TRIANGLE;
      };
      case FILLED -> switch (c.getShape()) {
        case CIRCLE -> FILLED_CIRCLE;
        case SQUARE -> FILLED_SQUARE;
        case TRIANGLE -> FILLED_TRIANGLE;
      };
    };

    return String.valueOf(color) + shape;
  }

  public static char colorToChar(Color c) {
    return switch (c) {
      case RED -> 'R';
      case GREEN -> 'G';
      case BLUE -> 'B';
    };
  }
}

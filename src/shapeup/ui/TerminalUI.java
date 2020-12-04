package shapeup.ui;

import shapeup.game.*;
import shapeup.game.boards.Coordinates;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TerminalUI implements UI {
  private GameState gameState = null;
  private final BoardDisplayer bd;

  public TerminalUI(BoardDisplayer bd) {
    this.bd = bd;
  }

  /**
   * Must be called on every change.
   *
   * @param gs the new game state.
   */
  @Override
  public void update(GameState gs) {
    this.gameState = gs;
  }

  @Override
  public void moveOrPlay(int playerID, BiConsumer<Coordinates, Coordinates> onMove, BiConsumer<Coordinates, Card> onPlay) {
    executePlayersAction(playerID, Arrays.asList(
            new MenuAction() {
              public String name() {
                return "Jouer une carte";
              }

              public void run() {
                var card = askCardInHand(playerID);
                var playPosition = askPlayPosition();
                onPlay.accept(playPosition, card);
              }
            },
            new MenuAction() {
              public String name() {
                return "Déplacer une carte";
              }

              public void run() {
                var move = askMovePositions();
                onMove.accept(move[0], move[1]);
              }
            }
    ));
  }

  @Override
  public void play(int playerID, BiConsumer<Card, Coordinates> onPlay) {
    executePlayersAction(playerID, Collections.singletonList(
            new MenuAction() {
              public String name() {
                return "Jouer une carte";
              }

              public void run() {
                var card = askCardInHand(playerID);
                var playPosition = askPlayPosition();
                onPlay.accept(card, playPosition);
              }
            }
    ));
  }

  @Override
  public void move(int playerID, BiConsumer<Coordinates, Coordinates> onMove) {
    executePlayersAction(playerID, Collections.singletonList(
            new MenuAction() {
              public String name() {
                return "Déplacer une carte";
              }

              public void run() {
                var move = askMovePositions();
                onMove.accept(move[0], move[1]);
              }
            }
    ));
  }

  @Override
  public void canFinishTurn(int playerID, Consumer<Boolean> onShouldFinish) {
    drawGame(playerID);
    TUIMenu.displayMenu(
            "Finir ce tour ?",
            Arrays.asList(
                    new MenuAction() {
                      public String name() {
                        return "Oui";
                      }

                      public void run() {
                        onShouldFinish.accept(true);
                      }
                    },
                    new MenuAction() {
                      public String name() {
                        return "Non (bouger une carte)";
                      }

                      public void run() {
                        onShouldFinish.accept(false);
                      }
                    }
            )
    )
            .run();
  }

  private static final Scanner scanner = new Scanner(System.in);

  @Override
  public void turnFinished(int playerID, Runnable onFinish) {
    drawGame(playerID);
    System.out.println("---");
    System.out.printf("Tour du joueur %d terminé. Appuyez sur entrée.\n", playerID);
    System.out.println("---");

    // Strange stuff happens if we close this scanner.
    scanner.nextLine();

    onFinish.run();
  }

  @Override
  public void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish) {
    int maxScoreIdx = 0;
    for (int i = 1; i < scores.size(); i++) {
      if (scores.get(i) > scores.get(maxScoreIdx))
        maxScoreIdx = i;
    }

    System.out.println("---");
    bd.terminalDisplay(gameState.board);
    System.out.println("---");
    System.out.printf("Le joueur %d a gagné ce round.\n", maxScoreIdx);

    for (int i = 0; i < scores.size(); i++) {
      var ps = gameState.playerStates[i];
      System.out.printf(
              "Joueur %d : %d points",
              i,
              scores.get(i)
      );

      if (ps.getVictoryCard().isPresent()) {
        System.out.printf(
                ", carte victoire %s.\n",
                TerminalUI.fancyCardString(ps.getVictoryCard().get())
        );
      } else {
        System.out.println(".");
      }
    }
    System.out.println("---");
    System.out.printf("La carte cachée était %s.\n", TerminalUI.fancyCardString(hiddenCard));
    System.out.println("---");
    onFinish.run();
  }

  private void executePlayersAction(int playerID, List<MenuAction> possibleActions) {
    drawGame(playerID);
    TUIMenu.displayMenu("Choisissez votre prochaine action :", possibleActions).run();
  }

  private Coordinates askPlayPosition() {
    int xCoords = askCoords(
            "x",
            gameState.board.getPlayablePositions().stream().map(Coordinates::getX)
    );
    int yCoords = askCoords(
            "y",
            gameState.board.getPlayablePositions().stream()
                    .filter(coordinates -> coordinates.getX() == xCoords)
                    .map(Coordinates::getY)
    );
    return new Coordinates(xCoords, yCoords);
  }

  private Card askCardInHand(int playerID) {
    var hand = gameState.playerStates[playerID].getHand();
    if (hand.size() == 1) return hand.get(0);

    var card = new Object() {
      Card val = null;
    };

    TUIMenu.displayMenu(
            "Choisissez une carte :",
            hand.stream()
                    .map(c -> new MenuAction() {
                      public String name() {
                        return TerminalUI.fancyCardString(c);
                      }

                      public void run() {
                        card.val = c;
                      }
                    })
                    .collect(Collectors.toList())
    ).run();

    return card.val;
  }

  private Coordinates[] askMovePositions() {
    var occupiedPositions = gameState.board.getOccupiedPositions();
    int xSource = askCoords(
            "x de la source",
            occupiedPositions.stream().map(Coordinates::getX)
    );
    int ySource = askCoords(
            "y de la source",
            occupiedPositions.stream()
                    .filter(coordinates -> coordinates.getX() == xSource)
                    .map(Coordinates::getY)
    );
    var source = new Coordinates(xSource, ySource);

    var movablePositions = gameState.board.getMovablePositions(source);
    int xDest = askCoords(
            "x de la destination",
            movablePositions.stream().map(Coordinates::getX)
    );
    int yDest = askCoords(
            "y de la destination",
            movablePositions.stream()
                    .filter(coordinates -> coordinates.getX() == xDest)
                    .map(Coordinates::getY)
    );

    return new Coordinates[]{source, new Coordinates(xDest, yDest)};
  }

  private int askCoords(String kind, Stream<Integer> choices) {
    final var coord = new Object() {
      int val = 0;
    };

    TUIMenu.displayMenu(
            String.format("Coordonnées en %s", kind),
            choices.distinct().sorted().map(c -> new MenuAction() {
              public String name() {
                return String.valueOf(c);
              }

              public void run() {
                coord.val = c;
              }
            }).collect(Collectors.toList())
    ).run();

    return coord.val;
  }

  private void drawGame(int playerID) {
    System.out.println("---");
    System.out.println("Joueur " + playerID);
    System.out.println("---");
    bd.terminalDisplay(gameState.board);
    System.out.println("---");
    TerminalUI.deckDisplay(gameState.deck);
    for (var ps : gameState.playerStates) {
      if (ps.getPlayerID() == playerID) {

        if (!ps.getHand().isEmpty()) {
          System.out.println("---");
          System.out.println("Votre main :");
          for (var card : ps.getHand()) {
            System.out.println(TerminalUI.fancyCardString(card));
          }
        }

        ps.getVictoryCard().ifPresent(card -> {
          System.out.println("---");
          System.out.println("Votre carte victoire :");
          System.out.println(TerminalUI.fancyCardString(card));
        });
        break;
      }
    }
    System.out.println("---");
  }

  private static void deckDisplay(Deck deck) {
    System.out.printf("%d cartes restantes.\n", deck.cardsLeft());
  }

  public static final char FILLED_CIRCLE = '\u25cf';
  public static final char HOLLOW_CIRCLE = '\u25cb';

  public static final char FILLED_SQUARE = '\u25a0';
  public static final char HOLLOW_SQUARE = '\u25a1';

  public static final char FILLED_TRIANGLE = '\u25b2';
  public static final char HOLLOW_TRIANGLE = '\u25b3';

  public static String fancyCardString(Card c) {
    char color = colorToChar(c.getColor());

    char shape = ' ';
    switch (c.getFilledness()) {
      case HOLLOW:
        switch (c.getShape()) {
          case CIRCLE:
            shape = HOLLOW_CIRCLE;
            break;
          case SQUARE:
            shape = HOLLOW_SQUARE;
            break;
          case TRIANGLE:
            shape = HOLLOW_TRIANGLE;
            break;
        }
        break;
      case FILLED:
        switch (c.getShape()) {
          case CIRCLE:
            shape = FILLED_CIRCLE;
            break;
          case SQUARE:
            shape = FILLED_SQUARE;
            break;
          case TRIANGLE:
            shape = FILLED_TRIANGLE;
            break;
        }
        break;
    }

    return String.valueOf(color) + shape;
  }

  public static char colorToChar(Color c) {
    switch (c) {
      case RED:
        return 'R';
      case GREEN:
        return 'G';
      case BLUE:
        return 'B';
      default:
        throw new IllegalArgumentException();
    }
  }
}

package shapeup.ui.gui;

import shapeup.game.Card;
import shapeup.game.GameState;
import shapeup.game.boards.Coordinates;
import shapeup.ui.UI;

import javax.swing.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class GraphicalUI implements UI {
  private static final String SELECT_MAIN_PLATEAU = "sélectionnez une carte dans votre main ou sur le plateau.";
  private static final String SELECT_CASE = "sélectionnez une case.";
  private static final String SELECT_MAIN = "sélectionnez une carte dans votre main.";
  private static final String SELECT_PLATEAU = "sélectionnez une carte sur le plateau.";

  private final JFrame frame;
  private JPanel mainPanel;
  private JPanel choicePanel;
  private JLabel currentPlayer;
  private JLabel helpMessage;
  private BoardView boardView;
  private HandView handView;
  private DeckView deckView;
  private CardView victoryCard;

  private ArrayList<String> messages;

  private GameState gameState;

  public GraphicalUI() {
    frame = new JFrame("Shape Up!");
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setup();
  }

  private void setup() {
    if (mainPanel == null) {
      mainPanel = new JPanel();
      mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
      frame.add(mainPanel);
    } else {
      mainPanel.removeAll();
    }

    // Board Panel
    boardView = new BoardView();
    mainPanel.add(boardView);

    // Info panel
    var infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.LINE_AXIS));
    currentPlayer = new JLabel();
    infoPanel.add(currentPlayer);
    helpMessage = new JLabel();
    infoPanel.add(helpMessage);
    choicePanel = new JPanel();
    choicePanel.setLayout(new BoxLayout(choicePanel, BoxLayout.LINE_AXIS));
    infoPanel.add(choicePanel);
    mainPanel.add(infoPanel);

    // Hand and deck panel
    var handAndDeck = new JPanel();
    handAndDeck.setLayout(new BoxLayout(handAndDeck, BoxLayout.LINE_AXIS));
    deckView = new DeckView();
    handAndDeck.add(deckView);
    handView = new HandView();
    handAndDeck.add(handView);
    var victoryCardPanel = new JPanel();
    victoryCardPanel.setLayout(new BoxLayout(victoryCardPanel, BoxLayout.PAGE_AXIS));
    victoryCard = new CardView(null);
    victoryCardPanel.add(new JLabel("Carte victoire :"));
    victoryCardPanel.add(victoryCard);
    handAndDeck.add(victoryCardPanel);
    mainPanel.add(handAndDeck);

    frame.pack();
    frame.setVisible(true);

    messages = new ArrayList<>();
    replaceMessage("Bienvenue !");
    frame.revalidate();
  }

  private void pushMessage(String message) {
    messages.add(message);
    helpMessage.setText(message);
  }

  private void popMessage() {
    if (messages.size() == 0) {
      helpMessage.setText("");
      return;
    }

    if (messages.size() == 1) {
      messages.remove(0);
      helpMessage.setText("");
      return;
    }

    messages.remove(messages.size() - 1);
    helpMessage.setText(messages.get(messages.size() - 1));
  }

  private void replaceMessage(String message) {
    popMessage();
    pushMessage(message);
  }

  private void notifyChildren(int playerID) {
    currentPlayer.setText("Joueur " + playerID + " : ");
    boardView.update(gameState.board);
    deckView.update(gameState.deck);
    handView.update(gameState.playerStates[playerID].getHand());
    victoryCard.setCard(gameState.playerStates[playerID].getVictoryCard().orElse(null));
  }

  @Override
  public void update(GameState gs) {
    gameState = gs;
  }

  @Override
  public void moveOrPlay(int playerID, BiConsumer<Coordinates, Coordinates> onMove, BiConsumer<Coordinates, Card> onPlay) {
    notifyChildren(playerID);

    replaceMessage(SELECT_MAIN_PLATEAU);

    handView.setOneShotCardListener(card -> {
      boardView.removeCardListener();
      replaceMessage(SELECT_CASE);
      boardView.setDisplayPlayable(true);

      boardView.setOneShotPlayableListener(coordinates -> {
        popMessage();
        boardView.setDisplayPlayable(false);
        onPlay.accept(coordinates, card);
      });
    });

    boardView.setOneShotCardListener(from -> {
      handView.removeCardListener();
      replaceMessage(SELECT_CASE);
      boardView.setDisplayMovableFrom(from);

      boardView.setOneShotMovableListener(to -> {
        popMessage();
        boardView.setDisplayMovableFrom(null);
        onMove.accept(from, to);
      });
    });
  }

  @Override
  public void play(int playerID, BiConsumer<Card, Coordinates> onPlay) {
    notifyChildren(playerID);

    replaceMessage(SELECT_MAIN);
    handView.setOneShotCardListener(card -> {
      replaceMessage(SELECT_CASE);
      boardView.setDisplayPlayable(true);

      boardView.setOneShotPlayableListener(coordinates -> {
        popMessage();
        boardView.setDisplayPlayable(false);
        onPlay.accept(card, coordinates);
      });
    });
  }

  @Override
  public void move(int playerID, BiConsumer<Coordinates, Coordinates> onMove) {
    notifyChildren(playerID);

    replaceMessage(SELECT_PLATEAU);
    boardView.setOneShotCardListener(from -> {
      replaceMessage(SELECT_CASE);
      boardView.setDisplayMovableFrom(from);

      boardView.setOneShotMovableListener(to -> {
        popMessage();
        boardView.setDisplayMovableFrom(null);
        onMove.accept(from, to);
      });
    });
  }

  @Override
  public void canFinishTurn(int playerID, Consumer<Boolean> onShouldFinish) {
    notifyChildren(playerID);
    replaceMessage("Faites un choix : ");

    var moveCard = new JButton("Déplacer une carte.");
    moveCard.addActionListener(e -> {
      choicePanel.removeAll();
      popMessage();

      onShouldFinish.accept(false);
    });

    var finishTurn = new JButton("Finir votre tour.");
    finishTurn.addActionListener(e -> {
      choicePanel.removeAll();
      popMessage();

      onShouldFinish.accept(true);
    });

    choicePanel.add(moveCard);
    choicePanel.add(finishTurn);
    choicePanel.revalidate();
  }

  @Override
  public void turnFinished(int playerID, Runnable onFinishConfirmed) {
    notifyChildren(playerID);

    replaceMessage("tour terminé ! ");

    var continuer = new JButton("continuer...");
    continuer.addActionListener(e -> {
      choicePanel.removeAll();
      popMessage();

      onFinishConfirmed.run();
    });
    choicePanel.add(continuer);
    choicePanel.revalidate();
  }

  @Override
  public void roundFinished(List<Integer> scores, Card hiddenCard, Runnable onFinish) {
    mainPanel.removeAll();

    var hiddenCardPanel = new JPanel();
    hiddenCardPanel.setLayout(new BoxLayout(hiddenCardPanel, BoxLayout.LINE_AXIS));
    hiddenCardPanel.add(new JLabel("Carte cachée : "));
    hiddenCardPanel.add(new CardView(hiddenCard));
    mainPanel.add(hiddenCardPanel);

    for (int i = 0; i < gameState.playerStates.length; ++i) {
      var ps = gameState.playerStates[i];
      var panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
      panel.add(new JLabel("Joueur " + i + " - score : " + scores.get(i) + ". "));
      panel.add(new JLabel("Carte victoire : "));
      panel.add(new CardView(ps.getVictoryCard().get()));
      mainPanel.add(panel);
    }

    mainPanel.revalidate();

    mainPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        setup();
        onFinish.run();
      }
    });
  }

  @Override
  public void gameFinished(List<Integer> scores, Runnable onFinish) {
    mainPanel.removeAll();

    for (int i = 0; i < gameState.playerStates.length; ++i) {
      var ps = gameState.playerStates[i];
      var panel = new JPanel();
      panel.setLayout(new BoxLayout(panel, BoxLayout.LINE_AXIS));
      panel.add(new JLabel("Joueur " + i + " - score : " + scores.get(i) + ". "));
      mainPanel.add(panel);
    }

    mainPanel.revalidate();

    mainPanel.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        onFinish.run();
      }
    });
  }
}

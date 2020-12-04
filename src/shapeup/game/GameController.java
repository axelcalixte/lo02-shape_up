package shapeup.game;

import shapeup.game.boards.Board;
import shapeup.game.boards.Coordinates;
import shapeup.game.players.BasicAI;
import shapeup.game.players.PlayerStrategy;
import shapeup.game.players.PlayerType;
import shapeup.game.players.RealPlayer;
import shapeup.game.scores.NormalScoreCounter;
import shapeup.game.scores.ScoreCounterVisitor;
import shapeup.ui.BoardDisplayer;
import shapeup.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Main controller class.
 * {@code startRound} should be called after constructing it.
 * Currently doesn't support starting several rounds with the same controller object.
 */
public final class GameController {
  private final PlayerStrategy[] playerStrategies;
  private final PlayerState[] playerStates;

  private final Board board;
  private final Deck deck;
  private Card hiddenCard;

  private final boolean advancedShapeUp;
  private final ScoreCounterVisitor scoreCounter;

  /**
   * Used to display scores at the end of AI-only games
   */
  private final UI ui;
  /**
   * Used to display scores at the end of AI-only games
   */
  private final boolean aiOnlyGame;

  /**
   * Constructs a GameController.
   *
   * @param uiConstructor    used to get a user interface.
   * @param boardConstructor used to get a board.
   * @param playerTypes      the types of players playing the game (AI/real).
   * @param advancedShapeUp  whether the "Advanced Shape Up!" rules should be used.
   */
  public GameController(Function<BoardDisplayer, UI> uiConstructor,
                        Supplier<Board> boardConstructor,
                        List<PlayerType> playerTypes,
                        boolean advancedShapeUp) {
    this.board = boardConstructor.get();
    this.advancedShapeUp = advancedShapeUp;

    this.deck = new Deck();

    final int nbPlayers = playerTypes.size();
    if (nbPlayers < 2 || nbPlayers > 3)
      throw new IllegalArgumentException("2 or 3 players");

    this.playerStrategies = new PlayerStrategy[nbPlayers];
    this.playerStates = new PlayerState[nbPlayers];

    this.ui = uiConstructor.apply(this.board.displayer());

    this.aiOnlyGame = playerTypes.stream().noneMatch(playerType -> playerType == PlayerType.REAL_PLAYER);

    for (int i = 0; i < playerTypes.size(); i++) {
      playerStates[i] = new PlayerState(i);
      switch (playerTypes.get(i)) {
        case BASIC_AI:
          playerStrategies[i] = new BasicAI(i);
          break;
        case REAL_PLAYER:
          playerStrategies[i] = new RealPlayer(ui, i);
      }
    }

    this.scoreCounter = new NormalScoreCounter();
  }

  public void startRound() {
    //noinspection OptionalGetWithoutIsPresent
    hiddenCard = deck.drawCard().get();

    for (PlayerState ps : this.playerStates) {
      if (advancedShapeUp)
        for (int i = 0; i < 3; i++)
          //noinspection OptionalGetWithoutIsPresent
          ps.giveCard(deck.drawCard().get());
      else
        //noinspection OptionalGetWithoutIsPresent
        ps.giveVictoryCard(deck.drawCard().get());
    }

    this.startGameTurn();
  }

  /**
   * Play out an entire game turn, for all players.
   */
  private void startGameTurn() {
    // Drops the call stack.
    new Thread(() ->
            this.playerTurn(0)
    ).start();
  }

  /**
   * Play out a player's turn.
   *
   * @param playerID their ID
   */
  private void playerTurn(int playerID) {
    if (advancedShapeUp)
      advancedPlayerTurnStart(playerID);
    else
      simplePlayerTurnStart(playerID);
  }

  private void simplePlayerTurnStart(int playerID) {
    if (deck.cardsLeft() == 0
            && Arrays.stream(this.playerStates).allMatch(ps -> ps.getHand().size() <= 0)) {
      this.finishRound();
      return;
    }

    var currentPlayerState = playerStates[playerID];

    var drawnCard = deck.drawCard();
    if (drawnCard.isEmpty() && currentPlayerState.getHand().size() == 0) {
      this.onTurnFinished(playerID);
      return;
    }

    this.updateStrategies();

    drawnCard.ifPresent(currentPlayerState::giveCard);

    this.updateStrategies();

    commonTurnEnd(playerID);
  }

  private void advancedPlayerTurnStart(int playerID) {
    if (deck.cardsLeft() == 0
            && Arrays.stream(this.playerStates).allMatch(ps -> ps.getHand().size() == 1)) {
      this.finishRound();
      return;
    }

    commonTurnEnd(playerID);
  }

  private void commonTurnEnd(int playerID) {
    updateStrategies();

    var currentPlayerStrategy = playerStrategies[playerID];

    if (board.getOccupiedPositions().size() > 1)
      currentPlayerStrategy.canMoveOrPlay(
              (card, coordinates) -> onPlay(playerID, card, coordinates, false),
              (from, to) -> onMove(playerID, from, to, false)
      );
    else
      currentPlayerStrategy.canPlay(
              (card, coordinates) -> this.onPlay(playerID, card, coordinates, false)
      );
  }

  private void onPlay(int playerID, Card card, Coordinates coordinates, boolean alreadyMoved) {
    var currentPlayerState = this.playerStates[playerID];
    var currentPlayerStrategy = this.playerStrategies[playerID];

    currentPlayerState.getHand().remove(card);
    this.board.playCard(card, coordinates);

    this.updateStrategies();

    if (alreadyMoved) {
      currentPlayerStrategy.turnFinished(
              () -> this.onTurnFinished(playerID)
      );
    } else {
      // If a card can be moved.
      if (board.getOccupiedPositions().size() > 1)
        currentPlayerStrategy.canFinishTurn(
                () -> this.onTurnFinished(playerID),
                (from, to) -> this.onMove(playerID, from, to, true)
        );
        // Special case for player 0's first turn.
      else
        currentPlayerStrategy.turnFinished(() -> this.onTurnFinished(playerID));
    }
  }

  private void onMove(int playerID, Coordinates from, Coordinates to, boolean alreadyPlayed) {
    var currentPlayerStrategy = this.playerStrategies[playerID];

    this.board.moveCard(from, to);

    this.updateStrategies();

    if (alreadyPlayed)
      currentPlayerStrategy.turnFinished(() -> this.onTurnFinished(playerID));
    else
      currentPlayerStrategy.canPlay((card, coordinates) -> this.onPlay(playerID, card, coordinates, true));
  }

  void onTurnFinished(int playerID) {
    if (advancedShapeUp)
      deck.drawCard().ifPresent(c -> {
        updateStrategies();
        playerStates[playerID].giveCard(c);
        updateStrategies();
      });

    if (playerID == playerStates.length - 1)
      startGameTurn();
    else
      playerTurn(playerID + 1);
  }

  private void finishRound() {
    var scores = new ArrayList<Integer>(playerStates.length);
    for (var pstate : playerStates) {
      var victoryCard = pstate.getVictoryCard();

      victoryCard.ifPresent(card -> scores.add(board.acceptScoreCounter(scoreCounter, card)));
    }

    for (var pstrat : playerStrategies) {
      pstrat.roundFinished(scores, hiddenCard, () -> {
      });
    }

    if (this.aiOnlyGame) {
      this.ui.update(new GameState(playerStates, board, deck));
      this.ui.roundFinished(scores, hiddenCard, () -> {
      });
    }
  }

  private void updateStrategies() {
    for (var pstrat : playerStrategies) {
      pstrat.update(new GameState(playerStates, board, deck));
    }
  }
}

package shapeup.game;

import shapeup.game.boards.Board;
import shapeup.game.boards.Coordinates;
import shapeup.game.players.BasicAI;
import shapeup.game.players.PlayerStrategy;
import shapeup.game.players.PlayerType;
import shapeup.game.players.RealPlayer;
import shapeup.game.scores.NormalScoreCounter;
import shapeup.game.scores.ScoreCounterVisitor;
import shapeup.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Supplier;

/**
 * Main controller class.
 * {@code startGame} should be called after constructing it.
 * Currently doesn't support starting several rounds with the same controller object.
 */
public final class Game {
  private final PlayerStrategy[] playerStrategies;

  private PlayerState[] playerStates;
  private Deck deck;
  private Board board;
  private Card hiddenCard;

  private final Supplier<Board> boardConstructor;
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
  public Game(Supplier<UI> uiConstructor,
              Supplier<Board> boardConstructor,
              List<PlayerType> playerTypes,
              boolean advancedShapeUp) {
    this.boardConstructor = boardConstructor;
    this.advancedShapeUp = advancedShapeUp;

    final int nbPlayers = playerTypes.size();
    if (nbPlayers < 2 || nbPlayers > 3)
      throw new IllegalArgumentException("2 or 3 players");

    this.playerStrategies = new PlayerStrategy[nbPlayers];

    this.ui = uiConstructor.get();

    this.aiOnlyGame = playerTypes.stream().noneMatch(playerType -> playerType == PlayerType.REAL_PLAYER);

    for (int i = 0; i < nbPlayers; i++) {
      switch (playerTypes.get(i)) {
        case BASIC_AI -> playerStrategies[i] = new BasicAI(i);
        case REAL_PLAYER -> playerStrategies[i] = new RealPlayer(ui, i);
      }
    }

    this.scoreCounter = new NormalScoreCounter();
    cleanGameState();
    this.updateStrategies();
  }

  private void cleanGameState() {
    board = boardConstructor.get();
    deck = new Deck();

    final int nbPlayer = playerStrategies.length;

    playerStates = new PlayerState[nbPlayer];
    for (int i = 0; i < nbPlayer; i++) {
      playerStates[i] = new PlayerState(i);
    }
  }

  // in preparation of multi-round games
  public void startGame() {
    cleanGameState();
    playRound();
  }

  private void playRound() {
    //noinspection OptionalGetWithoutIsPresent
    hiddenCard = deck.drawCard().get();

    for (PlayerState ps : playerStates) {
      if (advancedShapeUp)
        for (int i = 0; i < 3; i++)
          //noinspection OptionalGetWithoutIsPresent
          ps.giveCard(deck.drawCard().get());
      else
        //noinspection OptionalGetWithoutIsPresent
        ps.giveVictoryCard(deck.drawCard().get());
    }

    boolean keepPlaying;
    do {
      keepPlaying = this.gameTurn();
    } while (keepPlaying);

    var scores = new ArrayList<Integer>(playerStates.length);
    for (var pstate : playerStates) {
      var victoryCard = pstate.getVictoryCard();

      victoryCard.ifPresent(card -> scores.add(board.acceptScoreCounter(scoreCounter, card)));
    }

    // Wait for all players
    CompletableFuture.allOf(
            Arrays.stream(playerStrategies)
                    .map((strat) -> strat.roundFinished(scores, hiddenCard))
                    .toArray(CompletableFuture[]::new)
    ).join();

    if (this.aiOnlyGame) {
      this.ui.update(new GameState(playerStates, board, deck));
      this.ui.roundFinished(scores, hiddenCard, () -> {
      });
    }
  }

  /**
   * Play out an entire game turn, for all players.
   *
   * @return whether to play the next turn
   */
  private boolean gameTurn() {
    for (var ps : playerStates) {
      var keepPlaying = this.playerTurn(ps.getPlayerID());
      if (!keepPlaying) return false;
    }

    return true;
  }

  /**
   * Play out a player's turn.
   *
   * @param playerID their ID
   * @return whether to keep playing the current round
   */
  private boolean playerTurn(int playerID) {
    boolean keepPlaying = advancedShapeUp ?
            advancedPlayerTurnStart() :
            simplePlayerTurnStart(playerID);

    if (!keepPlaying) return false;

    updateStrategies();

    var currentPlayerStrategy = playerStrategies[playerID];

    var canMove = board.getOccupiedPositions().size() > 1;
    // For first & second turns
    if (canMove) {
      var nextMove = currentPlayerStrategy.canMoveOrPlay().join();

      nextMove
              .ifMoved((from, to) -> {
                move(from, to);

                if (currentPlayerStrategy.canFinishTurn().join()) return;

                var played = currentPlayerStrategy.canPlay().join();
                play(playerID, played.a, played.b);
              })
              .ifPlayed((card, coord) -> {
                play(playerID, card, coord);

                if (currentPlayerStrategy.canFinishTurn().join()) return;

                var moved = currentPlayerStrategy.canMove().join();
                move(moved.a, moved.b);
              });
    } else {
      var played = currentPlayerStrategy.canPlay().join();
      play(playerID, played.a, played.b);
    }

    if (advancedShapeUp)
      deck.drawCard().ifPresent(c -> {
        updateStrategies();
        playerStates[playerID].giveCard(c);
        updateStrategies();
      });

    currentPlayerStrategy.turnFinished().join();
    return true;
  }

  private boolean simplePlayerTurnStart(int playerID) {
    if (deck.cardsLeft() == 0
            && Arrays.stream(this.playerStates).allMatch(ps -> ps.getHand().size() <= 0)) {
      return false;
    }

    var currentPlayerState = playerStates[playerID];

    var drawnCard = deck.drawCard();
    if (drawnCard.isEmpty() && currentPlayerState.getHand().size() == 0) {
      return false;
    }

    this.updateStrategies();

    drawnCard.ifPresent(currentPlayerState::giveCard);

    this.updateStrategies();

    return true;
  }

  private boolean advancedPlayerTurnStart() {
    if (deck.cardsLeft() == 0
            && Arrays.stream(this.playerStates).allMatch(ps -> ps.getHand().size() == 1)) {
      return false;
    }

    return true;
  }

  private void play(int playerID, Card card, Coordinates coord) {
    playerStates[playerID].getHand().remove(card);
    this.board.playCard(card, coord);

    this.updateStrategies();
  }

  private void move(Coordinates from, Coordinates to) {
    this.board.moveCard(from, to);

    this.updateStrategies();
  }

  private void updateStrategies() {
    for (var pstrat : playerStrategies) {
      pstrat.update(new GameState(playerStates, board, deck));
    }
  }
}

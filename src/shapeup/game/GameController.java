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

public final class GameController {
  private final PlayerStrategy[] playerStrategies;
  private final PlayerState[] playerStates;

  private final Board board;
  private final Deck deck;
  private final ScoreCounterVisitor scoreCounter;
  private Card hiddenCard;

  public GameController(Function<BoardDisplayer, UI> uiConstructor, Supplier<Board> boardConstructor, List<PlayerType> playerTypes) {
    this.board = boardConstructor.get();

    this.deck = new Deck();

    final int nbPlayers = playerTypes.size();
    if (nbPlayers < 2 || nbPlayers > 3)
      throw new IllegalArgumentException("2 or 3 players");

    this.playerStrategies = new PlayerStrategy[nbPlayers];
    this.playerStates = new PlayerState[nbPlayers];

    var ui = uiConstructor.apply(this.board.displayer());

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
    hiddenCard = this.deck.drawCard().get();

    for (PlayerState ps : this.playerStates) {
      ps.giveVictoryCard(this.deck.drawCard().get());
    }

    // TODO: let the players choose who starts

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
    if (deck.cardsLeft() == 0
            && Arrays.stream(this.playerStates).allMatch(ps -> ps.getHand().size() <= 0)) {
      this.finishRound();
      return;
    }

    var currentPlayerStrategy = playerStrategies[playerID];
    var currentPlayerState = playerStates[playerID];

    var drawnCard = deck.drawCard();
    if (drawnCard.isEmpty() && currentPlayerState.getHand().size() == 0) {
      this.onTurnFinished(playerID);
      return;
    }

    this.updateStrategies();

    drawnCard.ifPresent(currentPlayerState::giveCard);

    this.updateStrategies();

    if (board.getOccupiedPositions().size() > 1)
      currentPlayerStrategy.canMoveOrPlay(
              (card, coordinates) -> this.onPlay(playerID, card, coordinates, false),
              (from, to) -> this.onMove(playerID, from, to, false)
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
    if (playerID == playerStates.length - 1)
      this.startGameTurn();
    else
      this.playerTurn(playerID + 1);
  }

  private void finishRound() {
    var scores = new ArrayList<Integer>(playerStates.length);
    for (var pstate : playerStates) {
      if (pstate.getVictoryCard().isPresent()) {

        scores.add(board.acceptScoreCounter(scoreCounter, pstate.getVictoryCard().get()));
      }
    }

    for (var pstrat : playerStrategies) {
      pstrat.roundFinished(scores, hiddenCard, () -> {
      });
    }
  }

  private void updateStrategies() {
    for (var pstrat : playerStrategies) {
      pstrat.update(new GameState(playerStates, board, deck));
    }
  }


}

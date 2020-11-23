package shapeup.game;

import shapeup.game.boards.Board;
import shapeup.game.boards.Coordinates;
import shapeup.game.players.BasicAI;
import shapeup.game.players.PlayerStrategy;
import shapeup.game.players.RealPlayer;
import shapeup.game.scores.NormalScoreCounter;
import shapeup.game.scores.ScoreCounterVisitor;
import shapeup.ui.BoardDisplayer;
import shapeup.ui.UI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public final class GameController {
  private final PlayerStrategy[] playerStrategies;
  private final PlayerState[] playerStates;

  private final Board board;
  private final Deck deck;
  private final ScoreCounterVisitor scoreCounter;
  private Card hiddenCard;

  public GameController(Function<BoardDisplayer, UI> uiConstructor, Supplier<Board> boardConstructor, boolean withAI) {
    this.board = boardConstructor.get();

    this.deck = new Deck();

    final int nbPlayers = 2;

    this.playerStrategies = new PlayerStrategy[nbPlayers];
    this.playerStates = new PlayerState[nbPlayers];

    var ui = uiConstructor.apply(this.board.displayer());

    if (withAI) {
      playerStrategies[0] = new RealPlayer(ui, 0);
      playerStates[0] = new PlayerState(0);
      playerStrategies[1] = new BasicAI(1);
      playerStates[1] = new PlayerState(1);
    } else {
      for (int i = 0; i < nbPlayers; i++) {
        playerStrategies[i] = new RealPlayer(ui, i);
        playerStates[i] = new PlayerState(i);
      }
    }

    this.scoreCounter = new NormalScoreCounter();
  }

  public void startGame() {
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
    // Drops the call stack. Please kill me.
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

    final var currentPlayerStrategy = new Object() {
      PlayerStrategy val;
    };
    final var currentPlayerState = new Object() {
      PlayerState val;
    };
    for (int i = 0; i < playerStrategies.length; ++i) {
      if (playerStrategies[i].getPlayerID() == playerID) {
        currentPlayerStrategy.val = playerStrategies[i];
        currentPlayerState.val = playerStates[i];
        break;
      }
    }

    if (currentPlayerState.val == null || currentPlayerStrategy.val == null)
      throw new IllegalArgumentException();

    Runnable onTurnFinished = () -> {
      if (playerID == playerStates.length - 1)
        this.startGameTurn();
      else
        this.playerTurn(playerID + 1);
    };

    var drawnCard = deck.drawCard();
    if (drawnCard.isEmpty() && currentPlayerState.val.getHand().size() == 0) {
      onTurnFinished.run();
      return;
    }

    drawnCard.ifPresent(card ->
            currentPlayerState.val.giveCard(card));

    this.updateStrategies();

    var alreadyPlayed = new Object() {
      boolean val = false;
    };
    var alreadyMoved = new Object() {
      boolean val = false;
    };

    var onPlay = new Object() {
      Consumer<Coordinates> val;
    };

    BiConsumer<Coordinates, Coordinates> onMove = (from, to) -> {
      if (alreadyMoved.val) return;
      this.board.moveCard(from, to);
      this.updateStrategies();
      alreadyMoved.val = true;
      if (alreadyPlayed.val) {
        currentPlayerStrategy.val.turnFinished(onTurnFinished);
      }
      currentPlayerStrategy.val.canPlay(onPlay.val);
    };

    onPlay.val = coord -> {
      if (alreadyPlayed.val) return;

      var toPlay = currentPlayerState.val.takeCard(0).get();
      this.board.playCard(toPlay, coord);
      this.updateStrategies();

      alreadyPlayed.val = true;
      if (alreadyMoved.val) {
        currentPlayerStrategy.val.turnFinished(onTurnFinished);
        return;
      }
      // If a card can be moved (special case for player 0's first turn).
      if (board.getOccupiedPositions().size() > 1)
        currentPlayerStrategy.val.canFinishTurn(onTurnFinished, onMove);
      else
        currentPlayerStrategy.val.turnFinished(onTurnFinished);
    };

    if (board.getOccupiedPositions().size() > 1)
      currentPlayerStrategy.val.canMoveOrPlay(onPlay.val, onMove);
    else
      currentPlayerStrategy.val.canPlay(onPlay.val);
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

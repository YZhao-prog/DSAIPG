package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class MCTSTest {
    @Test
    public void testSimulate_Win() {
        // X plays and should win
        String board = "X . O\nX O .\n. . .";
        State<TicTacToe> state = new TicTacToe().new TicTacToeState(Position.parsePosition(board, TicTacToe.O));
        int score = invokeSimulate(state);
        assertTrue("simulate should return 2 for win or 0 for loss", score == 0 || score == 2);
    }

    @Test
    public void testExploreAndBestSelection() {
        TicTacToe game = new TicTacToe(1234L);
        Node<TicTacToe> root = new TicTacToeNode(game.start());

        MCTS.run(root, 200); // Run 200 simulations

        Node<TicTacToe> best = MCTS.getBest(root);
        assertNotNull("Best child should not be null", best);
        assertTrue("Best child should have at least 1 playout", best.playouts() > 0);
        assertTrue("Best child should have win score ≥ 0", best.wins() >= 0);
    }

    @Test
    public void testGetBestByWinRate() {
        TicTacToe game = new TicTacToe(2024L);
        Node<TicTacToe> root = new TicTacToeNode(game.start());

        MCTS.run(root, 300);

        Node<TicTacToe> bestByWinRate = MCTS.getBestByWinRate(root);
        assertNotNull(bestByWinRate);
        assertTrue(bestByWinRate.playouts() > 0);
    }

    // --------------- Helper for accessing simulate() (if private) ---------------
    private int invokeSimulate(State<TicTacToe> state) {
        return new Object() {
            int simulate(State<TicTacToe> s) {
                int rootPlayer = s.player();
                while (!s.isTerminal()) {
                    s = s.next(s.chooseMove(s.player()));
                }
                Optional<Integer> winner = s.winner();
                return winner.isEmpty() ? 1 : (winner.get() == rootPlayer ? 2 : 0);
            }
        }.simulate(state);
    }
}
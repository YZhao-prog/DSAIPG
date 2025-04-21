package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import org.junit.Test;

public class TicTacSimulateUtil {
    @Test
    public void runMCTSSimulation() {
        TicTacSimulateUtil.runMCTSSimulationStats(100, 1000);
    }

    public static void runMCTSSimulationStats(int games, int simulationsPerMove) {
        int xWins = 0, oWins = 0, draws = 0;

        for (int i = 0; i < games; i++) {
            TicTacToe game = new TicTacToe();
            Node<TicTacToe> root = new TicTacToeNode(game.start());
            int moveNumber = 0;

            while (!root.state().isTerminal()) {
                System.out.printf(">>> Move #%d by Player: %s\n", moveNumber,
                        root.state().player() == TicTacToe.X ? "X" : "O");

                // MCTS simulate and choose best move
                MCTS.run(root, simulationsPerMove);
                Node<TicTacToe> best = MCTS.getBest(root);

                // show mid result
                System.out.printf("Chosen Move Playouts: %d, Wins: %d, WinRate: %.2f\n",
                        best.playouts(), best.wins(), best.playouts() == 0 ? 0 : (double) best.wins() / best.playouts());

                System.out.println("New Board After Move:");
                System.out.println(best.state());
                System.out.println("----------------------------------------------------");

                // move to next step
                root = new TicTacToeNode(best.state());
                moveNumber++;
            }

            // store result
            var winner = root.state().winner();
            if (winner.isEmpty()) {
                draws++;
            } else if (winner.get() == TicTacToe.X) {
                xWins++;
            } else {
                oWins++;
            }
        }

        // print result
        System.out.println("=== MCTS TicTacToe Simulation (" + games + " games) ===");
        System.out.printf("X wins : %d (%.2f%%)\n", xWins, xWins * 100.0 / games);
        System.out.printf("O wins : %d (%.2f%%)\n", oWins, oWins * 100.0 / games);
        System.out.printf("Draws  : %d (%.2f%%)\n", draws, draws * 100.0 / games);
        System.out.println("===============================================");
    }
}

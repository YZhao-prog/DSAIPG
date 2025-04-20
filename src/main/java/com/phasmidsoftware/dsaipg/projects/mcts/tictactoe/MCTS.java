/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.Comparator;
import java.util.Optional;

public class MCTS {

    public static void main(String[] args) {
        TicTacToe game = new TicTacToe(42L);
        TicTacToeNode root = new TicTacToeNode(game.start());

        int moveNumber = 0;
        while (!root.isLeaf()) {
            System.out.printf("Move #%d - Player: %s%n", moveNumber + 1,
                    root.state().player() == TicTacToe.X ? "X" : "O");

            // Step 1: MCTS Search on current root
            root.explore(); //  addChildren + MCTS.explore + backPropagate

            // Step 2: Choose best move (most simulations)
            Node<TicTacToe> best = MCTS.getBestByWinRate(root);

            // Step 3: Display move
            System.out.println("Best move chosen:");
            System.out.println(best.state());
            System.out.printf("→ Playouts: %d, Wins: %d, Win Rate: %.2f%n%n",
                    best.playouts(), best.wins(), (double) best.wins() / best.playouts());

            // Step 4: Advance to next state
            root = new TicTacToeNode(best.state());
            moveNumber++;
        }

        // Step 5: Game over
        System.out.println("=== Final Game State ===");
        System.out.println(root.state());
        System.out.println("Game Over!");
        root.state().winner().ifPresentOrElse(
                winner -> System.out.println("Winner: " + (winner == TicTacToe.X ? "X" : "O")),
                () -> System.out.println("Result: Draw")
        );
    }

    /**
     * Explore a node:
     * - Add all children via Node.addChildren (already done in Node.explore)
     * - For each child:
     *     - Simulate a complete game from that child's state
     *     - Set child wins/playouts based on the result
     * Parent's backPropagate is called in Node.explore()
     */
    public static void explore(Node<TicTacToe> node) {
        for (Node<TicTacToe> child : node.children()) {
            int result = simulate(child.state());
            if (child instanceof TicTacToeNode tNode) {
                tNode.setPlayouts(1);
                tNode.setWins(result);
            }
        }
    }

    /**
     * Simulate a random playout until terminal state.
     * @param state the starting state
     * @return 2 if win, 1 if draw, 0 if loss (from the perspective of starting player)
     */
    private static int simulate(State<TicTacToe> state) {
        int rootPlayer = state.player();
        while (!state.isTerminal()) {
            Move<TicTacToe> move = state.chooseMove(state.player());
            state = state.next(move);
        }
        Optional<Integer> winner = state.winner();
        return winner.isEmpty() ? 1 : (winner.get() == rootPlayer ? 2 : 0);
    }

    /**
     * Select the best child node based on playouts.
     * @param node parent node
     * @return the most visited child
     */
    public static Node<TicTacToe> getBest(Node<TicTacToe> node) {
        return node.children().stream()
                .max(Comparator.comparingInt(Node::playouts))
                .orElseThrow(() -> new RuntimeException("No children"));
    }

    /**
     * Optional: Get best child by win rate.
     */
    public static Node<TicTacToe> getBestByWinRate(Node<TicTacToe> node) {
        return node.children().stream()
                .max(Comparator.comparingDouble(child -> {
                    int p = child.playouts();
                    return p == 0 ? 0 : (double) child.wins() / p;
                }))
                .orElseThrow();
    }
}
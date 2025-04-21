package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;
import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.*;

public class MCTS {

    private static final int SIMULATIONS = 1000;
    private static final double EXPLORATION_CONSTANT = Math.sqrt(2);

    /**
     * use MCTS
     */
    public static void run(Node<TicTacToe> root, int iterations) {
        for (int i = 0; i < iterations; i++) {
            Node<TicTacToe> selected = select(root);
            Node<TicTacToe> expanded = expand(selected);
            int result = simulate(expanded.state());
            backPropagate(expanded, result);
        }
    }

    /**
     *
     */
    private static Node<TicTacToe> select(Node<TicTacToe> node) {
        while (!node.isLeaf() && !node.children().isEmpty()) {
            Node<TicTacToe> finalNode = node;
            node = node.children().stream()
                    .max(Comparator.comparingDouble(child -> ucbScore(finalNode, child)))
                    .orElseThrow();
        }
        return node;
    }

    /**
     * UCB1 rate
     */
    private static double ucbScore(Node<TicTacToe> parent, Node<TicTacToe> child) {
        if (child.playouts() == 0) return Double.POSITIVE_INFINITY;
        double winRate = (double) child.wins() / child.playouts();
        double explore = Math.sqrt(Math.log(parent.playouts() + 1.0) / child.playouts());
        return winRate + EXPLORATION_CONSTANT * explore;
    }

    /**
     * Expansion：expand a node that hasn't been visited
     */
    private static Node<TicTacToe> expand(Node<TicTacToe> node) {
//        node.explore();
        if (node.isLeaf()) return node;

        if (node.children().isEmpty()) {
            // Only expand one child node
            State<TicTacToe> currentState = node.state();
            Iterator<Move<TicTacToe>> iterator = currentState.moveIterator(currentState.player());
            if (iterator.hasNext()) {
                Move<TicTacToe> move = iterator.next();
                State<TicTacToe> childState = currentState.next(move);
                return node.addChild(childState);
//                return new TicTacToeNode(childState);
            }
        }

        return node.children().stream()
                .filter(child -> child.playouts() == 0)
                .findAny()
                .orElseGet(() -> node.children().iterator().next());
    }

    /**
     * Simulation：run to final
     */
    private static int simulate(State<TicTacToe> state) {
        int rootPlayer = state.player();
        while (!state.isTerminal()) {
            Iterator<Move<TicTacToe>> moves = state.moveIterator(state.player());
            Move<TicTacToe> move = moves.next();
            state = state.next(move);
        }
        Optional<Integer> winner = state.winner();
        return winner.isEmpty() ? 1 : (winner.get() == rootPlayer ? 2 : 0);
    }

    /**
     * Backpropagation：callback score to parent recursively
     */
    private static void backPropagate(Node<TicTacToe> node, int result) {
        while (node instanceof TicTacToeNode tNode) {
            tNode.incrementPlayouts();
            tNode.addWins(result);
            node = ((TicTacToeNode) node).getParent();
        }
    }

    /**
     * find best node
     */
    public static Node<TicTacToe> getBest(Node<TicTacToe> node) {
        return node.children().stream()
                .max(Comparator.comparingInt(Node::playouts))
                .orElseThrow(() -> new RuntimeException("No children"));
    }

    /**
     * find best node by winrate
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

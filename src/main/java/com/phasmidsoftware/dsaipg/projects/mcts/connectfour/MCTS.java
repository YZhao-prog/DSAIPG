package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;

import java.util.*;

public class MCTS<G extends Game<G>> {
    private final int simulations;
    private final Random random = new Random();

    public MCTS(int simulations) {
        this.simulations = simulations;
    }

    public Move<G> choose(State<G> rootState) {
        MCTSNode<G> root = new MCTSNode<>(null, null, rootState);
        for (int i = 0; i < simulations; i++) {
            MCTSNode<G> node = select(root);
            if (!node.state.isTerminal()) {
                expand(node);
                node = node.children.isEmpty() ? node : randomChild(node);
            }
            int result = simulate(node.state);
            backpropagate(node, result);
        }
        return bestChild(root).move;
    }

    private MCTSNode<G> select(MCTSNode<G> node) {
        while (!node.children.isEmpty()) {
            node = ucbBest(node);
        }
        return node;
    }

    private void expand(MCTSNode<G> node) {
        for (Move<G> move : node.state.moves(node.state.player())) {
            State<G> nextState = node.state.next(move);
            MCTSNode<G> child = new MCTSNode<>(node, move, nextState);
            node.children.add(child);
        }
    }

    private int simulate(State<G> state) {
        State<G> current = state;
        while (!current.isTerminal()) {
            Move<G> move = current.chooseMove(current.player());
            current = current.next(move);
        }
        return current.winner().orElse(-1);
    }

    private void backpropagate(MCTSNode<G> node, int winner) {
        while (node != null) {
            node.visits++;
            if (node.state.player() != winner) node.wins++;
            node = node.parent;
        }
    }

    private MCTSNode<G> ucbBest(MCTSNode<G> node) {
        return Collections.max(node.children, Comparator.comparingDouble(this::ucb1));
    }

    private double ucb1(MCTSNode<G> node) {
        if (node.visits == 0) return Double.MAX_VALUE;
        return (double) node.wins / node.visits + Math.sqrt(2 * Math.log(node.parent.visits) / node.visits);
    }

    private MCTSNode<G> bestChild(MCTSNode<G> node) {
        return Collections.max(node.children, Comparator.comparingInt(n -> n.visits));
    }

    private MCTSNode<G> randomChild(MCTSNode<G> node) {
        return node.children.get(random.nextInt(node.children.size()));
    }
}
package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;

import java.util.*;

public class MCTSNode<G extends Game<G>> {
    final MCTSNode<G> parent;
    final Move<G> move;
    final State<G> state;
    final List<MCTSNode<G>> children = new ArrayList<>();
    int wins = 0;
    int visits = 0;

    public MCTSNode(MCTSNode<G> parent, Move<G> move, State<G> state) {
        this.parent = parent;
        this.move = move;
        this.state = state;
    }
}
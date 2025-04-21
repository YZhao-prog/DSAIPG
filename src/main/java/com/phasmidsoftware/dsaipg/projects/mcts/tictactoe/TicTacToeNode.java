/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.projects.mcts.tictactoe;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Node;
import com.phasmidsoftware.dsaipg.projects.mcts.core.State;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class TicTacToeNode implements Node<TicTacToe> {

    private TicTacToeNode parent = null;

    public TicTacToeNode getParent() {
        return parent;
    }

    public void setParent(TicTacToeNode parent) {
        this.parent = parent;
    }
    /**
     * @return true if this node is a leaf node (in which case no further exploration is possible).
     */
    public boolean isLeaf() {
        return state().isTerminal();
    }

    /**
     * @return the State of the Game G that this Node represents.
     */
    public State<TicTacToe> state() {
        return state;
    }

    /**
     * Method to determine if the player who plays to this node is the opening player (by analogy with chess).
     * For this method, we assume that X goes first so is "white."
     * NOTE: this assumes a two-player game.
     *
     * @return true if this node represents a "white" move; false for "black."
     */
    public boolean white() {
        return state.player() == state.game().opener();
    }

    /**
     * @return the children of this Node.
     */
    public Collection<Node<TicTacToe>> children() {
        return children;
    }

    public void update() {
        MCTS.run(this, TicTacToeNode.ROUND);
    }

    /**
     * Method to add a child to this Node.
     *
     * @param state the State for the new chile.
     */
    public Node<TicTacToe> addChild(State<TicTacToe> state) {
        TicTacToeNode child = new TicTacToeNode(state);
        child.setParent(this);
        children.add(child);
        return child;
    }


    /**
     * This method sets the number of wins and playouts according to the children states.
     */
    public void backPropagate() {
        playouts = 0;
        wins = 0;
        for (Node<TicTacToe> child : children) {
            wins += child.wins();
            playouts += child.playouts();
        }
    }

    /**
     * @return the score for this Node and its descendents a win is worth 2 points, a draw is worth 1 point.
     */
    public int wins() {
        return wins;
    }

    /**
     * @return the number of playouts evaluated (including this node). A leaf node will have a playouts value of 1.
     */
    public int playouts() {
        return playouts;
    }

    public TicTacToeNode(State<TicTacToe> state) {
        this.state = state;
        children = new ArrayList<>();
        initializeNodeData();
    }

    private void initializeNodeData() {
        if (isLeaf()) {
            playouts = 1;
            Optional<Integer> winner = state.winner();
            if (winner.isPresent())
                wins = 2; // CONSIDER check that the winner is the correct player. We shouldn't need to.
            else
                wins = 1; // a draw.
        }
    }

    private final State<TicTacToe> state;
    private final ArrayList<Node<TicTacToe>> children;
    public static final Integer ROUND = 100;

    private int wins;

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setPlayouts(int playouts) {
        this.playouts = playouts;
    }

    private int playouts;

    public void incrementPlayouts() {
        this.playouts++;
    }

    public void addWins(int result) {
        this.wins += result;
    }
}
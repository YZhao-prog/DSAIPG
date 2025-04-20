package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.Move;

public class ConnectFourMove implements Move<ConnectFourGame> {
    private final int column;
    private final int player;

    public ConnectFourMove(int column, int player) {
        this.column = column;
        this.player = player;
    }

    public int getColumn() {
        return column;
    }

    @Override
    public int player() {
        return player;
    }

    @Override
    public String toString() {
        return "Player " + player + " drops in column " + column;
    }
}
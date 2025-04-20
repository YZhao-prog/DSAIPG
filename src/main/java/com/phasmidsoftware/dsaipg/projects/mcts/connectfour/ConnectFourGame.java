package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;

import java.util.Arrays;
import java.util.Random;

public class ConnectFourGame implements Game<ConnectFourGame> {
    public static final int ROWS = 6;
    public static final int COLS = 7;

    @Override
    public State<ConnectFourGame> start() {
        int[][] board = new int[ROWS][COLS];
        for (int i = 0; i < ROWS; i++) Arrays.fill(board[i], -1); // use -1 to indicate empty
        return new ConnectFourState(this, board, 0, new Random());
    }

    @Override
    public int opener() {
        return 0;
    }
}

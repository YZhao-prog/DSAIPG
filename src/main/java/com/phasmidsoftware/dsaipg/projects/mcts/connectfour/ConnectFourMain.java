package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;

public class ConnectFourMain {
    public static void main(String[] args) {
        ConnectFourGame game = new ConnectFourGame();
        State<ConnectFourGame> state = game.start();
        MCTS<ConnectFourGame> mcts = new MCTS<>(1000);

        int round = 0;
        while (!state.isTerminal()) {
            Move<ConnectFourGame> move = mcts.choose(state);
            System.out.println("Round " + round + ": Player " + state.player() + " -> " + move);
            state = state.next(move);
            System.out.println(state);
            round++;
        }

        System.out.println("Game Over!");
        state.winner().ifPresentOrElse(
                w -> System.out.println("Winner: Player " + w),
                () -> System.out.println("It's a draw!")
        );
    }
}
package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;

public class ConnectFourSimulationStats {
    public static void main(String[] args) {
        int win0 = 0, win1 = 0, draw = 0;
        int simulations = 500; // Run 500 games

        for (int i = 0; i < simulations; i++) {
            ConnectFourGame game = new ConnectFourGame();
            State<ConnectFourGame> state = game.start();
            MCTS<ConnectFourGame> mcts = new MCTS<>(100);

            while (!state.isTerminal()) {
                Move<ConnectFourGame> move = mcts.choose(state);
                state = state.next(move);
            }

            if (state.winner().isPresent()) {
                int w = state.winner().get();
                if (w == 0) win0++;
                else win1++;
            } else draw++;
        }

        System.out.println("Total Games: " + simulations);
        System.out.println("Player 0 Wins: " + win0);
        System.out.println("Player 1 Wins: " + win1);
        System.out.println("Draws: " + draw);
    }
}
package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;
import org.junit.Test;

import java.util.Iterator;

public class ConnectFourSimulationStats {

    interface Strategy {
        Move<ConnectFourGame> choose(State<ConnectFourGame> state);
    }

    static Strategy mcts(int simulations) {
        return state -> new MCTS<ConnectFourGame>(simulations).choose(state);
    }

    static Strategy random() {
        return state -> {
            Iterator<Move<ConnectFourGame>> it = state.moveIterator(state.player());
            return it.hasNext() ? it.next() : null;
        };
    }

    static void simulate(String label, int games, Strategy p0, Strategy p1) {
        int win0 = 0, win1 = 0, draw = 0;

        for (int i = 0; i < games; i++) {
            ConnectFourGame game = new ConnectFourGame();
            State<ConnectFourGame> state = game.start();

            while (!state.isTerminal()) {
                Move<ConnectFourGame> move = (state.player() == 0) ? p0.choose(state) : p1.choose(state);
                if (move == null) break;
                state = state.next(move);
            }

            if (state.winner().isPresent()) {
                if (state.winner().get() == 0) win0++;
                else win1++;
            } else {
                draw++;
            }
        }

        System.out.printf("Strategy: %s\n", label);
        System.out.printf("Player 0 Wins: %d (%.1f%%)\n", win0, win0 * 100.0 / games);
        System.out.printf("Player 1 Wins: %d (%.1f%%)\n", win1, win1 * 100.0 / games);
        System.out.printf("Draws:        %d (%.1f%%)\n", draw, draw * 100.0 / games);
        System.out.println("---------------------------");
    }

    public static void main(String[] args) {
        int rounds = 300;

        simulate("MCTS(100) vs MCTS(100)", rounds, mcts(100), mcts(100));
        simulate("MCTS(100) vs Random",     rounds, mcts(100), random());
        simulate("Random vs MCTS(100)",     rounds, random(), mcts(100));
        simulate("Random vs Random",        rounds, random(), random());
        simulate("MCTS(10) vs MCTS(200)",   rounds, mcts(10), mcts(200));
    }
}
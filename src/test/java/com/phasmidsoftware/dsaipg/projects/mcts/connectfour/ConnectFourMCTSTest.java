package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class ConnectFourMCTSTest {

    @Test
    public void testChooseMove() {
        ConnectFourGame game = new ConnectFourGame();
        State<ConnectFourGame> state = game.start();
        MCTS<ConnectFourGame> mcts = new MCTS<>(100);
        Move<ConnectFourGame> move = mcts.choose(state);
        assertNotNull(move);
        assertTrue(move.player() == 0);
    }
}
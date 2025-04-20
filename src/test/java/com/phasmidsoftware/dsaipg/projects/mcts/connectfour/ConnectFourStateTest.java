package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;
import org.junit.Test;

import java.util.Collection;

import static org.junit.Assert.*;
public class ConnectFourStateTest {

    @Test
    public void testInitialMoves() {
        ConnectFourGame game = new ConnectFourGame();
        State<ConnectFourGame> state = game.start();
        Collection<Move<ConnectFourGame>> moves = state.moves(state.player());
        assertEquals(7, moves.size());
    }

    @Test
    public void testNextState() {
        ConnectFourGame game = new ConnectFourGame();
        State<ConnectFourGame> state = game.start();
        Move<ConnectFourGame> move = new ConnectFourMove(3, 0);
        State<ConnectFourGame> next = state.next(move);
        assertNotNull(next);
        assertEquals(1, next.player());
    }

    @Test
    public void testWinner() {
        ConnectFourGame game = new ConnectFourGame();
        int[][] board = new int[6][7];
        for (int i = 0; i < 6; i++) java.util.Arrays.fill(board[i], -1);
        board[5][0] = board[4][0] = board[3][0] = board[2][0] = 0;
        State<ConnectFourGame> state = new ConnectFourState(game, board, 1, new java.util.Random());
        assertTrue(state.isTerminal());
        assertEquals(java.util.Optional.of(0), state.winner());
    }
}
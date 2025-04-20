package com.phasmidsoftware.dsaipg.projects.mcts.connectfour;

import com.phasmidsoftware.dsaipg.projects.mcts.core.*;

import java.util.*;

public class ConnectFourState implements State<ConnectFourGame> {
    private final int[][] board;
    private final int player;
    private final ConnectFourGame game;
    private final Random random;

    private Boolean terminal = null;
    private Optional<Integer> winner = null;

    public ConnectFourState(ConnectFourGame game, int[][] board, int player, Random random) {
        this.game = game;
        this.board = board;
        this.player = player;
        this.random = random;
    }

    @Override
    public ConnectFourGame game() {
        return game;
    }

    @Override
    public boolean isTerminal() {
        if (terminal != null) return terminal;
        if (winner().isPresent()) return terminal = true;
        for (int c = 0; c < ConnectFourGame.COLS; c++)
            if (board[0][c] == -1) return terminal = false;
        return terminal = true;
    }

    @Override
    public int player() {
        return player;
    }

    @Override
    public Optional<Integer> winner() {
        if (winner != null) return winner;
        int rows = board.length, cols = board[0].length;
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                int p = board[r][c];
                if (p == -1) continue;
                if (c + 3 < cols && p == board[r][c + 1] && p == board[r][c + 2] && p == board[r][c + 3])
                    return winner = Optional.of(p);
                if (r + 3 < rows && p == board[r + 1][c] && p == board[r + 2][c] && p == board[r + 3][c])
                    return winner = Optional.of(p);
                if (r + 3 < rows && c + 3 < cols && p == board[r + 1][c + 1] && p == board[r + 2][c + 2] && p == board[r + 3][c + 3])
                    return winner = Optional.of(p);
                if (r + 3 < rows && c - 3 >= 0 && p == board[r + 1][c - 1] && p == board[r + 2][c - 2] && p == board[r + 3][c - 3])
                    return winner = Optional.of(p);
            }
        }
        return winner = Optional.empty();
    }

    @Override
    public Random random() {
        return random;
    }

    @Override
    public Collection<Move<ConnectFourGame>> moves(int player) {
        if (isTerminal()) return Collections.emptyList();
        List<Move<ConnectFourGame>> list = new ArrayList<>();
        for (int c = 0; c < ConnectFourGame.COLS; c++) {
            if (board[0][c] == -1) list.add(new ConnectFourMove(c, player));
        }
        return list;
    }

    @Override
    public State<ConnectFourGame> next(Move<ConnectFourGame> move) {
        ConnectFourMove m = (ConnectFourMove) move;
        int[][] newBoard = new int[ConnectFourGame.ROWS][ConnectFourGame.COLS];
        for (int i = 0; i < ConnectFourGame.ROWS; i++)
            newBoard[i] = board[i].clone();

        for (int r = ConnectFourGame.ROWS - 1; r >= 0; r--) {
            if (newBoard[r][m.getColumn()] == -1) {
                newBoard[r][m.getColumn()] = m.player();
                break;
            }
        }
        return new ConnectFourState(game, newBoard, 1 - m.player(), random);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int[] row : board) {
            for (int cell : row) {
                if (cell == -1) sb.append(". ");
                else if (cell == 0) sb.append("O "); // Player 0
                else if (cell == 1) sb.append("X "); // Player 1
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}

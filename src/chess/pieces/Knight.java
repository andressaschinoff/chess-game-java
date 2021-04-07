package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

import java.util.HashMap;

public class Knight extends ChessPiece {
    public Knight(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "N";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        HashMap<String, Integer[]> directions = new HashMap<>();
        directions.put("one", new Integer[]{-1, -2});
        directions.put("two", new Integer[]{-1, 2});
        directions.put("three", new Integer[]{1, -2});
        directions.put("four", new Integer[]{1, 2});
        directions.put("five", new Integer[]{-2, -1});
        directions.put("six", new Integer[]{-2, 1});
        directions.put("seven", new Integer[]{2, -1});
        directions.put("eight", new Integer[]{2, 1});

        for (String direction : directions.keySet()) {
            directionToMove(p, mat, directions.get(direction));
        }


        return mat;
    }

    private boolean canMove(Position position) {
        ChessPiece p = (ChessPiece) getBoard().piece(position);
        return p == null || p.getColor() != getColor();
    }

    private void directionToMove(Position p, boolean[][] mat, Integer[] rowAndColumn) {
        Integer row = rowAndColumn[0];
        Integer column = rowAndColumn[1];

        p.setValues(position.getRow() + row, position.getColumn() + column);
        if (getBoard().positionExists(p) && canMove(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }
}

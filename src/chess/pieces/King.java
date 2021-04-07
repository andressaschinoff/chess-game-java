package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

import java.util.HashMap;

public class King extends ChessPiece {
    public King(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "K";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        HashMap<String, Integer[]> directions = new HashMap<>();
        directions.put("above", new Integer[]{-1, 0});
        directions.put("below", new Integer[]{1, 0});
        directions.put("left", new Integer[]{0, -1});
        directions.put("right", new Integer[]{0, 1});
        directions.put("northwest", new Integer[]{-1, -1});
        directions.put("northeast", new Integer[]{-1, 1});
        directions.put("southwest", new Integer[]{1, -1});
        directions.put("southeast", new Integer[]{1, 1});

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

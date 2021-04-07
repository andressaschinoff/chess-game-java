package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

import java.util.HashMap;
import java.util.Map;

public class Bishop extends ChessPiece {
    public Bishop(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "B";
    }
    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        Map<String, Integer[]> directions = new HashMap<>();
        directions.put("northwest", new Integer[]{-1, -1});
        directions.put("northeast", new Integer[]{-1, 1});
        directions.put("southwest", new Integer[]{1, -1});
        directions.put("southeast", new Integer[]{1, 1});

        for (String direction : directions.keySet()) {
            directionToMove(p, mat, directions.get(direction));
        }

        return mat;
    }

    private void directionToMove(Position p, boolean[][] mat, Integer[] rowAndColumn) {
        Integer row = rowAndColumn[0];
        Integer column = rowAndColumn[1];

        p.setValues(position.getRow() + row, position.getColumn() + column);
        while (getBoard().positionExists(p) && !getBoard().thereIsAPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
            p.setValues(p.getRow() + row, p.getColumn() + column);
        }
        if (getBoard().positionExists(p) && isThereOpponentPiece(p)) {
            mat[p.getRow()][p.getColumn()] = true;
        }
    }
}

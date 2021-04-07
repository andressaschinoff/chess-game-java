package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

import java.util.HashMap;
import java.util.Map;

public class Rook extends ChessPiece {

    public Rook(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "R";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];
        Position p = new Position(0, 0);

        Map<String, Integer[]> directions = new HashMap<>();
        directions.put("above", new Integer[]{-1, 0});
        directions.put("left", new Integer[]{0, -1});
        directions.put("right", new Integer[]{0, 1});
        directions.put("below", new Integer[]{1, 0});

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

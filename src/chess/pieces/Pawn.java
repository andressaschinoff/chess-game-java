package chess.pieces;

import boardgame.Board;
import boardgame.Position;
import chess.ChessPiece;
import chess.Color;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pawn extends ChessPiece {
    public Pawn(Board board, Color color) {
        super(board, color);
    }

    @Override
    public String toString() {
        return "P";
    }

    @Override
    public boolean[][] possibleMoves() {
        boolean[][] mat = new boolean[getBoard().getRows()][getBoard().getColumns()];

        Position p = new Position(0, 0);

        Map<String, Integer[]> directions = new HashMap<>();

        directions.put("forward", new Integer[]{1, 0});
        directions.put("left", new Integer[]{1, -1});
        directions.put("right", new Integer[]{1, 1});

        for (String direction : directions.keySet()) {
            directionToMove(p, mat, directions.get(direction), getColor(), direction.contains("forward"));
        }

        return mat;
    }

    private void directionToMove(Position p, boolean[][] mat, Integer[] rowAndColumn, Color color, boolean isForward) {
        Integer row = (color == Color.BLACK) ? rowAndColumn[0] : -rowAndColumn[0];
        Integer column = rowAndColumn[1];
        p.setValues(position.getRow() + row, position.getColumn() + column);
        boolean isCountZero = getMoveCount() == 0;
        boolean isTrue = isForward ? getBoard().positionExists(p) && !getBoard().thereIsAPiece(p) : getBoard().positionExists(p) && isThereOpponentPiece(p);

        if (isTrue) {
            mat[p.getRow()][p.getColumn()] = true;
        }

        row += row;
        Position p2 = new Position(position.getRow() + row, position.getColumn() + column);

        if (isTrue && getBoard().positionExists(p2) && !getBoard().thereIsAPiece(p2) && isCountZero) {
            mat[p2.getRow()][p2.getColumn()] = true;
        }

    }
}

package chess;

import boardgame.Board;
import boardgame.Piece;
import boardgame.Position;
import chess.pieces.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChessMatch {

    private Integer turn;
    private Color currentPlayer;
    private Board board;
    private boolean check;
    private boolean checkMate;

    private List<Piece> piecesOnTheBoard = new ArrayList<>();
    private List<Piece> capturedPieces = new ArrayList<>();

    public ChessMatch() {
        this.board = new Board(8, 8);
        this.turn = 1;
        currentPlayer = Color.WHITE;
        initialSetup();
    }

    public Integer getTurn() {
        return this.turn;
    }

    public Color getCurrentPlayer() {
        return this.currentPlayer;
    }

    public boolean getCheck() {
        return this.check;
    }

    public boolean getCheckMate() {
        return this.checkMate;
    }

    public ChessPiece[][] getPieces() {
        ChessPiece[][] mat = new ChessPiece[board.getRows()][board.getColumns()];

        for (int i = 0; i < board.getRows(); i++) {
            for (int j = 0; j < board.getColumns(); j++) {
                mat[i][j] = (ChessPiece) board.piece(i, j);
            }
        }

        return mat;
    }

    public boolean[][] possibleMoves(ChessPosition sourcePosition) {
        Position position = sourcePosition.toPosition();
        validateSourcePosition(position);
        return board.piece(position).possibleMoves();
    }

    public ChessPiece performChessMove(ChessPosition sourcePosition, ChessPosition targetPosition) {
        Position source = sourcePosition.toPosition();
        Position target = targetPosition.toPosition();
        validateSourcePosition(source);
        validateTargetPosition(source, target);
        Piece capturedPiece = makeMove(source, target);

        if (testCheck(currentPlayer)) {
            undoMove(source, target, capturedPiece);
            throw new ChessException("You can't put yourself in check");
        }

        check = testCheck(opponent(currentPlayer));
        checkMate = testCheckMate(opponent(currentPlayer));
        if (!checkMate) {
            nextTurn();
        }

        return (ChessPiece) capturedPiece;
    }

    private Piece makeMove(Position source, Position target) {
        ChessPiece p = (ChessPiece) board.removePiece(source);
        p.increaseMoveCount();
        Piece capturedPiece = board.removePiece(target);
        board.placePiece(p, target);

        if (capturedPiece != null) {
            piecesOnTheBoard.remove(capturedPiece);
            capturedPieces.add(capturedPiece);
        }

        return capturedPiece;
    }

    private void undoMove(Position source, Position target, Piece capturedPiece) {
        ChessPiece p = (ChessPiece) board.removePiece(target);
        p.decreaseMoveCount();
        board.placePiece(p, source);

        if (capturedPiece != null) {
            board.placePiece(capturedPiece, target);
            capturedPieces.remove(capturedPiece);
            piecesOnTheBoard.add(capturedPiece);
        }
    }

    private void validateSourcePosition(Position position) {
        if (!board.thereIsAPiece(position)) {
            throw new ChessException("There is no piece on source position");
        }
        if (currentPlayer != ((ChessPiece) board.piece(position)).getColor()) {
            throw new ChessException("The chosen piece is not yours");
        }
        if (!board.piece(position).isThereAnyPossibleMove()) {
            throw new ChessException("There is no possible moves for the chosen piece");
        }
    }

    private void validateTargetPosition(Position source, Position target) {
        if (!board.piece(source).possibleMoves(target)) {
            throw new ChessException("The chosen piece can't move to target position");
        }
    }

    private void nextTurn() {
        turn++;
        currentPlayer = currentPlayer == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private Color opponent(Color color) {
        return color == Color.WHITE ? Color.BLACK : Color.WHITE;
    }

    private ChessPiece king(Color color) {
        List<Piece> list = piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == color)
                .collect(Collectors.toList());
        for (Piece p : list) {
            if (p instanceof King) {
                return (ChessPiece) p;
            }
        }
        throw new IllegalStateException("There is no " + color + " king on the board");
    }

    private void placeNewPiece(char column, int row, ChessPiece piece) {
        board.placePiece(piece, new ChessPosition(column, row).toPosition());
        piecesOnTheBoard.add(piece);
    }

    private boolean testCheck(Color color) {
        Position kingPosition = king(color).getChessPosition().toPosition();
        List<Piece> opponentPieces = piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == opponent(color))
                .collect(Collectors.toList());

        for (Piece p : opponentPieces) {
            boolean[][] mat = p.possibleMoves();

            if (mat[kingPosition.getRow()][kingPosition.getColumn()]) {
                return true;
            }
        }
        return false;
    }

    private boolean testCheckMate(Color color) {
        if (!testCheck(color)) {
            return false;
        }

        List<Piece> currentPlayerPieces = piecesOnTheBoard.stream()
                .filter(piece -> ((ChessPiece) piece).getColor() == color)
                .collect(Collectors.toList());

        for (Piece p : currentPlayerPieces) {
            boolean[][] pieceMoves = p.possibleMoves();

            for (int i = 0; i < board.getRows(); i++) {
                for (int j = 0; j < board.getColumns(); j++) {
                    if (pieceMoves[i][j]) {
                        Position source = ((ChessPiece) p).getChessPosition().toPosition();
                        Position target = new Position(i, j);

                        Piece capturedPiece = makeMove(source, target);
                        boolean testCheck = testCheck(color);
                        undoMove(source, target, capturedPiece);

                        if (!testCheck) {
                            return false;
                        }
                    }
                }

            }
        }
        return true;
    }

    private void initialSetup() {
        Map<Color, Integer[]> colors = new HashMap<>();
        colors.put(Color.WHITE, new Integer[]{1, 2});
        colors.put(Color.BLACK, new Integer[]{8, 7});

        for (Color color : colors.keySet()) {
            Integer rowOne = colors.get(color)[0];
            Integer rowTwo = colors.get(color)[1];
            placeNewPiece('a', rowOne, new Rook(board, color));
            placeNewPiece('b', rowOne, new Knight(board, color));
            placeNewPiece('c', rowOne, new Bishop(board, color));
            placeNewPiece('d', rowOne, new Queen(board, color));
            placeNewPiece('e', rowOne, new King(board, color));
            placeNewPiece('f', rowOne, new Bishop(board, color));
            placeNewPiece('g', rowOne, new Knight(board, color));
            placeNewPiece('h', rowOne, new Rook(board, color));
            placeNewPiece('a', rowTwo, new Pawn(board, color));
            placeNewPiece('b', rowTwo, new Pawn(board, color));
            placeNewPiece('c', rowTwo, new Pawn(board, color));
            placeNewPiece('d', rowTwo, new Pawn(board, color));
            placeNewPiece('e', rowTwo, new Pawn(board, color));
            placeNewPiece('f', rowTwo, new Pawn(board, color));
            placeNewPiece('g', rowTwo, new Pawn(board, color));
            placeNewPiece('h', rowTwo, new Pawn(board, color));
        }
    }
}

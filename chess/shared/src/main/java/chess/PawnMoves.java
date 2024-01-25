package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    private final Collection<ChessMove> moves = new ArrayList<>();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col);
//        checkMoves(board, position, piece, row, col, 1, -1);
//        checkMoves(board, position, piece, row, col, 1, 0);
//        checkMoves(board, position, piece, row, col, 2, 0);
        return moves;
    }

    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //WhiteForward
            ChessPosition next = new ChessPosition(row + 1, col);
            if (board.inBounds(row + 1, col)) {
                if (board.getPiece(next) == null) {
                    if (next.getRow() == 8) {
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.QUEEN));
                    } else if (next.getRow() != 8) {
                        moves.add(new ChessMove(position, next, null));
                    }
                }
            }
            next = new ChessPosition(row + 2, col);
            if (board.inBounds(row + 2, col)) {
                if (row == 2 && board.getPiece(next) == null) {
                    moves.add(new ChessMove(position, next, null));
                }
            }
            //WhiteCapture
            next = new ChessPosition(row + 1, col + 1);
            if (board.inBounds(row + 1, col + 1)) {
                if (board.getPiece(next) != null && board.getPiece(next).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (next.getRow() == 8) {
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.QUEEN));
                    } else {
                        moves.add(new ChessMove(position, next, null));
                    }
                }
            }
            next = new ChessPosition(row + 1, col - 1);
            if (board.inBounds(row + 1, col - 1)) {
                if (board.getPiece(next) != null && board.getPiece(next).getTeamColor() == ChessGame.TeamColor.BLACK) {
                    if (next.getRow() == 8) {
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.ROOK));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.KNIGHT));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.BISHOP));
                        moves.add(new ChessMove(position, next, ChessPiece.PieceType.QUEEN));
                    } else {
                        moves.add(new ChessMove(position, next, null));
                    }
                }
            }
        }
    }
}

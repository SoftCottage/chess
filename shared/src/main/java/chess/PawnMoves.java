package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    private final ArrayList<ChessMove> moves = new ArrayList<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col);
        return moves;
    }

    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col) {
        if (piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
            //WhiteForward
            ChessPosition next = new ChessPosition(row + 1, col);
            if (board.inBounds(row + 1, col)) {
                if (board.getPiece(next) == null) {
                    if (next.getRow() == 8) {
                        Promote(position, next);
                    } else if (next.getRow() != 8) {
                        moves.add(new ChessMove(position, next, null));
                    }
                }
            }
            //WhiteStart
            next = new ChessPosition(row + 2, col);
            if (board.inBounds(row + 2, col)) {
                if (row == 2 && board.getPiece(next) == null && board.getPiece(new ChessPosition(row + 1, col)) == null) {
                    moves.add(new ChessMove(position, next, null));
                }
            }
            //WhiteCapture
            for (int i = -1; i <= 1; i = i + 2) {
                next = new ChessPosition(row + 1, col + i);
                if (board.inBounds(row + 1, col + i)) {
                    if (board.getPiece(next) != null && board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                        if (next.getRow() == 8) {
                            Promote(position, next);
                        } else {
                            moves.add(new ChessMove(position, next, null));
                        }
                    }
                }
            }
        }

        if (piece.getTeamColor() == ChessGame.TeamColor.BLACK) {
            //BlackForward
            ChessPosition next = new ChessPosition(row - 1, col);
            if (board.inBounds(row - 1, col)) {
                if (board.getPiece(next) == null) {
                    if (next.getRow() == 1) {
                        Promote(position, next);
                    } else if (next.getRow() != 1) {
                        moves.add(new ChessMove(position, next, null));
                    }
                }
            }
            //BlackStart
            next = new ChessPosition(row - 2, col);
            if (board.inBounds(row - 2, col)) {
                if (row == 7 && board.getPiece(next) == null && board.getPiece(new ChessPosition(row - 1, col)) == null) {
                    moves.add(new ChessMove(position, next, null));
                }
            }
            //BlackCapture
            for (int i = -1; i <= 1; i = i + 2) {

                next = new ChessPosition(row - 1, col + i);
                if (board.inBounds(row - 1, col + i)) {
                    if (board.getPiece(next) != null && board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                        if (next.getRow() == 1) {
                            Promote(position, next);
                        } else {
                            moves.add(new ChessMove(position, next, null));
                        }
                    }
                }
            }
        }
    }
    public void Promote (ChessPosition position, ChessPosition next) {
        moves.add(new ChessMove(position, next, ChessPiece.PieceType.BISHOP));
        moves.add(new ChessMove(position, next, ChessPiece.PieceType.ROOK));
        moves.add(new ChessMove(position, next, ChessPiece.PieceType.KNIGHT));
        moves.add(new ChessMove(position, next, ChessPiece.PieceType.QUEEN));
    }
}
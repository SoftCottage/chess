package chess;

import java.util.ArrayList;
import java.util.Collection;
public class RookMoves {
    private final Collection<ChessMove> moves = new ArrayList<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col);
        return moves;
    }
    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col) {
        //Up
        int a = row + 1;
        while (board.inBounds(a, col)) {
            ChessPosition next = new ChessPosition(a, col);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                a++;
            }
            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
                break;
            }
            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
                break;
            }
        }
        //Down
        int b = row - 1;
        while (board.inBounds(b, col)) {
            ChessPosition next = new ChessPosition(b, col);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                b--;
            }
            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
                break;
            }
            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
                break;
            }
        }
        //Right
        int c = col + 1;
        while (board.inBounds(row, c)) {
            ChessPosition next = new ChessPosition(row, c);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                c++;
            }
            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
                break;
            }
            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
                break;
            }
        }
        //Left
        int d = col - 1;
        while (board.inBounds(row, d)) {
            ChessPosition next = new ChessPosition(row, d);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                d--;
            }
            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
                break;
            }
            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
                break;
            }
        }
    }
}


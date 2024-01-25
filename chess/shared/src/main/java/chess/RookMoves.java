package chess;

import java.util.ArrayList;
import java.util.Collection;
public class RookMoves {
    ArrayList<ChessMove> moves = new ArrayList<>();


    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col) {
        //Up
        int i = row + 1;
        while (board.inBounds(i, col)) {
            ChessPosition next = new ChessPosition(i, col);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                i++;
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
        int j = row - 1;
        while (board.inBounds(j, col)) {
            ChessPosition next = new ChessPosition(j, col);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                j--;
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
        int k = col + 1;
        while (board.inBounds(row, k)) {
            ChessPosition next = new ChessPosition(row, k);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                k++;
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
        int l = col - 1;
        while (board.inBounds(row, l)) {
            ChessPosition next = new ChessPosition(row, l);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                l--;
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
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col);
        return moves;
    }

}


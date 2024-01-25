package chess;

import java.util.ArrayList;
import java.util.Collection;
public class BishopMoves {

    private final ArrayList<ChessMove> moves = new ArrayList<>();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col);
        return moves;
    }

    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col) {
        //UpRight
        int i = row + 1;
        int j = col + 1;
        while (board.inBounds(i, j)) {
            ChessPosition next = new ChessPosition(i, j);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                i++;
                j++;
            }
            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
                break;
            }
            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
                break;
            }
        }
        //UpLeft
        int k = row + 1;
        int l = col - 1;
        while (board.inBounds(k, l)) {
            ChessPosition next = new ChessPosition(k, l);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                k++;
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
        //DownRight
        int m = row - 1;
        int n = col + 1;
        while (board.inBounds(m, n)) {
            ChessPosition next = new ChessPosition(m, n);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                m--;
                n++;
            }
            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
                break;
            }
            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
                break;
            }
        }
        //UpRight
        int o = row - 1;
        int p = col - 1;
        while (board.inBounds(o, p)) {
            ChessPosition next = new ChessPosition(o, p);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                o--;
                p--;
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

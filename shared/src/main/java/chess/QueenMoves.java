package chess;

import java.util.ArrayList;
import java.util.Collection;
public class QueenMoves {
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
        //UpRight
        int e = row + 1;
        int f = col + 1;
        while (board.inBounds(e, f)) {
            ChessPosition next = new ChessPosition(e, f);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                e++;
                f++;
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
        int g = row + 1;
        int h = col - 1;
        while (board.inBounds(g, h)) {
            ChessPosition next = new ChessPosition(g, h);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                g++;
                h--;
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
        int i = row - 1;
        int j = col + 1;
        while (board.inBounds(i, j)) {
            ChessPosition next = new ChessPosition(i, j);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                i--;
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
        //UpRight
        int k = row - 1;
        int l = col - 1;
        while (board.inBounds(k, l)) {
            ChessPosition next = new ChessPosition(k, l);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                k--;
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
}


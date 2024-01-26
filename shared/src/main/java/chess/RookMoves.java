package chess;

import java.util.ArrayList;
import java.util.Collection;
public class RookMoves {
    private final ArrayList<ChessMove> moves = new ArrayList<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col, 1, 0);
        checkMoves(board, position, piece, row, col, -1, 0);
        checkMoves(board, position, piece, row, col, 0, 1);
        checkMoves(board, position, piece, row, col, 0, -1);

        return moves;
    }
    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col, int rowMove, int colMove) {
        //UpRight
        int e = row + rowMove;
        int f = col + colMove;
        while (board.inBounds(e, f)) {
            ChessPosition next = new ChessPosition(e, f);
            if (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                e = e + rowMove;
                f = f + colMove;
            } else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
                break;
            } else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) break;
        }
    }
}
/*    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col, int rowMove, int colMove) {
        //Up
        int a = row + rowMove;
        while (board.inBounds(a, col)) {
            ChessPosition next = new ChessPosition(a, col);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                a = a + rowMove;
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
//        int b = row - 1;
//        while (board.inBounds(b, col)) {
//            ChessPosition next = new ChessPosition(b, col);
//            if  (board.getPiece(next) == null) {
//                moves.add(new ChessMove(position, next, null));
//                b--;
//            }
//            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
//                moves.add(new ChessMove(position, next, null));
//                break;
//            }
//            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
//                break;
//            }
//        }
        //Right
        int c = col + colMove;
        while (board.inBounds(row, c)) {
            ChessPosition next = new ChessPosition(row, c);
            if  (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
                c = c + rowMove;
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
//        int d = col - 1;
//        while (board.inBounds(row, d)) {
//            ChessPosition next = new ChessPosition(row, d);
//            if  (board.getPiece(next) == null) {
//                moves.add(new ChessMove(position, next, null));
//                d--;
//            }
//            else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
//                moves.add(new ChessMove(position, next, null));
//                break;
//            }
//            else if (board.getPiece(next).getTeamColor() == piece.getTeamColor()) {
//                break;
//            }
//        }
    }
}
*/



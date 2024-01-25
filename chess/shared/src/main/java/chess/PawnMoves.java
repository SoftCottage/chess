package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMoves {
    private final Collection<ChessMove> moves = new ArrayList<>();

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col, 1, 1);
        checkMoves(board, position, piece, row, col, 1, -1);
        checkMoves(board, position, piece, row, col, 1, 0);
        checkMoves(board, position, piece, row, col, 2, 0);
        return moves;
    }

    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col, int rowMove, int colMove) {
        int a = row + rowMove;
        int b = col + colMove;
        ChessPosition next = new ChessPosition(a, b);

        if (board.inBounds(a, b)) {
            //Capture
            if (board.getPiece(next)!= null && board.getPiece(next).getTeamColor() == ChessGame.TeamColor.BLACK && colMove != 0) {
                moves.add(new ChessMove(position, next, null));
            }
            //White2
            if (row == 2 && piece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                moves.add(new ChessMove(position, next, null));
            }
            if (board.getPiece(next) == null) {
                ChessPosition one = new ChessPosition(1, 0);
                moves.add(new ChessMove(position, one, null));
                ChessPosition two = new ChessPosition(2, 0);
                moves.add(new ChessMove(position, one, null));
            }
        }
    }
}

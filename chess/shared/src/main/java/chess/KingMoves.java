package chess;

import java.util.ArrayList;
import java.util.Collection;
public class KingMoves {

    private final Collection<ChessMove> moves = new ArrayList<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col, 1, 1);
        checkMoves(board, position, piece, row, col, 1, -1);
        checkMoves(board, position, piece, row, col, -1, 1);
        checkMoves(board, position, piece, row, col, 1, 0);
        checkMoves(board, position, piece, row, col, -1, -1);
        checkMoves(board, position, piece, row, col, -1, 0);
        checkMoves(board, position, piece, row, col, 0, 1);
        checkMoves(board, position, piece, row, col, 0, -1);
        return moves;
    }

    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col, int rowMove, int colMove) {
        int a = row + rowMove;
        int b = col + colMove;
        ChessPosition next = new ChessPosition(a, b);
        if (board.inBounds(a, b)) {
            if (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
            } else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
            }
        }
    }
}

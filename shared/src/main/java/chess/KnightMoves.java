package chess;

import java.util.ArrayList;
import java.util.Collection;
public class KnightMoves {
    private final ArrayList<ChessMove> moves = new ArrayList<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col, 2, 1);
        checkMoves(board, position, piece, row, col, 2, -1);
        checkMoves(board, position, piece, row, col, -2, 1);
        checkMoves(board, position, piece, row, col, -2, -1);
        checkMoves(board, position, piece, row, col, 1, 2);
        checkMoves(board, position, piece, row, col, 1, -2);
        checkMoves(board, position, piece, row, col, -1, 2);
        checkMoves(board, position, piece, row, col, -1, -2);
        return moves;
    }
    public void checkMoves(ChessBoard board, ChessPosition position, ChessPiece piece, int row, int col, int rowMove, int colMove) {
        int i = row + rowMove;
        int j = col + colMove;
        ChessPosition next = new ChessPosition(i, j);
        if (board.inBounds(i, j)) {
            if (board.getPiece(next) == null) {
                moves.add(new ChessMove(position, next, null));
            } else if (board.getPiece(next).getTeamColor() != piece.getTeamColor()) {
                moves.add(new ChessMove(position, next, null));
            }
        }
    }
}
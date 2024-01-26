package chess;

import java.util.ArrayList;
import java.util.Collection;
public class BishopMoves {
    private final ArrayList<ChessMove> moves = new ArrayList<>();
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition position) {
        ChessPiece piece = board.getPiece(position);
        int row = position.getRow();
        int col = position.getColumn();
        checkMoves(board, position, piece, row, col, 1, 1);
        checkMoves(board, position, piece, row, col, 1, -1);
        checkMoves(board, position, piece, row, col, -1, 1);
        checkMoves(board, position, piece, row, col, -1, -1);

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

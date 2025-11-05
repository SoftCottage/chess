package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

public class ChessPiece {

    private final ChessGame.TeamColor pieceColor;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    public enum PieceType {
        KING, QUEEN, BISHOP, KNIGHT, ROOK, PAWN
    }

    public ChessGame.TeamColor getTeamColor() { return pieceColor; }
    public PieceType getPieceType() { return type; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition pos) {
        return switch (type) {
            case KING -> kingMoves(board, pos);
            case QUEEN -> slidingMoves(board, pos, new int[][]{
                    {1, 0}, {-1, 0}, {0, 1}, {0, -1},
                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            });
            case BISHOP -> slidingMoves(board, pos, new int[][]{
                    {1, 1}, {1, -1}, {-1, 1}, {-1, -1}
            });
            case ROOK -> slidingMoves(board, pos, new int[][]{
                    {1, 0}, {-1, 0}, {0, 1}, {0, -1}
            });
            case KNIGHT -> stepMoves(board, pos, new int[][]{
                    {2, 1}, {2, -1}, {-2, 1}, {-2, -1},
                    {1, 2}, {1, -2}, {-1, 2}, {-1, -2}
            });
            case PAWN -> pawnMoves(board, pos);
        };
    }

    private Collection<ChessMove> slidingMoves(ChessBoard board, ChessPosition pos, int[][] directions) {
        List<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow();
        int col = pos.getColumn();

        for (int[] dir : directions) {
            int r = row + dir[0];
            int c = col + dir[1];

            while (board.inBounds(r, c)) {
                ChessPosition next = new ChessPosition(r, c);
                ChessPiece target = board.getPiece(next);

                if (target == null) {
                    moves.add(new ChessMove(pos, next, null));
                } else {
                    if (target.getTeamColor() != pieceColor) {
                        moves.add(new ChessMove(pos, next, null));
                    }
                    break;
                }
                r += dir[0];
                c += dir[1];
            }
        }
        return moves;
    }


    private Collection<ChessMove> stepMoves(ChessBoard board, ChessPosition pos, int[][] directions) {
        List<ChessMove> moves = new ArrayList<>();
        int row = pos.getRow(), col = pos.getColumn();

        for (int[] dir : directions) {
            int r = row + dir[0], c = col + dir[1];
            if (!board.inBounds(r, c)) {
                continue;
            }

            ChessPosition next = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(next);
            if (target == null || target.getTeamColor() != pieceColor) {
                moves.add(new ChessMove(pos, next, null));
            }
        }
        return moves;
    }

    private Collection<ChessMove> kingMoves(ChessBoard board, ChessPosition pos) {
        int[][] directions = {
                {1, 1}, {1, 0}, {1, -1},
                {0, 1}, {0, -1},
                {-1, 1}, {-1, 0}, {-1, -1}
        };
        return stepMoves(board, pos, directions);
    }

    private Collection<ChessMove> pawnMoves(ChessBoard board, ChessPosition pos) {
        List<ChessMove> moves = new ArrayList<>();
        int dir = (pieceColor == ChessGame.TeamColor.WHITE) ? 1 : -1;
        int row = pos.getRow(), col = pos.getColumn();

        int nextRow = row + dir;

        // Forward one square
        if (board.inBounds(nextRow, col) && board.getPiece(new ChessPosition(nextRow, col)) == null) {
            ChessPosition next = new ChessPosition(nextRow, col);
            if (isPromotionRank(nextRow)) {
                addPromotions(moves, pos, next); // multiple promotion choices
            } else {
                moves.add(new ChessMove(pos, next, null));
            }
        }

        // Double move from start
        int startRow = (pieceColor == ChessGame.TeamColor.WHITE) ? 2 : 7;
        int twoRow = row + (2 * dir);
        if (row == startRow && board.inBounds(twoRow, col)
                && board.getPiece(new ChessPosition(nextRow, col)) == null
                && board.getPiece(new ChessPosition(twoRow, col)) == null) {
            moves.add(new ChessMove(pos, new ChessPosition(twoRow, col), null));
        }

        // Diagonal captures
        int[][] captures = {{dir, 1}, {dir, -1}};
        for (int[] cap : captures) {
            int r = row + cap[0], c = col + cap[1];
            if (!board.inBounds(r, c)) {
                continue;
            }
            ChessPosition targetPos = new ChessPosition(r, c);
            ChessPiece target = board.getPiece(targetPos);
            if (target != null && target.getTeamColor() != pieceColor) {
                if (isPromotionRank(r)) {
                    addPromotions(moves, pos, targetPos);
                } else {
                    moves.add(new ChessMove(pos, targetPos, null));
                }
            }
        }
        return moves;
    }

    private boolean isPromotionRank(int row) {
        return (pieceColor == ChessGame.TeamColor.WHITE && row == 8)
                || (pieceColor == ChessGame.TeamColor.BLACK && row == 1);
    }

    private void addPromotions(List<ChessMove> moves, ChessPosition from, ChessPosition to) {
        moves.add(new ChessMove(from, to, PieceType.QUEEN));
        moves.add(new ChessMove(from, to, PieceType.ROOK));
        moves.add(new ChessMove(from, to, PieceType.BISHOP));
        moves.add(new ChessMove(from, to, PieceType.KNIGHT));
    }
}

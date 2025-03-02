package chess;

import java.util.Collection;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessPiece that)) {
            return false;
        }
        return color == that.color && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type);
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        return switch (getPieceType()) {
            case KING -> new KingMoves().pieceMoves(board, myPosition);
            case QUEEN -> new QueenMoves().pieceMoves(board, myPosition);
            case BISHOP -> new BishopMoves().pieceMoves(board, myPosition);
            case KNIGHT -> new KnightMoves().pieceMoves(board, myPosition);
            case ROOK -> new RookMoves().pieceMoves(board, myPosition);
            case PAWN -> new PawnMoves().pieceMoves(board, myPosition);
        };
    }

    //Consolidate piece classes here

/*    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {

        ChessPiece piece = board.getPiece(myPosition);
        int row = myPosition.getRow();
        int col = myPosition.getColumn();
        int[] rowMove = null;
        int [] colMove = null;

        switch (getPieceType()) {
            case KING:
                rowMove = new int[]{-1, -1, -1, 0, 1, 1, 1, 0};
                colMove = new int[]{-1, 0, 1, 1, 1, 0, -1, -1};
                break;
            case QUEEN:
                rowMove = new int[]{-1, -1, -1, 0, 1, 1, 1, 0}; // Rook + Bishop directions
                colMove = new int[]{0, 1, -1, -1, 1, 0, -1, 1};
                break;
            case ROOK:
                rowMove = new int[]{-1, 0, 1, 0};
                colMove = new int[]{0, 1, 0, -1};
                break;
            case BISHOP:
                rowMove = new int[]{-1, -1, 1, 1};
                colMove = new int[]{-1, 1, -1, 1};
                break;
            case KNIGHT:
                rowMove = new int[]{-2, -1, 1, 2, 2, 1, -1, -2};
                colMove = new int[]{1, 2, 2, 1, -1, -2, -2, -1};
                break;
            case PAWN:
                rowMove = new int[]{1}; // Pawns can only move one square forward
                colMove = new int[]{0}; // Pawns move straight
                break;
        }
        return null;
    }*/
}

package chess;

import java.util.Collection;
import java.util.Objects;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private TeamColor team;
    private ChessBoard board;
    private int turn = 1;


    public ChessGame() {
        this.team = TeamColor.WHITE;
        this.board = new ChessBoard();
        this.board.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return team;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.team = team;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ChessGame chessGame)) return false;
        return team == chessGame.team && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(team, getBoard());
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "team=" + team +
                ", board=" + board +
                '}';
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        if (piece == null) {
            return null;
        }

        Collection<ChessMove> validMoves = new HashSet<>();
        Collection<ChessMove> potentialMoves = piece.pieceMoves(board, startPosition);

        for (ChessMove move : potentialMoves) {
            ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
            testMove(move, piece);

            boolean kingInCheck = isInCheck(piece.getTeamColor());

            undoMove(move, piece, capturedPiece);

            if (!kingInCheck) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPiece piece = board.getPiece(start);
        if (piece == null) {
            throw new InvalidMoveException("start position null");
        }
        if (piece.getTeamColor() != team) {
            throw new InvalidMoveException("wrong team");
        }

        Collection<ChessMove> validMoves = validMoves(start);
        boolean isValid = validMoves.contains(move);

        if (isValid) {
            testMove(move, piece);
        }
        else {
            throw new InvalidMoveException("Invalid move");
        }

        if (turn % 2 == 0) {
            team = TeamColor.WHITE;
        }
        else {
            team = TeamColor.BLACK;
        }
        turn++;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKing(teamColor);

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPiece piece = board.getPiece(new ChessPosition(i, j));
                if (piece != null && piece.getTeamColor() != teamColor) {
                    Collection<ChessMove> potentialMoves = piece.pieceMoves(board, new ChessPosition(i, j));
                    for (ChessMove moves : potentialMoves) {
                        if (moves.getEndPosition().equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition findKing (TeamColor teamColor) {
        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getPieceType() == ChessPiece.PieceType.KING && piece.getTeamColor() == teamColor) {
                    return position;
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);

                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> potentialMoves = piece.pieceMoves(board, position);
                    for (ChessMove move : potentialMoves) {
                        ChessPiece capturedPiece = board.getPiece(move.getEndPosition());
                        testMove(move, piece);

                        boolean stillInCheck = isInCheck(teamColor);

                        undoMove(move, piece, capturedPiece);

                        if (!stillInCheck) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    public void testMove(ChessMove move, ChessPiece piece) {
        ChessPosition end = move.getEndPosition();
        ChessPosition start = move.getStartPosition();
        ChessPiece.PieceType type = move.getPromotionPiece();
        ChessPiece promoted = new ChessPiece(team, type);
        if (type != null) {
            board.addPiece(end, promoted);
            board.removePiece(start);
        } else {
            board.addPiece(end, piece);
            board.removePiece(start);
        }
    }

    public void undoMove(ChessMove move, ChessPiece piece, ChessPiece capturedPiece) {
        ChessPosition end = move.getEndPosition();
        ChessPosition start = move.getStartPosition();
        board.removePiece(end);
        board.addPiece(start, piece);
        if (capturedPiece != null) {
            board.addPiece(end, capturedPiece);
        }
    }


    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (int i = 1; i <= 8; i++) {
            for (int j = 1; j <= 8; j++) {
                ChessPosition position = new ChessPosition(i, j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    Collection<ChessMove> validMoves = validMoves(position);
                    if (!validMoves.isEmpty()) {
                        return false;
                    }
                }
            }
        }
        return true;
    }
}

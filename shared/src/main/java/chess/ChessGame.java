package chess;

import java.util.*;

/**
 * For a class that can manage a chess game, making moves on a board
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

    public TeamColor getTeamTurn() { return team; }
    public void setTeamTurn(TeamColor team) { this.team = team; }
    public void setBoard(ChessBoard board) { this.board = board; }
    public ChessBoard getBoard() { return board; }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ChessGame other)) {
            return false;
        }
        return team == other.team && Objects.equals(board, other.board);
    }

    @Override
    public int hashCode() { return Objects.hash(team, board); }

    @Override
    public String toString() {
        return "ChessGame{team=" + team + ", board=" + board + '}';
    }

    public enum TeamColor { WHITE, BLACK }

    // Move Validation
    public Collection<ChessMove> validMoves(ChessPosition start) {
        ChessPiece piece = board.getPiece(start);
        if (piece == null) {
            return null;
        }

        Set<ChessMove> validMoves = new HashSet<>();
        for (ChessMove move : piece.pieceMoves(board, start)) {
            ChessPiece captured = board.getPiece(move.getEndPosition());
            testMove(move, piece);
            boolean kingInCheck = isInCheck(piece.getTeamColor());
            undoMove(move, piece, captured);
            if (!kingInCheck) {
                validMoves.add(move);
            }
        }
        return validMoves;
    }

    // Make Move
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece == null) {
            throw new InvalidMoveException("start position null");
        }
        if (piece.getTeamColor() != team) {
            throw new InvalidMoveException("wrong team");
        }

        if (!validMoves(move.getStartPosition()).contains(move)) {
            throw new InvalidMoveException("Invalid move");
        }

        testMove(move, piece);

        team = (turn++ % 2 == 0) ? TeamColor.WHITE : TeamColor.BLACK;
    }

    // Check / Checkmate / Stalemate
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPos = findKing(teamColor);
        for (ChessPosition pos : getAllPiecePositions()) {
            ChessPiece piece = board.getPiece(pos);
            if (piece != null && piece.getTeamColor() != teamColor) {
                for (ChessMove move : piece.pieceMoves(board, pos)) {
                    if (move.getEndPosition().equals(kingPos)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public boolean isInCheckmate(TeamColor teamColor) {
        if (!isInCheck(teamColor)) {
            return false;
        }

        for (ChessPosition pos : getTeamPiecePositions(teamColor)) {
            ChessPiece piece = board.getPiece(pos);
            if (piece == null) {
                continue;
            }

            for (ChessMove move : piece.pieceMoves(board, pos)) {
                if (moveRemovesCheck(move, piece, teamColor)) {
                    return false;
                }
            }
        }
        return true;
    }

    public boolean isInStalemate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            return false;
        }

        for (ChessPosition pos : getTeamPiecePositions(teamColor)) {
            Collection<ChessMove> moves = validMoves(pos);
            if (moves != null && !moves.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // Helper Methods
    private boolean moveRemovesCheck(ChessMove move, ChessPiece piece, TeamColor teamColor) {
        ChessPiece captured = board.getPiece(move.getEndPosition());
        testMove(move, piece);
        boolean stillInCheck = isInCheck(teamColor);
        undoMove(move, piece, captured);
        return !stillInCheck;
    }

    private List<ChessPosition> getTeamPiecePositions(TeamColor teamColor) {
        List<ChessPosition> positions = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition pos = new ChessPosition(row, col);
                ChessPiece piece = board.getPiece(pos);
                if (piece != null && piece.getTeamColor() == teamColor) {
                    positions.add(pos);
                }
            }
        }
        return positions;
    }

    private List<ChessPosition> getAllPiecePositions() {
        List<ChessPosition> positions = new ArrayList<>();
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                positions.add(new ChessPosition(row, col));
            }
        }
        return positions;
    }

    public ChessPosition findKing(TeamColor teamColor) {
        for (ChessPosition pos : getAllPiecePositions()) {
            ChessPiece piece = board.getPiece(pos);
            if (piece != null &&
                    piece.getPieceType() == ChessPiece.PieceType.KING &&
                    piece.getTeamColor() == teamColor) {
                return pos;
            }
        }
        return null;
    }

    // Board Manipulation
    public void testMove(ChessMove move, ChessPiece piece) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece.PieceType promotion = move.getPromotionPiece();

        if (promotion != null) {
            board.addPiece(end, new ChessPiece(piece.getTeamColor(), promotion));
        } else {
            board.addPiece(end, piece);
        }
        board.removePiece(start);
    }

    public void undoMove(ChessMove move, ChessPiece piece, ChessPiece captured) {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();

        board.removePiece(end);
        board.addPiece(start, piece);
        if (captured != null) {
            board.addPiece(end, captured);
        }
    }
}

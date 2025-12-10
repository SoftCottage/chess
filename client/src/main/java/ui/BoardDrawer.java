package ui;

import chess.ChessGame;

public class BoardDrawer {

    private static final String LIGHT_BG = "\u001B[47m";  // white background
    private static final String DARK_BG  = "\u001B[43m";  // yellow/brown background
    private static final String RESET    = "\u001B[0m";

    private static final String WHITE_FG = "\u001B[97m";  // bright white
    private static final String BLACK_FG = "\u001B[30m";  // bright white

    private static final String[][] START = {
            {"♜","♞","♝","♛","♚","♝","♞","♜"},
            {"♟","♟","♟","♟","♟","♟","♟","♟"},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {null,null,null,null,null,null,null,null},
            {"♙","♙","♙","♙","♙","♙","♙","♙"},
            {"♖","♘","♗","♕","♔","♗","♘","♖"}
    };

    public static void draw(ChessGame.TeamColor perspective) {
        if (perspective == ChessGame.TeamColor.BLACK) {
            drawBlackPerspective();
        } else {
            drawWhitePerspective();
        }
    }

    private static void drawWhitePerspective() {
        System.out.println("\n   a  b  c  d  e  f  g  h");

        for (int row = 7; row >= 0; row--) {
            System.out.printf("%d ", row + 1);

            for (int col = 0; col < 8; col++) {
                drawSquare(row, col);
            }

            System.out.printf(" %d%n", row + 1);
        }

        System.out.println("   a  b  c  d  e  f  g  h");
        System.out.println();
    }

    private static void drawBlackPerspective() {
        System.out.println("\n    h   g   f   e   d   c   b   a");

        for (int row = 0; row < 8; row++) {
            System.out.printf("%d ", row + 1);

            for (int col = 7; col >= 0; col--) {
                drawSquare(row, col);
            }

            System.out.printf(" %d%n", row + 1);
        }

        System.out.println("    h   g   f   e   d   c   b   a");
        System.out.println();
    }

    private static void drawSquare(int row, int col) {
        boolean lightSquare = (row + col) % 2 == 0;

        String bg = lightSquare ? LIGHT_BG : DARK_BG;

        String piece = START[row][col];

        if (piece == null) {
            System.out.print(bg + "   " + RESET);
            return;
        }

        boolean isWhitePiece = Character.isUpperCase(piece.charAt(0)) ||
                piece.equals("♙") || piece.equals("♖") ||
                piece.equals("♘") || piece.equals("♗") ||
                piece.equals("♕") || piece.equals("♔");

        String fg = isWhitePiece ? WHITE_FG : BLACK_FG;

        System.out.print(bg + fg + " " + piece + " " + RESET);
    }
}

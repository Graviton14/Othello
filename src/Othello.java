/**
 * Henry Fricke
 * Lab 2: Othello
 * 9/13/2023
 * CS251
 */

import cs251.lab2.OthelloGUI;
import cs251.lab2.OthelloInterface;
import javax.swing.*;
import java.util.Random;

/**
 * The Othello class implements the Othello game logic and interface.
 */
public class Othello implements OthelloInterface {
    private static boolean computerState = false; // Flag to indicate if the computer is enabled
    private StringBuilder boardString; // String representation of the game board
    private Piece currentPlayer; // The current player (DARK or LIGHT)
    private Piece[][] boardState; // 2D array to represent the game board
    private Timer timer; // Timer for delaying the computer's move

    /**
     * Initializes a new Othello game instance.
     */
    public Othello() {
        boardState = new Piece[DEFAULT_SIZE][DEFAULT_SIZE];
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                boardState[i][j] = Piece.EMPTY;
            }
        }

        timer = new Timer(150, e -> {
            // Code to execute after the delay (1 second)
            makeComputerMove();
            timer.stop(); // Stop the timer after the action is performed
        });
        timer.setRepeats(false); // Set to non-repeating, so it triggers only once
    }

    /**
     * The main entry point of the Othello application.
     *
     * @param args Command-line arguments (optional).
     */
    public static void main(String[] args) {
        Othello game = new Othello();
        if (args.length > 0) {
            game.initComputerPlayer(args[0]);
        }
        OthelloGUI.showGUI(game);
    }

    @Override
    public int getSize() {
        return DEFAULT_SIZE;
    }

    @Override
    public void initGame() {
        boardString = new StringBuilder();
        boardState = new Piece[DEFAULT_SIZE][DEFAULT_SIZE];
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                if ((i == DEFAULT_SIZE / 2 - 1 && j == DEFAULT_SIZE / 2 - 1) ||
                        (i == DEFAULT_SIZE / 2 && j == DEFAULT_SIZE / 2)) {
                    boardState[i][j] = Piece.LIGHT;
                    boardString.append(Piece.LIGHT.toChar());
                } else if ((i == DEFAULT_SIZE / 2 - 1 && j == DEFAULT_SIZE / 2) ||
                        (i == DEFAULT_SIZE / 2 && j == DEFAULT_SIZE / 2 - 1)) {
                    boardState[i][j] = Piece.DARK;
                    boardString.append(Piece.DARK.toChar());
                } else {
                    boardState[i][j] = Piece.EMPTY;
                    boardString.append(Piece.EMPTY.toChar());
                }
            }
        }
        currentPlayer = Piece.DARK;
    }

    @Override
    public String getBoardString() {
        StringBuilder formattedBoard = new StringBuilder();
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                formattedBoard.append(boardState[i][j].toChar());
            }
            formattedBoard.append("\n");
        }
        return formattedBoard.toString();
    }

    @Override
    public Piece getCurrentTurn() {
        return currentPlayer;
    }

    @Override
    public Result handleClickAt(int x, int y) {
        if (currentPlayer == Piece.DARK || currentPlayer == Piece.LIGHT) {
            if (checkGameOver() == Result.DARK_WINS || checkGameOver() == Result.LIGHT_WINS) {
                return checkGameOver();
            }
            if (isLegal(x, y)) {
                makeMove(x, y);

                // Print a message indicating the player's move
                System.out.println("Player flipped (" + x + ", " + y + ").");

                currentPlayer = (currentPlayer == Piece.DARK) ? Piece.LIGHT : Piece.DARK;

                // Start the timer to delay the computer's move
                timer.start();

                return checkGameOver();
            }
            return checkGameOver();
        }
        return checkGameOver();
    }

    /**
     * Checks the game over conditions and returns the game result.
     *
     * @return The game result (DRAW, DARK_WINS, LIGHT_WINS, or GAME_NOT_OVER).
     */
    private Result checkGameOver() {
        int legalMoves = 0;
        int darkPieces = 0;
        int lightPieces = 0;

        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                if (isLegal(i, j)) {
                    legalMoves++;
                }
                if (boardState[i][j] == Piece.DARK) {
                    darkPieces++;
                } else if (boardState[i][j] == Piece.LIGHT) {
                    lightPieces++;
                }
            }
        }
        if (legalMoves > 0) {
            return Result.GAME_NOT_OVER;
        } else {
            if (darkPieces > lightPieces) {
                return Result.DARK_WINS;
            } else if (lightPieces > darkPieces) {
                return Result.LIGHT_WINS;
            }
        }
        return Result.GAME_NOT_OVER;
    }

    /**
     * Makes a move on the board and flips pieces as necessary.
     *
     * @param x The X coordinate of the move.
     * @param y The Y coordinate of the move.
     */
    private void makeMove(int x, int y) {
        if (isLegal(x, y)) {
            int[][] directions = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1}, {0, 1},
                    {1, -1}, {1, 0}, {1, 1}
            };

            for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];
                int posX = x + dx;
                int posY = y + dy;

                boolean flipped = false;

                while (posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE &&
                        boardState[posX][posY] == (currentPlayer == Piece.DARK ? Piece.LIGHT : Piece.DARK)) {
                    posX += dx;
                    posY += dy;
                    flipped = true;
                }

                if (flipped && posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE &&
                        boardState[posX][posY] == currentPlayer) {
                    int flipX = x + dx;
                    int flipY = y + dy;
                    while (flipX != posX || flipY != posY) {
                        boardState[flipX][flipY] = currentPlayer;
                        boardString.setCharAt(flipX * DEFAULT_SIZE + flipY, currentPlayer.toChar());
                        flipX += dx;
                        flipY += dy;
                    }
                }
            }

            boardState[x][y] = currentPlayer;
            boardString.setCharAt(x * DEFAULT_SIZE + y, currentPlayer.toChar());
        }
    }

    /**
     * Handles the computer's move, including flipping pieces.
     */
    private void makeComputerMove() {
        int[] legalMoves = new int[DEFAULT_SIZE * DEFAULT_SIZE];
        int numLegalMoves = 0;
        if (computerState) {
            for (int i = 0; i < DEFAULT_SIZE; i++) {
                for (int j = 0; j < DEFAULT_SIZE; j++) {
                    if (isLegal(i, j)) {
                        legalMoves[numLegalMoves] = i * DEFAULT_SIZE + j;
                        numLegalMoves++;
                    }
                }
            }
        }

        if (numLegalMoves > 0) {
            int randomIndex = new Random().nextInt(numLegalMoves);
            int move = legalMoves[randomIndex];
            int x = move / DEFAULT_SIZE;
            int y = move % DEFAULT_SIZE;

            // Flipping pieces for the computer move directly here
            int[][] directions = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1}, {0, 1},
                    {1, -1}, {1, 0}, {1, 1}
            };

            for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];
                int posX = x + dx;
                int posY = y + dy;

                boolean flipped = false;

                while (posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE &&
                        boardState[posX][posY] == (currentPlayer == Piece.DARK ? Piece.LIGHT : Piece.DARK)) {
                    posX += dx;
                    posY += dy;
                    flipped = true;
                }

                if (flipped && posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE &&
                        boardState[posX][posY] == currentPlayer) {
                    int flipX = x + dx;
                    int flipY = y + dy;
                    while (flipX != posX || flipY != posY) {
                        boardState[flipX][flipY] = currentPlayer;
                        boardString.setCharAt(flipX * DEFAULT_SIZE + flipY, currentPlayer.toChar());
                        flipX += dx;
                        flipY += dy;
                    }

                    // Flip the new piece itself
                    boardState[x][y] = currentPlayer;
                    boardString.setCharAt(x * DEFAULT_SIZE + y, currentPlayer.toChar());
                }
            }
            System.out.println("AI flipped (" + x + ", " + y + ").");
            currentPlayer = Piece.DARK;

            // Check if the game is over after the computer move
            Result result = checkGameOver();
            if (result != Result.GAME_NOT_OVER) {
                // The game is over, handle the result here
                System.out.println("Game Over! Result: " + result);
            }
        }
    }

    @Override
    public boolean isLegal(int x, int y) {
        if (x < 0 || x >= DEFAULT_SIZE || y < 0 || y >= DEFAULT_SIZE || boardState[x][y] != Piece.EMPTY) {
            return false;
        }

        int[][] directions = {
                {-1, -1}, {-1, 0}, {-1, 1},
                {0, -1}, {0, 1},
                {1, -1}, {1, 0}, {1, 1}
        };

        for (int[] dir : directions) {
            int dx = dir[0];
            int dy = dir[1];
            int posX = x + dx;
            int posY = y + dy;

            boolean foundOpponentPiece = false;

            while (posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE) {
                if (boardState[posX][posY] == Piece.EMPTY) {
                    break;
                }

                if (boardState[posX][posY] == currentPlayer) {
                    if (foundOpponentPiece) {
                        return true;
                    } else {
                        break;
                    }
                } else {
                    foundOpponentPiece = true;
                }

                posX += dx;
                posY += dy;
            }
        }

        return false;
    }

    /**
     * Initializes the computer player based on the specified settings.
     *
     * @param settings The settings for the computer player ("COMPUTER" or other).
     */
    public void initComputerPlayer(String settings) {
        computerState = "COMPUTER".equalsIgnoreCase(settings);
    }
}

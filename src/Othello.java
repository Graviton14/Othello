import cs251.lab2.*;

public class Othello implements OthelloInterface {
    public static void main(String[] args) {
        Othello game = new Othello();
        if (args.length>0) {
            game.initComputerPlayer(args[0]);
        }
        OthelloGUI.showGUI(game);
    }

    private StringBuilder boardString;
    private Piece currentPlayer;
    private Piece[][] boardState;

    public Othello() {
        // Initialize the boardState array with the default size.
        boardState = new Piece[DEFAULT_SIZE][DEFAULT_SIZE];

        // Initialize all elements to Piece.EMPTY
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                boardState[i][j] = Piece.EMPTY;
            }
        }
    }

    /**
     * @return
     */
    @Override
    public int getSize() {
        return DEFAULT_SIZE;
    }

    /**
     * Initialize the game board, starting pieces, and the initial player's turn.
     */
    @Override
    public void initGame() {
        System.out.println("initGame method called");
        boardString = new StringBuilder();
        currentPlayer = Piece.DARK; // Set the initial player's turn to black.

        // Initialize the boardState array.
        boardState = new Piece[DEFAULT_SIZE][DEFAULT_SIZE];

        // Initialize the boardState array and boardString.
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                if ((i == DEFAULT_SIZE / 2 - 1 && j == DEFAULT_SIZE / 2 - 1) ||
                        (i == DEFAULT_SIZE / 2 && j == DEFAULT_SIZE / 2)) {
                    // Place initial white pieces.
                    boardState[i][j] = Piece.LIGHT;
                    boardString.append(Piece.LIGHT.toChar());
                    System.out.println("Placed LIGHT piece at (" + i + ", " + j + ")");
                } else if ((i == DEFAULT_SIZE / 2 - 1 && j == DEFAULT_SIZE / 2) ||
                        (i == DEFAULT_SIZE / 2 && j == DEFAULT_SIZE / 2 - 1)) {
                    // Place initial black pieces.
                    boardState[i][j] = Piece.DARK;
                    boardString.append(Piece.DARK.toChar());
                    System.out.println("Placed DARK piece at (" + i + ", " + j + ")");
                } else {
                    // Place empty cells.
                    boardState[i][j] = Piece.EMPTY;
                    boardString.append(Piece.EMPTY.toChar()); // Append '.' for an empty cell.
                    System.out.println("Placed EMPTY cell at (" + i + ", " + j + ")");
                }

                if (j < DEFAULT_SIZE - 1) {
                    boardString.append(" ");
                }

                if (j == DEFAULT_SIZE - 1) {
                    boardString.append("\n"); // Append newline at the end of each row.
                }
            }
        }
        System.out.println("Initialized boardString:");
        System.out.println(boardString.toString());
        System.out.println("Initialized boardState:");
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                System.out.print(boardState[i][j].toChar() + " ");
            }
            System.out.println();
        }
    }

    /**
     * @return
     */
    @Override
    public String getBoardString() {
        return boardString.toString();
    }

    /**
     * @return
     */
    @Override
    public Piece getCurrentTurn() {
        return currentPlayer;
    }

    /**
     * Handle a player's click at the specified coordinates.
     * @param x The x-coordinate of the clicked cell.
     * @param y The y-coordinate of the clicked cell.
     * @return The game result after the move.
     */
    @Override
    public Result handleClickAt(int x, int y) {
        System.out.println("handleClickAt method called");
        int cellSize = getSize() * 75 / DEFAULT_SIZE; // Calculate cell size dynamically.

        // Calculate clickedCellX and clickedCellY based on cellSize.
        int clickedCellX = x / cellSize;
        int clickedCellY = y / cellSize;

        System.out.println("Clicked at (" + x + ", " + y + ")");
        System.out.println("Clicked cell: (" + clickedCellX + ", " + clickedCellY + ")");
        System.out.println("Current player: " + getCurrentTurn());

        // Rest of your handleClickAt method...

        if (isLegal(clickedCellX, clickedCellY)) {
            // Update the board state with the current player's piece.
            boardState[clickedCellX][clickedCellY] = getCurrentTurn();

            // Update the corresponding character in boardString.
            boardString.setCharAt(clickedCellX * (DEFAULT_SIZE + 1) + clickedCellY, getCurrentTurn().toChar());

            // Rest of your handleClickAt method...

            System.out.println("Updated boardState:");
            for (int i = 0; i < DEFAULT_SIZE; i++) {
                for (int j = 0; j < DEFAULT_SIZE; j++) {
                    System.out.print(boardState[i][j].toChar() + " ");
                }
                System.out.println();
            }

            System.out.println("Updated boardString:");
            System.out.println(boardString.toString());

            return checkGameOver();
        } else {
            // Handle an illegal move (e.g., show a message to the player).
            System.out.println("Illegal move!");
            return Result.GAME_NOT_OVER; // Modify this according to your game logic.
        }
    }


    /**
     * Helper method to check if the game is over.
     * @return The game result (WHITE_WINS, BLACK_WINS, DRAW, or GAME_NOT_OVER).
     */
    private Result checkGameOver() {
        int darkCount = 0;
        int lightCount = 0;
        int emptyCount = 0;

        // Count the number of dark, light, and empty cells on the board.
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                if (boardState[i][j] == Piece.DARK) {
                    darkCount++;
                } else if (boardState[i][j] == Piece.LIGHT) {
                    lightCount++;
                } else if (boardState[i][j] == Piece.EMPTY) {
                    emptyCount++;
                }
            }
        }

        if (darkCount + lightCount == DEFAULT_SIZE * DEFAULT_SIZE || emptyCount == 0) {
            // The game is over if the board is full or there are no empty cells.
            if (darkCount > lightCount) {
                return Result.DARK_WINS;
            } else if (lightCount > darkCount) {
                return Result.LIGHT_WINS;
            } else {
                return Result.DRAW;
            }
        }

        return Result.GAME_NOT_OVER;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    @Override
    public boolean isLegal(int x, int y) {
        return false;
    }

    /**
     * @param s
     */
    @Override
    public void initComputerPlayer(String s) {

    }
}

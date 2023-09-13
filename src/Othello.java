import cs251.lab2.OthelloGUI;
import cs251.lab2.OthelloInterface;
import java.util.Random;

public class Othello implements OthelloInterface {
    private StringBuilder boardString;
    private Piece currentPlayer;
    private Piece[][] boardState;
    private boolean playerTurn = true;

    public Othello() {
        boardState = new Piece[DEFAULT_SIZE][DEFAULT_SIZE];
        for (int i = 0; i < DEFAULT_SIZE; i++) {
            for (int j = 0; j < DEFAULT_SIZE; j++) {
                boardState[i][j] = Piece.EMPTY;
            }
        }
    }

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
        currentPlayer = Piece.DARK;
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
        if (isLegal(x, y)) {
            int[][] directions = {
                    {-1, -1}, {-1, 0}, {-1, 1},
                    {0, -1}, /* Center */ {0, 1},
                    {1, -1}, {1, 0}, {1, 1}
            };

            for (int[] dir : directions) {
                int dx = dir[0];
                int dy = dir[1];
                int posX = x + dx;
                int posY = y + dy;

                if (posX < 0 || posX >= DEFAULT_SIZE || posY < 0 || posY >= DEFAULT_SIZE || boardState[posX][posY] != (currentPlayer == Piece.DARK ? Piece.LIGHT : Piece.DARK)) {
                    continue;
                }

                while (posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE && boardState[posX][posY] == (currentPlayer == Piece.DARK ? Piece.LIGHT : Piece.DARK)) {
                    posX += dx;
                    posY += dy;
                }

                if (posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE && boardState[posX][posY] == currentPlayer) {
                    int flipX = x;
                    int flipY = y;
                    while (flipX != posX || flipY != posY) {
                        boardState[flipX][flipY] = currentPlayer;
                        boardString.setCharAt(flipX * DEFAULT_SIZE + flipY, currentPlayer.toChar());
                        flipX += dx;
                        flipY += dy;
                    }
                }
            }

            boardState[x][y] = getCurrentTurn();
            boardString.setCharAt(x * DEFAULT_SIZE + y, getCurrentTurn().toChar());
            currentPlayer = (currentPlayer == Piece.DARK) ? Piece.LIGHT : Piece.DARK;
            playerTurn = false;
            return checkGameOver();
        } else {
            return Result.GAME_NOT_OVER;
        }
    }

    private Result checkGameOver() {
        int darkCount = 0;
        int lightCount = 0;
        int emptyCount = 0;

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

            if (posX < 0 || posX >= DEFAULT_SIZE || posY < 0 || posY >= DEFAULT_SIZE || boardState[posX][posY] != (currentPlayer == Piece.DARK ? Piece.LIGHT : Piece.DARK)) {
                continue;
            }

            while (posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE && boardState[posX][posY] == (currentPlayer == Piece.DARK ? Piece.LIGHT : Piece.DARK)) {
                posX += dx;
                posY += dy;
            }

            if (posX >= 0 && posX < DEFAULT_SIZE && posY >= 0 && posY < DEFAULT_SIZE && boardState[posX][posY] == currentPlayer) {
                return true;
            }
        }

        return false;
    }

    public void initComputerPlayer(String settings) {
        // Implementation for initializing the computer player goes here.
    }
}

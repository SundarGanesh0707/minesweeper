package minesweeper;
import java.util.Scanner;
import java.util.Random;

public class Minesweeper {
    private static final int BOARD_SIZE = 10;
    private static final int BOMB_COUNT = 10;
    private static final char HIDDEN_CELL = '-';
    private static final char BOMB_CELL = '*';
    private static final char FLAGGED_CELL = 'F';

    private char[][] board;
    private boolean[][] bombLocations;
    private boolean[][] visited;

    private Scanner scanner;
    private int remainingCells;
    private boolean gameOver;

    public Minesweeper() {
        board = new char[BOARD_SIZE][BOARD_SIZE];
        bombLocations = new boolean[BOARD_SIZE][BOARD_SIZE];
        visited = new boolean[BOARD_SIZE][BOARD_SIZE];
        scanner = new Scanner(System.in);
        remainingCells = BOARD_SIZE * BOARD_SIZE - BOMB_COUNT;
        gameOver = false;
        initializeBoard();
    }

    public void play() {
        while (!gameOver) {
            printBoard();
            System.out.println("Enter row and column to uncover or flag (e.g. 3 4 F):");
            int row = scanner.nextInt() - 1;
            int col = scanner.nextInt() - 1;
            String action = scanner.next();

            if (action.equals("F")) {
                flagCell(row, col);
            } else {
                uncoverCell(row, col);
            }

            checkGameOver();
        }
        printBoard();
    }

    private void initializeBoard() {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                board[i][j] = HIDDEN_CELL;
                bombLocations[i][j] = false;
                visited[i][j] = false;
            }
        }

        int bombsPlaced = 0;
        Random random = new Random();
        while (bombsPlaced < BOMB_COUNT) {
            int row = random.nextInt(BOARD_SIZE);
            int col = random.nextInt(BOARD_SIZE);
            if (!bombLocations[row][col]) {
                bombLocations[row][col] = true;
                bombsPlaced++;
            }
        }
    }

    private void printBoard() {
        System.out.print("  ");
        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " ");
        }
        System.out.println();

        for (int i = 0; i < BOARD_SIZE; i++) {
            System.out.print((i + 1) + " ");
            for (int j = 0; j < BOARD_SIZE; j++) {
                System.out.print(board[i][j] + " ");
            }
            System.out.println();
        }
    }

    private void flagCell(int row, int col) {
        if (board[row][col] == HIDDEN_CELL) {
            board[row][col] = FLAGGED_CELL;
        } else if (board[row][col] == FLAGGED_CELL) {
            board[row][col] = HIDDEN_CELL;
        }
    }

    private void uncoverCell(int row, int col) {
        if (visited[row][col]) {
            return;
        }

        visited[row][col] = true;

        if (bombLocations[row][col]) {
            board[row][col] = BOMB_CELL;
            gameOver = true;
            System.out.println("Better luck Play next Time!");
            return;
        }

        int adjacentBombCount = 0;
        for (int i = row - 1; i <= row + 1; i++) {
            for (int j = col - 1; j<= col + 1; j++) {
                if (i < 0 || i >= BOARD_SIZE || j < 0 || j >= BOARD_SIZE || visited[i][j]) {
                    continue;
                }
                if (bombLocations[i][j]) {
                    adjacentBombCount++;
                }
            }
        }

        board[row][col] = (char) ('0' + adjacentBombCount);

        if (adjacentBombCount == 0) {
            for (int i = row - 1; i <= row + 1; i++) {
                for (int j = col - 1; j <= col + 1; j++) {
                    if (i < 0 || i >= BOARD_SIZE || j < 0 || j >= BOARD_SIZE || visited[i][j]) {
                        continue;
                    }
                    uncoverCell(i, j);
                }
            }
        }

        remainingCells--;
    }

    private void checkGameOver() {
        if (remainingCells == 0) {
            gameOver = true;
            System.out.println("Congratulations, you won!");
        }
    }

    public static void main(String[] args) {
        Minesweeper game = new Minesweeper();
        game.play();
    }
}    
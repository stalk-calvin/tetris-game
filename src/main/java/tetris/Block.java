package tetris;

import java.awt.Color;

class Block {

    static final int BLOCK_LENGTH = 16;

    private static final int[][] tetrisBlockI = {{1}, {1}, {1}, {1}};
    private static final int[][] tetrisBlockJ = {{2, 0, 0}, {2, 2, 2}};
    private static final int[][] tetrisBlockL = {{0, 0, 3}, {3, 3, 3}};
    private static final int[][] tetrisBlockO = {{4, 4}, {4, 4}};
    private static final int[][] tetrisBlockS = {{0, 5, 5}, {5, 5, 0}};
    private static final int[][] tetrisBlockT = {{0, 6, 0}, {6, 6, 6}};
    private static final int[][] tetrisBlockZ = {{7, 7, 0}, {0, 7, 7}};

    private int[][] currentBlockState;

    //starting piece positions
    private int blockColLocation = 7;
    private int blockRowLocation = 0;

    Block() {
    }

    Block(int[][] currentBlock) {
        currentBlockState = currentBlock;
    }

    //getter
    static Color getColour(int color) {
        switch (color) {
            case 1:
                return Color.DARK_GRAY;
            case 2:
                return Color.BLUE;
            case 3:
                return Color.RED;
            case 4:
                return Color.GREEN;
            case 5:
                return Color.BLACK;
            case 6:
                return Color.MAGENTA;
            case 7:
                return Color.GRAY;
        }
        return null;
    }

    int[][] getRandom() {
        switch ((int) (0 + Math.random() * 7)) {
            case 0:
                currentBlockState = tetrisBlockI;
                return tetrisBlockI;
            case 1:
                currentBlockState = tetrisBlockJ;
                return tetrisBlockJ;
            case 2:
                currentBlockState = tetrisBlockL;
                return tetrisBlockL;
            case 3:
                currentBlockState = tetrisBlockO;
                return tetrisBlockO;
            case 4:
                currentBlockState = tetrisBlockS;
                return tetrisBlockS;
            case 5:
                currentBlockState = tetrisBlockT;
                return tetrisBlockT;
            case 6:
                currentBlockState = tetrisBlockZ;
                return tetrisBlockZ;
            default:
                return null;
        }
    }

    int[][] getCurrentState() {
        return currentBlockState;
    }

    int getColNum() {
        return blockColLocation;
    }

    int getRowNum() {
        return blockRowLocation;
    }

    //rotator
    int[][] rotateRight(boolean shouldRotate) {
        int newColLength = currentBlockState.length;
        int newRowLength = currentBlockState[0].length;

        int[][] rotate90Array = new int[newRowLength][newColLength];
        for (int r = 0; r < newRowLength; r++) {
            for (int c = 0; c < newColLength; c++) {
                rotate90Array[r][c] = currentBlockState[newColLength - c - 1][r];
            }
        }

        // when the peice went overboard
        if (blockColLocation + newColLength > Board.boardWidth) {
            blockColLocation = Board.boardWidth - newColLength;
        }

        if (shouldRotate) {
            currentBlockState = rotate90Array;
        }

        return rotate90Array;
    }

    int[][] rotateLeft(boolean shouldRotate) {
        int newColLength = currentBlockState.length;
        int newRowLength = currentBlockState[0].length;

        int[][] rotate270Array = new int[newRowLength][newColLength];
        for (int r = 0; r < newRowLength; r++) {
            for (int c = 0; c < newColLength; c++) {
                rotate270Array[r][c] = currentBlockState[c][newRowLength - r - 1];
            }
        }

        if (blockColLocation + newColLength > Board.boardWidth) {
            blockColLocation = Board.boardWidth - newColLength;
        }
        if (shouldRotate) {
            currentBlockState = rotate270Array;
        }

        return rotate270Array;
    }

    //mover
    void moveLeft() {
        if (blockColLocation > 0) {
            blockColLocation--;
        }
    }

    void moveRight() {
        if (blockColLocation + currentBlockState[0].length < Board.boardWidth) {
            blockColLocation++;
        }
    }

    boolean moveDown() {
        if (currentBlockState.length + blockRowLocation < Board.boardHeight) {
            blockRowLocation++;
            return false;
        }
        return true;
    }
}

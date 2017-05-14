package tetris;

import java.awt.Color;

class Block {

    static final int BLOCK_LENGTH = 16;

    private int[][] tetrisBlockI = {{1}, {1}, {1}, {1}};
    private int[][] tetrisBlockJ = {{2, 0, 0}, {2, 2, 2}};
    private int[][] tetrisBlockL = {{0, 0, 3}, {3, 3, 3}};
    private int[][] tetrisBlockO = {{4, 4}, {4, 4}};
    private int[][] tetrisBlockS = {{0, 5, 5}, {5, 5, 0}};
    private int[][] tetrisBlockT = {{0, 6, 0}, {6, 6, 6}};
    private int[][] tetrisBlockZ = {{7, 7, 0}, {0, 7, 7}};

    private int[][] currentBlockState;

    //starting piece positions
    private int blockColLocation = 7;
    private int blockRowLocation = 0;

    Block() {
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
                this.currentBlockState = tetrisBlockI;
                return tetrisBlockI;
            case 1:
                this.currentBlockState = tetrisBlockJ;
                return tetrisBlockJ;
            case 2:
                this.currentBlockState = tetrisBlockL;
                return tetrisBlockL;
            case 3:
                this.currentBlockState = tetrisBlockO;
                return tetrisBlockO;
            case 4:
                this.currentBlockState = tetrisBlockS;
                return tetrisBlockS;
            case 5:
                this.currentBlockState = tetrisBlockT;
                return tetrisBlockT;
            case 6:
                this.currentBlockState = tetrisBlockZ;
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
        int newColLength = this.currentBlockState.length;
        int newRowLength = this.currentBlockState[0].length;

        int[][] rotate90Array = new int[newRowLength][newColLength];
        for (int r = 0; r < newRowLength; r++) {
            for (int c = 0; c < newColLength; c++) {
                rotate90Array[r][c] = this.currentBlockState[newColLength - c - 1][r];
            }
        }

        // when the peice went overboard
        if (this.blockColLocation + newColLength > Board.boardWidth) {
            this.blockColLocation = Board.boardWidth - newColLength;
        }

        if (shouldRotate) {
            this.currentBlockState = rotate90Array;
        }

        return rotate90Array;
    }

    int[][] rotateLeft(boolean shouldRotate) {
        int newColLength = this.currentBlockState.length;
        int newRowLength = this.currentBlockState[0].length;

        int[][] rotate270Array = new int[newRowLength][newColLength];
        for (int r = 0; r < newRowLength; r++) {
            for (int c = 0; c < newColLength; c++) {
                rotate270Array[r][c] = this.currentBlockState[c][newRowLength - r - 1];
            }
        }

        if (this.blockColLocation + newColLength > Board.boardWidth) {
            this.blockColLocation = Board.boardWidth - newColLength;
        }
        if (shouldRotate) {
            this.currentBlockState = rotate270Array;
        }

        return rotate270Array;
    }

    //mover
    void moveLeft() {
        if (this.blockColLocation > 0) {
            this.blockColLocation--;
        }
    }

    void moveRight() {
        if (this.blockColLocation + currentBlockState[0].length < Board.boardWidth) {
            this.blockColLocation++;
        }
    }

    void moveDown() {
        if (currentBlockState.length + this.blockRowLocation < Board.boardHeight) {
            this.blockRowLocation++;
        }
    }
}

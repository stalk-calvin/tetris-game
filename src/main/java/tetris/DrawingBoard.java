package tetris;

import static tetris.PlayMusic.playMusic;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.util.HashMap;
import java.util.Map;

class DrawingBoard extends Canvas {

    private static final int PIECE_WIDTH = 16;
    private static final int xOffset = 5;
    private static final int yOffset = 3;

    private static final int BLOCKDRAWSIZEPIECE = 14;
    private static final int FIRST_SCORE_BRACKET = 500;
    private static final int SECOND_SCORE_BRACKET = 1000;
    private static final int THIRD_SCORE_BRACKET = 3000;
    private static final int FOURTH_SCORE_BRACKET = 5000;
    private static final int FIFTH_SCORE_BRACKET = 10000;
    private static final int SIXTH_SCORE_BRACKET = 20000;
    private static final int SEVENTH_SCORE_BRACKET = 30000;
    private static final int GAMEAREAWIDTH = Board.boardWidth * PIECE_WIDTH + xOffset;
    private static final int GAMEAREAHEIGHT = Board.boardHeight * PIECE_WIDTH + yOffset;

    private static final int GAMEAREAXLOC = 1;
    private static final int GAMEAREAYLOC = 1;
    private static final int NPAREAXLOC = Board.boardWidth * PIECE_WIDTH + 10;
    private static final int NPAREAYLOC = 1;
    private static final int NPWIDTH = 128;
    private static final int NPHEIGHT = 100;
    private static final int GAMEINFOWIDTH = 128;
    private static final int GAMEINFOHEIGHT = 299;
    private static final int GAMEINFOXLOC = Board.boardWidth * PIECE_WIDTH + 10;
    private static final int GAMEINFOYLOC = 100 + yOffset;
    private static final int SCOREXLOC = Board.boardWidth * PIECE_WIDTH + 17;
    private static final int SCOREYLOC = 120 + yOffset;
    private static final int ROWSXLOC = Board.boardWidth * PIECE_WIDTH + 17;
    private static final int ROWSYLOC = 160 + yOffset;
    private static final int ROTATIONXLOC = Board.boardWidth * PIECE_WIDTH + 12;
    private static final int ROTATIONYLOC = 200 + yOffset;
    private static final int LEVELXLOC = Board.boardWidth * PIECE_WIDTH + 12;
    private static final int LEVELYLOC = 220 + yOffset;
    private static final int TIMELEFTXLOC = Board.boardWidth * PIECE_WIDTH + 12;
    private static final int TIMELEFTYLOC = 240 + yOffset;

    private int[][] boardArray;
    private int[][] piece, nextPiece;

    private int rowLocation = 0;
    private int columnLocation = 0;

    private boolean drawGameOver = false;
    private boolean drawPaused = false;
    private Map<Integer, Boolean> playMusicTracker;

    private int completedRows = 0;
    private long score = 0;
    private int level = 0;
    private int secondsTillNextLevel = 0;
    private String rotationDirection = "";

    private Image offscreen;

    DrawingBoard() {
        playMusicTracker = new HashMap<>();
        playMusicTracker.put(FIRST_SCORE_BRACKET, false);
        playMusicTracker.put(SECOND_SCORE_BRACKET, false);
        playMusicTracker.put(THIRD_SCORE_BRACKET, false);
        playMusicTracker.put(FOURTH_SCORE_BRACKET, false);
        playMusicTracker.put(FIFTH_SCORE_BRACKET, false);
        playMusicTracker.put(SIXTH_SCORE_BRACKET, false);
        playMusicTracker.put(SEVENTH_SCORE_BRACKET, false);
    }

    void setArrayPiece(int[][] arr) {
        piece = arr;
    }

    void setNextPiece(int[][] arr) {
        nextPiece = arr;
    }

    void setGameOver(boolean go) {
        drawGameOver = go;
    }

    void setArrayBoard(int[][] arr) {
        boardArray = arr;
    }

    void setPaintLocation(int columnLocation, int rowLocation) {
        this.columnLocation = columnLocation;
        this.rowLocation = rowLocation;
    }

    void setRowsComplete(int rows) {
        completedRows = rows;
    }

    void setScore(long score) {
        this.score = score;
    }

    void setPaused(boolean isPaused) {
        drawPaused = isPaused;
    }

    void setLevel(int level) {
        this.level = level;
    }

    void setSeconds(int secs) {
        secondsTillNextLevel = secs;
    }

    void setRotationDirection(String direction) {
        rotationDirection = direction;
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    public void paint(Graphics g) {
        if (offscreen == null) {
            offscreen = createImage(getWidth(), getHeight());
        }

        // setup canvas
        Graphics graphics = offscreen.getGraphics();
        graphics.clearRect(0, 0, getWidth(), getHeight());

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, getWidth(), getHeight());

        graphics.setColor(Color.BLACK);

        // where piece drops (game area)
        graphics.drawRect(GAMEAREAXLOC, GAMEAREAYLOC, GAMEAREAWIDTH, GAMEAREAHEIGHT);

        // next piece
        graphics.drawRect(NPAREAXLOC, NPAREAYLOC, NPWIDTH, NPHEIGHT);

        // game info area
        graphics.drawRect(GAMEINFOXLOC, GAMEINFOYLOC, GAMEINFOWIDTH, GAMEINFOHEIGHT);

        // current piece
        int r = PIECE_WIDTH * rowLocation + yOffset;
        int c = PIECE_WIDTH * columnLocation + xOffset;

        for (int[] aPiece : piece) {
            for (int j = 0; j < piece[0].length; j++) {
                if (aPiece[j] != 0) {
                    graphics.setColor(Block.getColour(aPiece[j]));
                    graphics.drawRect(c, r, BLOCKDRAWSIZEPIECE, BLOCKDRAWSIZEPIECE);
                }
                c += PIECE_WIDTH;
            }
            c = PIECE_WIDTH * columnLocation + xOffset;
            r += PIECE_WIDTH;
        }

        // let's fill it
        r = yOffset;
        c = xOffset;
        for (int[] aBoardArray : boardArray) {
            for (int l = 0; l < boardArray[0].length; l++) {
                if (aBoardArray[l] != 0) {
                    graphics.setColor(Block.getColour(aBoardArray[l]));
                    graphics.fillRect(c, r, BLOCKDRAWSIZEPIECE, BLOCKDRAWSIZEPIECE);
                }
                c += PIECE_WIDTH;
            }
            c = xOffset;
            r += PIECE_WIDTH;
        }
        graphics.setColor(Color.BLACK);

        //next piece setup
        int midr = (NPHEIGHT - (nextPiece.length * PIECE_WIDTH)) / 2;
        int midc = (NPWIDTH - (nextPiece[0].length * PIECE_WIDTH)) / 2;
        int row = 2 + midr, col = Board.boardWidth * PIECE_WIDTH + xOffset + xOffset + midc;

        //draw next piece
        for (int[] aNextPiece : nextPiece) {
            for (int j = 0; j < nextPiece[0].length; j++) {
                if (aNextPiece[j] != 0) {
                    graphics.setColor(Block.getColour(aNextPiece[j]));
                    graphics.drawRect(col, row, BLOCKDRAWSIZEPIECE, BLOCKDRAWSIZEPIECE);
                }
                col += PIECE_WIDTH;
            }
            col = Board.boardWidth * PIECE_WIDTH + 10 + midc;
            row += PIECE_WIDTH;
        }

        //info section
        graphics.setColor(Color.BLACK);
        graphics.setFont(new Font("", Font.BOLD, 12));
        graphics.drawString("Level: " + level, LEVELXLOC, LEVELYLOC);
        graphics.drawString("Rotation: " + rotationDirection, ROTATIONXLOC, ROTATIONYLOC);
        graphics.drawString("Next level in: " + secondsTillNextLevel, TIMELEFTXLOC, TIMELEFTYLOC);
        graphics.drawString("Completed Rows: ", ROWSXLOC, ROWSYLOC);
        graphics.drawString(String.valueOf(completedRows), ROWSXLOC+20, ROWSYLOC+20);

        //score alert
        if (score >= FIRST_SCORE_BRACKET && !playMusicTracker.get(FIRST_SCORE_BRACKET)) {
            new Thread(() -> {
                playMusic("wav/holy_alphabet.wav", false);
            }).start();
            playMusicTracker.put(FIRST_SCORE_BRACKET, true);
        } else if (score >= SECOND_SCORE_BRACKET && !playMusicTracker.get(SECOND_SCORE_BRACKET)) {
            new Thread(() -> {
                playMusic("wav/holy_caffeine.wav", false);
            }).start();
            playMusicTracker.put(SECOND_SCORE_BRACKET, true);
        } else if (score >= THIRD_SCORE_BRACKET && !playMusicTracker.get(THIRD_SCORE_BRACKET)) {
            new Thread(() -> {
                playMusic("wav/holy_fruit_salad.wav", false);
            }).start();
            playMusicTracker.put(THIRD_SCORE_BRACKET, true);
        } else if (score >= FOURTH_SCORE_BRACKET && !playMusicTracker.get(FOURTH_SCORE_BRACKET)) {
            new Thread(() -> {
                playMusic("wav/holy_heart_failure.wav", false);
            }).start();
            playMusicTracker.put(FOURTH_SCORE_BRACKET, true);
        } else if (score >= FIFTH_SCORE_BRACKET && !playMusicTracker.get(FIFTH_SCORE_BRACKET)) {
            new Thread(() -> {
                playMusic("wav/holy_mashed_potatoes.wav", false);
            }).start();
            playMusicTracker.put(FIFTH_SCORE_BRACKET, true);
        } else if (score >= SIXTH_SCORE_BRACKET && !playMusicTracker.get(SIXTH_SCORE_BRACKET)) {
            new Thread(() -> {
                playMusic("wav/holy_nightmare.wav", false);
            }).start();
            playMusicTracker.put(SIXTH_SCORE_BRACKET, true);
        } else if (score >= SEVENTH_SCORE_BRACKET && !playMusicTracker.get(SEVENTH_SCORE_BRACKET)) {
            new Thread(() -> {
                playMusic("wav/bitchin.wav", false);
            }).start();
            playMusicTracker.put(SEVENTH_SCORE_BRACKET, true);
        }

        //score board
        if (score >= SEVENTH_SCORE_BRACKET) {
            graphics.drawString("Bitchin!", SCOREXLOC, SCOREYLOC);

        } else if (score >= SIXTH_SCORE_BRACKET) {
            graphics.drawString("Holy Nightmare!", SCOREXLOC, SCOREYLOC);
        } else if (score >= FIFTH_SCORE_BRACKET) {
            graphics.drawString("Holy Mashed Potato!", SCOREXLOC, SCOREYLOC);
        } else if (score >= FOURTH_SCORE_BRACKET) {
            graphics.drawString("Holy Heart Failure!", SCOREXLOC, SCOREYLOC);
        } else if (score >= THIRD_SCORE_BRACKET) {
            graphics.drawString("Holy Fruit Salad!", SCOREXLOC, SCOREYLOC);
        } else if (score >= SECOND_SCORE_BRACKET) {
            graphics.drawString("Holy Caffeine!", SCOREXLOC, SCOREYLOC);
        } else if (score >= FIRST_SCORE_BRACKET) {
            graphics.drawString("Holy Alphabet!", SCOREXLOC, SCOREYLOC);
        } else {
            graphics.drawString("Score: ", SCOREXLOC, SCOREYLOC);
        }
        graphics.drawString(String.valueOf(score), SCOREXLOC+20, SCOREYLOC+20);

        //game over
        if (drawPaused) {
            graphics.setFont(new Font("", Font.BOLD, 16));
            graphics.setColor(Color.BLUE);
            graphics.drawString("PAUSED", Board.boardWidth * PIECE_WIDTH + 23, 300);
            graphics.drawString("Press 'p' ", Board.boardWidth * PIECE_WIDTH + 23, 320);
            graphics.drawString("to continue", Board.boardWidth * PIECE_WIDTH + 23, 340);
            graphics.drawString("Press 'x' ", Board.boardWidth * PIECE_WIDTH + 23, 360);
            graphics.drawString("to restart", Board.boardWidth * PIECE_WIDTH + 23, 380);
        } else if (drawGameOver) {
            new Thread(() -> {
                playMusic("wav/game_over.wav", false);
            }).start();
            playMusicTracker.put(FIRST_SCORE_BRACKET, true);
            graphics.setFont(new Font("", Font.BOLD, 16));
            graphics.setColor(Color.BLUE);
            graphics.drawString("GAME OVER!", Board.boardWidth * PIECE_WIDTH + 23, 300);
            graphics.drawString("Press 'x' ", Board.boardWidth * PIECE_WIDTH + 23, 320);
            graphics.drawString("to restart", Board.boardWidth * PIECE_WIDTH + 23, 340);
        }

        graphics.setFont(new Font("", Font.PLAIN, 12));
        graphics.setColor(Color.BLACK);
        graphics.drawString("By Calvin Lee", 5, 15);

        graphics.dispose();

        //draw!
        g.drawImage(offscreen, 0, 0, this);
    }

}

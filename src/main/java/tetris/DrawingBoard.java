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
    private static final int BLOCKDRAWSIZEPIECE = 14;
    private static final int FIRST_SCORE_BRACKET = 50;
    private static final int SECOND_SCORE_BRACKET = 1000;
    private static final int THIRD_SCORE_BRACKET = 3000;
    private static final int FOURTH_SCORE_BRACKET = 5000;
    private static final int FIFTH_SCORE_BRACKET = 10000;
    private static final int SIXTH_SCORE_BRACKET = 50000;
    private static final int SEVENTH_SCORE_BRACKET = 100000;
    private static final int GAMEAREAWIDTH = Board.boardWidth * PIECE_WIDTH + 5;
    private static final int GAMEAREAHEIGHT = Board.boardHeight * PIECE_WIDTH + 3;

    private static final int GAMEAREAXLOC = 1;
    private static final int GAMEAREAYLOC = 1;
    private static final int NPAREAXLOC = Board.boardWidth * PIECE_WIDTH + 10;
    private static final int NPAREAYLOC = 1;
    private static final int NPWIDTH = 128;
    private static final int NPHEIGHT = 100;
    private static final int GAMEINFOWIDTH = 128;
    private static final int GAMEINFOHEIGHT = 299;
    private static final int GAMEINFOXLOC = Board.boardWidth * PIECE_WIDTH + 10;
    private static final int GAMEINFOYLOC = 105;
    private static final int SCOREXLOC = Board.boardWidth * PIECE_WIDTH + 17;
    private static final int SCOREYLOC = 125;
    private static final int ROWSXLOC = Board.boardWidth * PIECE_WIDTH + 12;
    private static final int ROWSYLOC = 165;
    private static final int ROTATIONXLOC = Board.boardWidth * PIECE_WIDTH + 12;
    private static final int ROTATIONYLOC = 185;
    private static final int LEVELXLOC = Board.boardWidth * PIECE_WIDTH + 12;
    private static final int LEVELYLOC = 205;
    private static final int TIMELEFTXLOC = Board.boardWidth * PIECE_WIDTH + 12;
    private static final int TIMELEFTYLOC = 225;

    private int[][] boardArray;
    private int[][] piece, nextPiece;

    private int rowMultiplyer = 0;
    private int columnMultiplyer = 0;

    private boolean drawGameOver = false;
    private boolean drawPaused = false;
    private Map<Integer, Boolean> playMusicTracker;

    private int completedRows = 0;
    private long score = 0;
    private int Level = 0;
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
        this.piece = arr;
    }

    void setNextPiece(int[][] arr) {
        this.nextPiece = arr;
    }

    void setGameOver(boolean go) {
        this.drawGameOver = go;
    }

    void setArrayBoard(int[][] arr) {
        this.boardArray = arr;
    }

    void setPaintLocation(int columnLocation, int rowLocation) {
        this.columnMultiplyer = columnLocation;
        this.rowMultiplyer = rowLocation;
    }

    void setRowsComplete(int rows) {
        this.completedRows = rows;
    }

    void setScore(long score) {
        this.score = score;
    }

    void setPaused(boolean Paused) {
        this.drawPaused = Paused;
    }

    void setLevel(int Level) {
        this.Level = Level;
    }

    void setSeconds(int secs) {
        this.secondsTillNextLevel = secs;
    }

    void setRotationDirection(String direction) {
        rotationDirection = direction;
    }

    public void update(Graphics g) {
        this.paint(g);
    }

    public void paint(Graphics g) {
        if (offscreen == null) {
            offscreen = createImage(this.getWidth(), this.getHeight());
        }

        Graphics graphics = offscreen.getGraphics();
        graphics.clearRect(0, 0, this.getWidth(), this.getHeight());

        graphics.setColor(Color.WHITE);
        graphics.fillRect(0, 0, this.getWidth(), this.getHeight());

        graphics.setColor(Color.BLACK);

        // where piece drops
        graphics.drawRect(GAMEAREAXLOC, GAMEAREAYLOC, GAMEAREAWIDTH, GAMEAREAHEIGHT);

        // next piece
        graphics.drawRect(NPAREAXLOC, NPAREAYLOC, NPWIDTH, NPHEIGHT);

        //Draw a white border to make a 'game info area'
        graphics.drawRect(GAMEINFOXLOC, GAMEINFOYLOC, GAMEINFOWIDTH, GAMEINFOHEIGHT);

        // current piece
        int r = PIECE_WIDTH * rowMultiplyer + 3, c = PIECE_WIDTH * columnMultiplyer;

        for (int[] aPiece : piece) {
            for (int j = 0; j < piece[0].length; j++) {
                if (aPiece[j] != 0) {
                    graphics.setColor(Block.getColour(aPiece[j]));
                    graphics.drawRect(c + 5, r, BLOCKDRAWSIZEPIECE, BLOCKDRAWSIZEPIECE);
                }
                c += PIECE_WIDTH;
            }
            c = PIECE_WIDTH * columnMultiplyer;
            r += PIECE_WIDTH;
        }

        // board
        r = 3;
        c = 0;
        for (int[] aBoardArray : boardArray) {
            for (int l = 0; l < boardArray[0].length; l++) {
                if (aBoardArray[l] != 0) {
                    graphics.setColor(Block.getColour(aBoardArray[l]));
                    graphics.fillRect(c + 5, r, BLOCKDRAWSIZEPIECE, BLOCKDRAWSIZEPIECE);
                }
                c += PIECE_WIDTH;
            }
            c = 0;
            r += PIECE_WIDTH;
        }
        graphics.setColor(Color.BLACK);

        //next piece setup
        int midr = (NPHEIGHT - (this.nextPiece.length * PIECE_WIDTH)) / 2;
        int midc = (NPWIDTH - (this.nextPiece[0].length * PIECE_WIDTH)) / 2;
        int row = 2 + midr, col = Board.boardWidth * PIECE_WIDTH + 5 + 5 + midc;

        //draw next piece
        for (int[] aNextPiece : this.nextPiece) {
            for (int j = 0; j < this.nextPiece[0].length; j++) {
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
        graphics.drawString("Level: " + this.Level, LEVELXLOC, LEVELYLOC);
        graphics.drawString("Rotation: " + this.rotationDirection, ROTATIONXLOC, ROTATIONYLOC);
        graphics.drawString("Next level in: " + secondsTillNextLevel, TIMELEFTXLOC, TIMELEFTYLOC);
        graphics.drawString("Completed Rows: " + this.completedRows, ROWSXLOC, ROWSYLOC);

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

        //game paused
        if (this.drawPaused) {
            graphics.setFont(new Font("", Font.BOLD, 16));
            graphics.setColor(Color.BLUE);
            graphics.drawString("PAUSED", Board.boardWidth * PIECE_WIDTH + 30, 300);
            graphics.drawString("Press 'P' ", Board.boardWidth * PIECE_WIDTH + 30, 320);
            graphics.drawString("to continue", Board.boardWidth * PIECE_WIDTH + 30, 340);
        }

        //game over
        if (this.drawGameOver) {
            new Thread(() -> {
                playMusic("wav/game_over.wav", false);
            }).start();
            playMusicTracker.put(FIRST_SCORE_BRACKET, true);
            graphics.setFont(new Font("", Font.BOLD, 16));
            graphics.setColor(Color.BLUE);
            graphics.drawString("GAME OVER!", Board.boardWidth * PIECE_WIDTH + 40, 300);
        }

        graphics.setFont(new Font("", Font.PLAIN, 12));
        graphics.setColor(Color.BLACK);
        graphics.drawString("By Calvin Lee", 5, 15);

        graphics.dispose();

        //draw!
        g.drawImage(offscreen, 0, 0, this);
    }

}
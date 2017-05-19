package tetris;

import static tetris.PlayMusic.playMusic;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JFrame;
import timer.Alarmable;
import timer.CountDown;
import timer.TimerEvent;

class Tetris extends JFrame implements Runnable, Alarmable, KeyListener {

    // Points distribution per row
    private static final String TITLE = "Calvin's Tetris";
    private static final int ONE_ROW_CLEARED_POINT = 100;
    private static final int TWO_ROW_CLEARED_POINT = 300;
    private static final int THREE_ROW_CLEARED_POINT = 600;
    private static final int FOUR_ROW_CLEARED_POINT = 900;
    private static final int DROP_INTERVAL = 600;
    private static final int DROP_TIME_REDUCTION = 50; // Gets faster every level in ms
    private static final int MINUTE_TILL_NEXT_LVL = 1;  // Minutes until next level
    private static final String INITIAL_ROTATION = "Right";
    private static boolean gameOver = false;    // Game over?
    private Block cp, np;                       // current and next pieces
    private DrawingBoard canvas;                      // canvas to draw stuffs
    private Board b1 = new Board();             // The game board representing where the blocks are
    private boolean paused = false;             // paused?
    private long totalPoints = 0;               // keep track of points
    private int rowsCleared = 0;
    private int level = 0;                      // The current level the player is on
    private int dropWait = DROP_INTERVAL;       // The amount of time until the piece drops a row, 600 ms
    private int tempRes = 0;                    // feasible to place piece?
    private char rotateDirection = 'R';
    private CountDown countdown = new timer.CountDown(MINUTE_TILL_NEXT_LVL);

    Tetris() {
        // Let's register as alarmable to speedup/levelup
        countdown.addTimesUpListener(this);

        // Add title to the frame
        this.setTitle(TITLE);

        Runnable task = () -> { playMusic("wav/bgMusic.wav", true); };
        new Thread(task).start();

        // add a new game canvas to JFrame
        canvas = new DrawingBoard();
        canvas.setFocusable(false);
        canvas.setRotationDirection(INITIAL_ROTATION);
        this.add(canvas);

        // current piece chosen
        cp = new Block();
        cp.getRandom();

        // need next piece chosen
        np = new Block();
        np.getRandom();

        // Listen to key event
        this.addKeyListener(this);

        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        this.setLocation(dim.width/2-this.getWidth(), dim.height/2-this.getHeight());

        // thread start!
        start();
    }

    @Override
    public int getWidth() {
        return Block.BLOCK_LENGTH * Board.boardWidth + 140;
    }

    @Override
    public int getHeight() {
        return Block.BLOCK_LENGTH * Board.boardHeight + 28;
    }

    public void run() {
        while (!gameOver) {
            if (!paused) {
                canvas.setSeconds(Integer.parseInt(countdown.formatAsDate(countdown.getTotalTime()).split(":")[2]));

                // is it possible to place piece?
                tempRes = b1.placePiece(cp.getCurrentState(), cp.getRowNum() + 1, cp.getColNum(), false);

                if (tempRes == 1) {
                    // Not possible, place the piece where it is and we are done
                    piecePlaced();
                } else {
                    // current piece is falling ...
                    cp.moveDown();
                }
                drawGame();
            }

            try {
                Thread.sleep(dropWait);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }

        }
    }

    private void piecePlaced() {
        int rows;

        b1.placePiece(cp.getCurrentState(), cp.getRowNum(), cp.getColNum(), true);

        if (b1.isGameOver()) {
            gameOver = true;
            canvas.setGameOver(true);
        } else {
            // current piece is whatever next one was
            cp = np;

            // next is assigned to a new random one
            np = new Block();
            np.getRandom();

            rows = b1.calculateRowsCleared();
            rowsCleared += rows;   // cleared so far
            calculatePoints(rows);
        }
    }

    private void start() {
        gameOver = false;
        new Thread(this).start();
    }

    private void restart() {
        countdown = new timer.CountDown(MINUTE_TILL_NEXT_LVL);
        countdown.addTimesUpListener(this);

        b1 = new Board();

        cp = new Block();
        cp.getRandom();

        np = new Block();
        np.getRandom();

        dropWait = DROP_INTERVAL;

        paused = false;
        canvas.setPaused(false);
        canvas.setGameOver(false);
        canvas.setLevel(0);
        totalPoints = 0;
        rowsCleared = 0;
        level = 0;

        gameOver = true;

        //This needs to be here to give time for the current game to end (a bit hacky)
        try {
            Thread.sleep(500);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        start(); //Gets the game rolling again
    }

    private void calculatePoints(int Rows) {
        switch (Rows) {
            case 0:
                totalPoints += 0;
                break;
            case 1:
                totalPoints += ONE_ROW_CLEARED_POINT;
                break;
            case 2:
                totalPoints += TWO_ROW_CLEARED_POINT;
                break;
            case 3:
                totalPoints += THREE_ROW_CLEARED_POINT;
                break;
            case 4:
                totalPoints += FOUR_ROW_CLEARED_POINT;
                break;
        }
    }

    private void drawGame() {
        canvas.setScore(totalPoints);          // Sets score value
        canvas.setRowsComplete(rowsCleared);   // Sets rows cleared value
        canvas.setNextPiece(np.getCurrentState());  // Sets the next piece
        canvas.setArrayBoard(b1.getBoardArray());   // Sets the board
        canvas.setPaintLocation(cp.getColNum(), cp.getRowNum());   //Sets the location to draw the current piece
        canvas.setArrayPiece(cp.getCurrentState()); // Sets the current piece

        canvas.repaint(); // PAINT GAME CANVAS AGAIN
    }

    public void keyPressed(KeyEvent e) {
        if (paused && e.getKeyCode() != KeyEvent.VK_P && e.getKeyCode() != KeyEvent.VK_X) {
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            new Thread(() -> {
                playMusic("wav/rotate.wav", false);
            }).start();
            tempRes = b1.placePiece(cp.getCurrentState(), cp.getRowNum() + 1, cp.getColNum(), false);
            if (tempRes == 1) {
                piecePlaced();
            } else {
                totalPoints++;
                cp.moveDown();
                drawGame();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            new Thread(() -> {
                playMusic("wav/rotate.wav", false);
            }).start();
            tempRes = b1.placePiece(cp.rotateRight(false), cp.getRowNum(), cp.getColNum(), false);
            if (tempRes != 1) {
                if (rotateDirection == 'R') {
                    cp.rotateRight(true);
                    drawGame();
                } else {
                    cp.rotateLeft(true);
                    drawGame();
                }
            }
        } else if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            new Thread(() -> {
                playMusic("wav/rotate.wav", false);
            }).start();
            tempRes = b1.placePiece(cp.getCurrentState(), cp.getRowNum(), cp.getColNum() + 1, false);
            if (tempRes != 1) {
                cp.moveRight();
                drawGame();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            new Thread(() -> {
                playMusic("wav/rotate.wav", false);
            }).start();
            tempRes = b1.placePiece(cp.getCurrentState(), cp.getRowNum(), cp.getColNum() - 1, false);
            if (tempRes != 1) {
                cp.moveLeft();
                drawGame();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            new Thread(() -> {
                playMusic("wav/drop.wav", false);
            }).start();
            boolean movedDown = false;
            while (!movedDown) {
                tempRes = b1.placePiece(cp.getCurrentState(), cp.getRowNum() + 1, cp.getColNum(), false);
                if (tempRes == 1) {
                    piecePlaced();
                    movedDown = true;
                } else {
                    totalPoints += 2;
                    movedDown = cp.moveDown();
                }
            }
            drawGame();
        } else if (e.getKeyCode() == KeyEvent.VK_R) {
            new Thread(() -> {
                playMusic("wav/rotate.wav", false);
            }).start();
            if (rotateDirection == 'R') {
                rotateDirection = 'L';
                canvas.setRotationDirection("Left");
            } else {
                rotateDirection = 'R';
                canvas.setRotationDirection("Right");
            }
            drawGame();
        } else if (e.getKeyCode() == KeyEvent.VK_P) {
            new Thread(() -> {
                playMusic("wav/tap.wav", false);
            }).start();
            pauseGame();
        } else if (e.getKeyCode() == KeyEvent.VK_X) {
            new Thread(() -> {
                playMusic("wav/tap.wav", false);
            }).start();
            restart();
        }
    }

    void pauseGame() {
        paused = !paused;
        canvas.setPaused(paused);
        countdown.pause(paused);
        drawGame();
    }

    public void timesUp(TimerEvent Event) {
        new Thread(() -> {
            playMusic("wav/levelup.wav", false);
        }).start();
        dropWait -= DROP_TIME_REDUCTION;
        canvas.setLevel(++level);
        countdown = new timer.CountDown(MINUTE_TILL_NEXT_LVL);
        countdown.addTimesUpListener(this);
    }

    public void keyTyped(KeyEvent e) {
    }

    public void keyReleased(KeyEvent e) {
    }
}

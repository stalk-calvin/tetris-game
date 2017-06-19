package tetris;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BoardTest {
    private Board fixture;

    @Before
    public void setUp() {
        fixture = new Board();
    }

    @Test
    public void shouldHaveCorrectHeight() {
        assertEquals(Board.boardHeight, fixture.getBoardArray().length);
    }

    @Test
    public void shouldHaveCorrectWidth() {
        assertEquals(Board.boardWidth, fixture.getBoardArray()[0].length);
    }

    @Test
    public void shouldPlaceARandomPiece() {
        Block cp = new Block();

        assertEquals(0, fixture.placePiece(cp.getRandom(),cp.getRowNum(),cp.getColNum(),true));

        int col = cp.getColNum();
        int actualFirstRow = fixture.getBoardArray()[0][col - 2] + fixture.getBoardArray()[0][col - 1] + fixture
                .getBoardArray()[0][col] + fixture.getBoardArray()[0][col + 1] + fixture.getBoardArray()[0][col + 2];
        int actualSecondRow = fixture.getBoardArray()[1][col - 2] + fixture.getBoardArray()[1][col - 1] + fixture
                .getBoardArray()[1][col] + fixture.getBoardArray()[1][col + 1] + fixture.getBoardArray()[1][col + 2];
        assertTrue(actualFirstRow > 0);
        assertTrue(actualSecondRow > 0);
    }

    @Test
    public void shouldNotBeGameOverOnInitialBoard() {
        assertFalse(fixture.isGameOver());
    }

    @Test
    public void shouldCalculatePointsOnInitialBoard() {
        assertEquals(0, fixture.calculateRowsCleared());
    }

}

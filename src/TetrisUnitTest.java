import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Color;

import org.junit.Test;

public class TetrisUnitTest {
	@Test
	public void testMoveSquare() {
		Grid g = new Grid();
		Square s = new Square(g, 4, 4, Color.MAGENTA, true);
		// s is blocked on the right
		g.set(4, 5, Color.YELLOW);
		// s should not be able to move to the right
		boolean b = s.canMove(Direction.RIGHT);
		// System.out.println("b = " + b);
		assertFalse(b);
		// s should be able to move left and down
		assertTrue(s.canMove(Direction.LEFT));
		assertTrue(s.canMove(Direction.DOWN));
		// move s down
		s.move(Direction.DOWN);
		assertTrue(s.getRow() == 5 && s.getCol() == 4);
	}

	@Test
	public void testMoveLShape() {

		Grid g = new Grid();

		// l1 starts in the far left corner
		LShape l1 = new LShape(1, 0, g);

		// l1 should not be able to move to the left
		assertFalse(l1.canMove(Direction.LEFT));

		// count how many moves it takes to go to the right
		int m = 0;
		while (l1.canMove(Direction.RIGHT)) {
			l1.move(Direction.RIGHT);
			m++;
		}

		// it should take be two less than the width of the grid
		// as we are taking into account the shape of the piece
		assertTrue(m == Grid.WIDTH - 2);

		// drop down l1
		l1.move(Direction.DROP);

		// l1 should drop down to the bottom right corner
		// we check that each square of the l1 landed in the correct location
		// check square 0
		assertTrue(l1.getLocations()[0].getX() == (double) (Grid.HEIGHT - 3));
		assertTrue(l1.getLocations()[0].getY() == (double) (Grid.WIDTH - 2));

		// check square 1
		assertTrue(l1.getLocations()[1].getX() == (double) (Grid.HEIGHT - 2));
		assertTrue(l1.getLocations()[1].getY() == (double) (Grid.WIDTH - 2));

		// check square 2
		assertTrue(l1.getLocations()[2].getX() == (double) (Grid.HEIGHT - 1));
		assertTrue(l1.getLocations()[2].getY() == (double) (Grid.WIDTH - 2));

		// check square 3
		assertTrue(l1.getLocations()[3].getX() == (double) (Grid.HEIGHT - 1));
		assertTrue(l1.getLocations()[3].getY() == (double) (Grid.WIDTH - 1));

		// l2 starts in the middle of the grid
		LShape l2 = new LShape(Grid.HEIGHT / 2 - 1, Grid.WIDTH / 2 - 1, g);

		// drop down l2
		l2.move(Direction.DROP);

		// l2 should land at the bottom of the grid, but have the same Y
		// location
		// we check that each square of the l2 landed in the correct place
		// check square 0
		assertTrue(l2.getLocations()[0].getX() == (double) (Grid.HEIGHT - 3));
		assertTrue(l2.getLocations()[0].getY() == (double) (Grid.WIDTH / 2 - 1));

		// check square 1
		assertTrue(l2.getLocations()[1].getX() == (double) (Grid.HEIGHT - 2));
		assertTrue(l2.getLocations()[1].getY() == (double) (Grid.WIDTH / 2 - 1));

		// check square 2
		assertTrue(l2.getLocations()[2].getX() == (double) (Grid.HEIGHT - 1));
		assertTrue(l2.getLocations()[2].getY() == (double) (Grid.WIDTH / 2 - 1));

		// check square 3
		assertTrue(l2.getLocations()[3].getX() == (double) (Grid.HEIGHT - 1));
		assertTrue(l2.getLocations()[3].getY() == (double) (Grid.WIDTH / 2));
	}

	/**
	 * An example of a unit test for check rows Add your own grid example
	 */
	@Test
	public void testCheckRows() {
		// Create a grid with a few complete rows
		// e.g.
		/**
		 * <pre>
		 *             -> row = 0
		 *  XXXXXXXXXX -> row = 1
		 *    XXXX     -> row = 2
		 *     XX      -> row = 3
		 *     XX      -> row = 4
		 *  XXXXXXXXXX -> row = 5
		 *     XX      -> row = 6
		 *     XX      -> row = 7
		 *  XXXXXXXXXX -> row = 8
		 *     XX      -> row = 9
		 *  (empty rows from row = 10 to 19)
		 * </pre>
		 */
		// After calling checkRows, the grid should be
		/**
		 * <pre>
		 *             -> row = 0
		 *             -> row = 1
		 *             -> row = 2
		 *             -> row = 3
		 *    XXXX     -> row = 4
		 *     XX      -> row = 5
		 *     XX      -> row = 6
		 *     XX      -> row = 7
		 *     XX      -> row = 8
		 *     XX      -> row = 9
		 *  (empty rows from row = 10 to 19)
		 * </pre>
		 */
		Grid g = new Grid();
		// rows[r] = number of non empty squares on row r (the non empty squares
		// are centered)
		int[] rows = { 0, 10, 4, 2, 2, 10, 2, 2, 10, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int r = 0; r < Grid.HEIGHT; r++) {
			int cLeft = 0, cRight = Grid.WIDTH - 1;
			while (cLeft <= cRight) {
				if (cRight - cLeft < rows[r]) {
					g.set(r, cLeft, Color.MAGENTA);
					g.set(r, cRight, Color.MAGENTA);
				}
				cLeft++;
				cRight--;
			}
		}

		g.checkRows();

		rows = new int[] { 0, 0, 0, 0, 4, 2, 2, 2, 2, 2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		for (int r = 0; r < Grid.HEIGHT; r++) {
			int cLeft = 0, cRight = Grid.WIDTH - 1;
			while (cLeft <= cRight) {
				if (cRight - cLeft < rows[r]) {
					assertTrue(g.isSet(r, cLeft));
					assertTrue(g.isSet(r, cRight));
				} else {
					assertFalse(g.isSet(r, cLeft));
					assertFalse(g.isSet(r, cRight));
				}
				cLeft++;
				cRight--;
			}
		}
	}

	@Test
	public void checkRows2() {
		Grid g = new Grid();
		for (int r = 0; r < Grid.HEIGHT; r++) {
			if (r == 10) {
				for (int c = 6; c <= 7; c++) {
					g.set(r, c, Color.RED);
				}
			} else {
				for (int c = 0; c < Grid.WIDTH; c++) {
					g.set(r, c, Color.RED);
				}
			}
		}
		g.checkRows();

		// check the grid
		for (int r = 0; r < Grid.HEIGHT - 1; r++) {
			for (int c = 0; c < Grid.WIDTH; c++) {
				assertFalse(g.isSet(r, c));
			}
		}
		// bottom row
		for (int c = 0; c < Grid.WIDTH; c++) {
			if (c != 6 && c != 7) {
				assertFalse(g.isSet(Grid.HEIGHT - 1, c));
			} else {
				assertTrue(g.isSet(Grid.HEIGHT - 1, c));
			}
		}
	}

	@Test
	public void testRotateSquare() {
		Grid g = new Grid();
		Square c = new Square(g, 5, 5, Color.BLUE, true); // center square
		// rotate a square j squares away from the center
		for (int j = 1; j <= 4; j++) {
			// location: 1 square up from the bottom left corner
			Square s = new Square(g, c.getRow() + j - 1, c.getCol() - j, Color.YELLOW, true);
			int row = s.getRow();
			int col = s.getCol();
			for (int i = 1; i <= 4; i++) {
				assertTrue(s.canRotate(c));
				s.rotate(c);
				int dr = c.getRow() - row;
				int dc = c.getCol() - col;
				row = c.getRow() - dc;
				col = c.getCol() + dr;
				assertTrue(s.getRow() == row);
				assertTrue(s.getCol() == col);
			}

			// place a square in the way
			for (int k = 1; k <= j; k++) {
				int cornerRow = row + ((k < j) ? 1 : 0);
				int cornerCol = col + ((k < j) ? j - k - 1 : 0);
				for (int i = 1; i <= 4; i++) {
					int dr = c.getRow() - cornerRow;
					int dc = c.getCol() - cornerCol;
					cornerRow = c.getRow() - dc;
					cornerCol = c.getCol() + dr;
					g.set(cornerRow, cornerCol, Color.BLUE);
					assertTrue(!s.canRotate(c));
					g.set(cornerRow, cornerCol, Grid.EMPTY);
					assertTrue(s.canRotate(c));
					s.rotate(c);
					dr = c.getRow() - row;
					dc = c.getCol() - col;
					row = c.getRow() - dc;
					col = c.getCol() + dr;
					assertTrue(s.getRow() == row);
					assertTrue(s.getCol() == col);
				}
			}
		}
	}
	
	@Test
	public void testShapeRotate() {
		Grid g = new Grid();
		AbstractPiece p;
		
		// check rotation of Z-Shape
		// create Z-Shape in the middle of the grid
		p = new ZShape(Grid.HEIGHT / 2 - 1, Grid.WIDTH / 2 - 1, g);
		// make sure that it can rotate
		assertTrue(p.canRotate());
		// rotate it
		p.rotate();
		
		// move it all the way to the right
		while (p.canMove(Direction.RIGHT)) {
			p.move(Direction.RIGHT);
		}
		
		// make sure that it cannot be rotated
		assertTrue(!p.canRotate());
		p.rotate();
		
		// repeat these steps for other shapes
		
		// check rotation of J-Shape
		p = new JShape(Grid.HEIGHT / 2 - 1, Grid.WIDTH / 2 - 1, g);
		assertTrue(p.canRotate());
		
		while (p.canMove(Direction.RIGHT)) {
			p.move(Direction.RIGHT);
		}
		
		assertTrue(!p.canRotate());
		p.rotate();
		
		// check rotation of T-Shape
		p = new TShape(Grid.HEIGHT / 2 - 1, Grid.WIDTH / 2 - 1, g);
		assertTrue(p.canRotate());
		p.rotate();
		
		while (p.canMove(Direction.RIGHT)) {
			p.move(Direction.RIGHT);
		}
		
		assertTrue(!p.canRotate());
		p.rotate();
		
		// check rotation of S-Shape
		p = new SShape(Grid.HEIGHT / 2 - 1, Grid.WIDTH / 2 - 1, g);
		assertTrue(p.canRotate());
		p.rotate();
		
		while (p.canMove(Direction.RIGHT)) {
			p.move(Direction.RIGHT);
		}
		
		assertTrue(!p.canRotate());
		p.rotate();
		
		// check rotation of Bar-Shape
		p = new BarShape(Grid.HEIGHT / 2 - 1, Grid.WIDTH / 2 - 1, g);
		assertTrue(p.canRotate());
		p.rotate();
		
		while (p.canMove(Direction.RIGHT)) {
			p.move(Direction.RIGHT);
		}
		
		assertTrue(!p.canRotate());
		p.rotate();
		
		// check rotation of L	-Shape
		p = new LShape(Grid.HEIGHT / 2 - 1, Grid.WIDTH / 2 - 1, g);
		assertTrue(p.canRotate());
		p.rotate();
		p.rotate();
		
		while (p.canMove(Direction.RIGHT)) {
			p.move(Direction.RIGHT);
		}
		
		assertTrue(!p.canRotate());
		p.rotate();
		
	}
}
import java.awt.Color;
import java.awt.Graphics;

/**
 * One Square on our Tetris Grid or one square in our Tetris game piece
 * 
 * @author dtabys
 */
public class Square {
	private Grid grid; // the environment where this Square is

	private int row, col; // the grid location of this Square

	private boolean ableToMove; // true if this Square can move

	private Color color; // the color of this Square

	// possible move directions are defined by the Game class

	// dimensions of a Square
	public static final int WIDTH = 20;

	public static final int HEIGHT = 20;

	/**
	 * Creates a square
	 * 
	 * @param g
	 *            the Grid for this Square
	 * @param row
	 *            the row of this Square in the Grid
	 * @param col
	 *            the column of this Square in the Grid
	 * @param c
	 *            the Color of this Square
	 * @param mobile
	 *            true if this Square can move
	 * 
	 * @throws IllegalArgumentException
	 *             if row and col not within the Grid
	 */
	public Square(Grid g, int row, int col, Color c, boolean mobile) {
		if (row < 0 || row > Grid.HEIGHT - 1)
			throw new IllegalArgumentException("Invalid row =" + row);
		if (col < 0 || col > Grid.WIDTH - 1)
			throw new IllegalArgumentException("Invalid column  = " + col);

		// initialize instance variables
		grid = g;
		this.row = row;
		this.col = col;
		color = c;
		ableToMove = mobile;
	}

	/**
	 * Returns the row for this Square
	 */
	public int getRow() {
		return row;
	}

	/**
	 * Returns the column for this Square
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Returns true if this Square can move 1 spot in direction d
	 * 
	 * @param direction
	 *            the direction to test for possible move
	 */
	public boolean canMove(Direction direction) {
		if (!ableToMove)
			return false;

		boolean move = true;
		// if the given direction is blocked, we can't move
		// remember to check the edges of the grid
		switch (direction) {
		
		case DOWN:
			if (row == (Grid.HEIGHT - 1) || grid.isSet(row + 1, col)){
				move = false;				
			}
			break;

		// Check if you can move to the LEFT
		case LEFT:
			if (col == 0 || grid.isSet(row, col - 1)){
				move = false;			
			}
			break;
		
		// Check if you can move to the RIGHT
		case RIGHT:
			if (col == (Grid.WIDTH - 1)  || grid.isSet(row, col + 1)){
				move = false;
			}
			break;
		
		case UP:
			if (row == 0  || grid.isSet(row - 1, col)){
				move = false;
			}
			break;
			
		}
	
		return move;
	}
	/**
	 * 
	 * Checks if the square's path to rotate is clear
	 * 
	 * @param c - center/pivot square
	 * that this square is being rotated about
	 * 
	 * @return true - if the path is clear
	 * 		   false - if the destination is out of bounds
	 * 		   or the path is blocked
	 */
	public boolean canRotate(Square c) {
		
		// calculate where the row should be after rotation
		int r2 = c.row + (col - c.col);
		
		// check that it is within bounds
		if (r2 < 0 || r2 > Grid.HEIGHT - 1)
			return false;
		
		// calculate where the column should be after rotation
		int c2 = c.col + (c.row - row);
		
		// check that it is within bounds
		if (c2 < 0 || c2 > Grid.WIDTH - 1)
			return false;
		
		// how many rows and columns needs to be moved
		int rowMoves = r2 - row;
		int colMoves = c2 - col;
		
		// there are 4 cases to consider:
		// first: right then down
		// meaning that both final row & column should be
		// more than the initial row & column
		if (r2 >= row && c2 >= col) {
			
			// first check if the right of the square is clear
			for (int i = 0; i <= Math.abs(colMoves); i++){
				
				if(grid.isSet(row, col + i))
					return false;
			}
			
			// then check if the down direction with the altered
			// column is clear, return false if not
			for (int i = 0; i <= Math.abs(rowMoves); i++){
				
				if(grid.isSet(row + i, c2))		
					return false;
			}
		}
		
		// second: down then left
		// similar to the first one
		if (c2 <= col && r2 >= row) {

			for (int i = 0; i <= Math.abs(rowMoves); i++){
				
				if(grid.isSet(row + i, col))		
					return false;
			}
			
			for (int i = 0; i <= Math.abs(colMoves); i++){
				
				if(grid.isSet(r2, col - i))
					return false;
			}
		}
		
		// third: left then up		
		if (r2 <= row && c2 <= col) {

			for (int i = 0; i <= Math.abs(colMoves); i++){
				
				if(grid.isSet(row, col - i))
					return false;
			}
			
			for (int i = 0; i <= Math.abs(rowMoves); i++){
				
				if(grid.isSet(row - i, c2))		
					return false;
			}
		}
		
		// fourth: up then right		
		if (c2 >= col && r2 <= row) {
			
			for (int i = 0; i <= Math.abs(rowMoves); i++){
				
				if(grid.isSet(row - i, col))		
					return false;
			}
			
			for (int i = 0; i <= Math.abs(colMoves); i++){
				
				if(grid.isSet(r2, col + i))
					return false;
			}
		}
		
		// if all checks return nothing
		// that means the path is clear
		return true;
	}

	/**
	 * moves this square in the given direction if possible.
	 * 
	 * The square will not move if the direction is blocked, or if the square is
	 * unable to move.
	 * 
	 * If it attempts to move DOWN and it can't, the square is frozen and cannot
	 * move anymore
	 * 
	 * @param direction
	 *            the direction to move
	 */
	public void move(Direction direction) {
		if (canMove(direction)) {
			switch (direction) {
			
			case DOWN:
				row ++;
				break;
			
			case LEFT:
				col --;
				break;
				
			case RIGHT:
				col ++;
				break;

			}
		}
	}
	
	/**
	 * Rotates the given square 
	 * around the center square
	 * 
	 * @param c - center square
	 */
	public void rotate(Square c){
		
		if (canRotate(c)){
			
			int r2 = c.row + (col - c.col);
			int c2 = c.col + (c.row - row);
			
			row = r2;
			col = c2;
		}
		
	}

	/**
	 * Changes the color of this square
	 * 
	 * @param c
	 *            the new color
	 */
	public void setColor(Color c) {
		color = c;
	}

	/**
	 * Gets the color of this square
	 */
	public Color getColor() {
		return color;	
	}

	/**
	 * Draws this square on the given graphics context
	 */
	public void draw(Graphics g) {

		// calculate the upper left (x,y) coordinate of this square
		int actualX = Grid.LEFT + col * WIDTH;
		int actualY = Grid.TOP + row * HEIGHT;
		g.setColor(color);
		g.fillRect(actualX, actualY, WIDTH, HEIGHT);
		// black border (if not empty)
		if (!color.equals(Grid.EMPTY)) {
			g.setColor(Color.BLACK);
			g.drawRect(actualX, actualY, WIDTH, HEIGHT);
		}
	}
}

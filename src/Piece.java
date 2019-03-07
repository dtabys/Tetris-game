import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

/**
 * Interface for piece
 * 
 * @author dtabys
 *
 */

public interface Piece {
	void draw(Graphics g);
	void move(Direction direction);
	Point[] getLocations();
	Color getColor();
	boolean canMove(Direction direction);
	boolean canRotate();
	void rotate();
}

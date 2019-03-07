/**
 * Handles events for the Tetris Game.  User events (key strokes) as well as periodic timer
 * events.
 * 
 * @author dtabys
 */
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JFrame;
import javax.swing.Timer;

public class EventController extends KeyAdapter implements ActionListener {

	private Game game; // current game: grid and current piece
	private Timer timer;

	private static final double PIECE_MOVE_TIME = 0.8; // wait 0.8 s every time
														// the piece moves down
														// increase to slow it
														// down

	private boolean gameOver;

	/**
	 * Creates an EventController to handle key and timer events.
	 * 
	 * @param game
	 *            the game this is controlling
	 */
	public EventController(Game game) {
		this.game = game;
		gameOver = false;
		double delay = 1000 * PIECE_MOVE_TIME; // in milliseconds
		timer = new Timer((int) delay, this);
		timer.setCoalesce(true); // if multiple events pending, bunch them to
		// 1 event
		timer.start();
	}

	/**
	 * Responds to special keys being pressed.
	 * 
	 * Currently just responds to the space key and the q(uit) key
	 */
	public void keyPressed(KeyEvent e) {
		// if 'Q', quit the game
		if (e.getKeyCode() == KeyEvent.VK_Q) {
			timer.stop();
			((JFrame) e.getSource()).dispose();
		}
		if (!gameOver) {
			switch (e.getKeyCode()) {
			// if the user presses Space or Down Arrow
			// the block will go down instantaneously
			case KeyEvent.VK_SPACE:
				handleMove(Direction.DROP);
				break;
			
			// if the user presses Down Arrow the block will rotate
			case KeyEvent.VK_DOWN:
				handleMove(Direction.ROTATE);
				break;
				
			// if the user presses Left Arrow the block will move one square to the left
			case KeyEvent.VK_LEFT:
				handleMove(Direction.LEFT);
				break;
			
			// if the user presses Right Arrow the block will move one square to the right
			case KeyEvent.VK_RIGHT:
				handleMove(Direction.RIGHT);
				break;
			}
		}
	}

	/** Updates the game periodically based on a timer event */
	public void actionPerformed(ActionEvent e) {
		handleMove(Direction.DOWN);
	}

	/**
	 * Update the game by moving in the given direction
	 */
	private void handleMove(Direction direction) {
		if (direction == Direction.ROTATE){
			game.rotatePiece();
		} else {
			game.movePiece(direction);
		}
		gameOver = game.isGameOver();
		if (gameOver)
			timer.stop();
	}
}

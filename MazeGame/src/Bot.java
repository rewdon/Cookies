
/*
Bot.java

Mike Barnes
8/15/2011
 */

import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Bot is an abstract class for any "player" or "robot" subclass that will be
 * controlled by a path-finding and/or moving algorithm().
 */

public abstract class Bot {

	/** bot's current position */
	protected Point currentPosition;
	/** bot's initial position */
	protected Point initialPosition;
	/** all the places bot has been */
	protected ArrayList<Point> path;
	/** bot's color */
	protected Color color;
	/** bot's name */
	protected String name;

	/**
	 * Make a Bot
	 * 
	 * @param label
	 *            Window title
	 * @param x
	 *            initial horizontal screen position
	 * @param y
	 *            initial vertical screen position;
	 * @param colorValue
	 *            bot's color
	 */

	public Bot(String label, int x, int y, Color colorValue) {
		name = new String(label);
		initialPosition = new Point(x, y);
		color = colorValue;
		reset();
	}

	/**
	 * Make a Bot
	 * 
	 * @param label
	 *            Window title
	 * @param pt
	 *            Point position of Bot
	 * @param colorValue
	 *            bot's color
	 */

	public Bot(String label, Point pt, Color colorValue) {
		name = new String(label);
		initialPosition = pt;
		color = colorValue;
		reset();
	}

	/**
	 * @return the Bot's point location.
	 */
	public Point getCurrentPosition() {
		return currentPosition;
	}

	/**
	 * Restore bot to initial simulation model. Allows algorithm to be simulated
	 * (run) again with same model (setup).
	 */

	public void reset() {
		currentPosition = new Point(initialPosition);
		path = new ArrayList<Point>();
		path.add(new Point(currentPosition)); // set starting point
	}

	/**
	 * Take the next step in the bot's path.
	 * 
	 * @param xPos
	 *            horizontal screen position to move to
	 * @param yPos
	 *            vertical screen position to move to
	 */

	public void moveTo(int xPos, int yPos) {
		currentPosition.setLocation(xPos, yPos);
		// add next path segment
		path.add(new Point(currentPosition));
	}

	/**
	 * Take the next step in the bot's path.
	 * 
	 * @param pt
	 *            is point to move to
	 */

	public void moveTo(Point pt) {
		currentPosition.setLocation(pt);
		// add next path segment
		path.add(new Point(currentPosition));
	}

	/**
	 * Draw the bot for the current simulation step.
	 */

	public void draw(Graphics g) {
		Color tColor;
		tColor = g.getColor(); // save existing color
		g.setColor(color);
		// draw bot's path from start to current location
		Iterator<Point> position = path.iterator();
		Point pt;
		int x1, y1, x2, y2;
		pt = position.next();
		x1 = (int) pt.getX();
		y1 = (int) pt.getY();
		while (position.hasNext()) {
			pt = position.next();
			x2 = (int) pt.getX();
			y2 = (int) pt.getY();
			g.drawLine(x1, y1, x2, y2);
			x1 = x2;
			y1 = y2;
		}
		// draw bot
		g.fillOval(x1 - 3, y1 - 3, 7, 7);
		g.setColor(tColor); // restore previous color
	}

	/**
	 * Behaviors (actions) to simulate on each step in the algorithm's
	 * simulation.
	 */

	public abstract void move();
}
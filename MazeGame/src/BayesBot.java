
/*
BayesBot.java

Mike Barnes
8/15/2011
 */

import java.awt.*;
import java.util.*;

/**
 * BayesBot is an example on how to extend Bot for a "player". BayesBots move in
 * a "random walk" or "Bayesian" manner. That is, the next position is randomly
 * selected from 8 possible directions of: up, upLeft, left, downLeft, down,
 * downRight, right, or upRight.
 * <p>
 * BayesBot UML class diagram
 * <p>
 * <Img align left src="../UML/BayesBot.png">
 * 
 * @since 8/15/2011
 * @version 1.0
 * @author G. M. Barnes
 */

public class BayesBot extends Bot {
	/** for random simulation events sample from aRandom */
	private Random aRandom;
	/** BayesBots do not return to their last position */
	private int lastX, lastY;

	/**
	 * Make a BayesBot
	 * 
	 * @param name
	 *            give the bot a name
	 * @param x
	 *            bot's initial horizontal vertical position
	 * @param y
	 *            bot's initial vertical screen position
	 * @param colorValue
	 *            bot's color
	 */

	public BayesBot(String name, int x, int y, Color colorValue) {
		super(name, x, y, colorValue);
		aRandom = new Random();
		lastX = x;
		lastY = y;
	}

	/**
	 * Restore simulation model's initial state
	 */

	public void reset() {
		super.reset();
		lastX = (int) currentPosition.getX();
		lastY = (int) currentPosition.getY();
	}

	/**
	 * BayesBots move in a random direction on each step, except they do not
	 * move to their previous position. BayesBots can move: up, upRight, right,
	 * downRight, down, downLeft, left, and upLeft.
	 */

	public void move() {
		int newX, newY;
		do {
			newX = lastX + (aRandom.nextInt(3) - 1) * 5;
			newY = lastY + (aRandom.nextInt(3) - 1) * 5;
		} while (newX == lastX && newY == lastY);
		moveTo(newX, newY);
		lastX = newX;
		lastY = newY;
	}

}
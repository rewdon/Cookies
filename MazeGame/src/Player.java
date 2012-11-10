/*
 Oliver Barreto
 */

import java.awt.Color;
import java.awt.Point;

public class Player extends Bot {
	private int strength;
	
	public Player(String name, int x, int y, Color color) {
		super(name, x, y, color);
		currentPosition = new Point(x, y);
		strength = 500;
	}
	
	public void moveTo(int x, int y) {
		super.moveTo(x, y);
	}
	
	public void setStrength(int strength) {
		this.strength = strength;
	}
	
	public int getStrength() {
		return strength;
	}

	@Override
	public void move() {
		
	}
}

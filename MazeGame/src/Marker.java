
/*
Marker.java

Mike Barnes
8/15/2011
 */


import java.awt.*;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Markers are used to represent position on the canvas that Bot's can use for
 * path finding. In algorithm simulation Markers can be either temporary or
 * permanent. Permanent markers are only cleared when the simulation's "Reset"
 * option is selected. Temporary markers are cleared when the simulation's
 * "Clear" option is selected.
 * 
 * Marker's size has a default value of 2. Can be changed with int getSize() and
 * void setSize(int size).
 * 
 * <p>
 * Marker UML class diagram
 * <p>
 * <Img align left src="../UML/Marker.png">
 * 
 * @since 8/17/2012
 * @version 2.1
 * @author G. M. Barnes
 */

public class Marker implements Drawable {

	/** marker's position */
	private Point point;
	/** marker's color */
	private Color color;
	/** marker's size */
	private int size;
	/** marker's label */
	private String label;
	private boolean drawLabel;

	/**
	 * Make a Marker
	 * 
	 * @param x
	 *            marker's horizontal screen position
	 * @param y
	 *            marker's vertical screen position
	 * @param colorValue
	 *            marker's color
	 */
	public Marker(int x, int y, Color colorValue) {
		point = new Point(x, y);
		color = colorValue;
		size = 2;
		drawLabel = false;
	}

	/**
	 * Make a Marker
	 * 
	 * @param x
	 *            marker's horizontal screen position
	 * @param y
	 *            marker's vertical screen position
	 * @param colorValue
	 *            marker's color
	 * @param ptSize
	 *            marker's size
	 */
	public Marker(int x, int y, Color colorValue, int ptSize) {
		point = new Point(x, y);
		color = colorValue;
		size = ptSize;
		drawLabel = false;
	}

	/**
	 * Make a Marker
	 * 
	 * @param pt
	 *            marker's horizontal screen position
	 * @param colorValue
	 *            marker's color
	 */
	public Marker(Point pt, Color colorValue) {
		point = pt;
		color = colorValue;
		size = 2;
		drawLabel = false;
	}

	/**
	 * Make a Marker
	 * 
	 * @param pt
	 *            marker's horizontal screen position
	 * @param colorValue
	 *            marker's color
	 * @param ptSize
	 *            marker's size
	 */
	public Marker(Point pt, Color colorValue, int ptSize) {
		point = pt;
		color = colorValue;
		size = ptSize;
		drawLabel = false;
	}

	/**
	 * Make a Marker
	 * 
	 * @param m
	 *            Marker to copy values for new Marker from
	 */
	public Marker(Marker m) {
		point = m.getPoint();
		color = m.getColor();
		size = m.getSize();
		drawLabel = false;
	}

	/** @return the marker's color */
	public Color getColor() {
		return color;
	}

	/** @return the marker's label */
	public String getLabel() {
		return label;
	}

	/** @return the marker's position */
	public Point getPoint() {
		return point;
	}

	/** @return the marker's size */
	public int getSize() {
		return size;
	}

	/** change marker's color */
	public void setColor(Color c) {
		color = c;
	}

	/** change marker's label */
	public void setLabel(String l) {
		drawLabel = true;
		label = l;
	}

	/** change marker's size */
	public void setSize(int s) {
		size = s;
	}

	/** Draw the marker on the simulation's canvas */
	public void draw(Graphics g) {
		Color tColor;
		tColor = g.getColor(); // save exisiting color
		g.setColor(color);
		g.fillRect((int) point.getX() - size, (int) point.getY() - size,
				2 * size, 2 * size);
		g.setColor(tColor); // restore previous color
		if (drawLabel)
			g.drawString(label, (int) point.getX() - 2 * size,
					(int) point.getY() - 2 * size);
	}

}
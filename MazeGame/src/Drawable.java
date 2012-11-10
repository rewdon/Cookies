
/*
Drawable.java

Mike Barnes
8/15/2011
 */

import java.awt.*;

/**
 * Drawable interfaces must implement public Draw(Graphics G). In the Simulation
 * Framework Markers, Connectors, and Bots are Drawable. Interface type allows
 * all drawables to be held in same collection.
 */

public interface Drawable {
	public void draw(Graphics g);
}
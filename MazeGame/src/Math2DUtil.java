/*
 Oliver Barreto
 */

public class Math2DUtil {
	static public double calculateDistance(Waypoint a, Waypoint b) {
		return Math.sqrt(
			(a.getX() - b.getX()) * (a.getX() - b.getX()) +
			(a.getY() - b.getY()) * (a.getY() - b.getY()) + 
			(a.getHeight() - b.getHeight()) * (a.getHeight() - b.getHeight())
		);
	}
}

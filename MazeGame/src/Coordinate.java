/*
 Oliver Barreto
 */

public class Coordinate implements Comparable<Coordinate> {
	public int x;
	public int y;
	
	public Coordinate(int x, int y) {
		this.x = x;
		this.y = y;
	}

	@Override
	public int compareTo(Coordinate o) {
		if (x < o.x) {
			return -1;
		} else if (x > o.x) {
			return 1;
		} else {
			if (y < o.y) {
				return -1;
			} else if (y > o.y) {
				return 1;
			} else {
				return 0;
			}
		}
	}
	
	
}


public class Edge implements Comparable<Edge>{
	public Coordinate a;
	public Coordinate b;
	
	public Edge(Coordinate a, Coordinate b) {
		this.a = a;
		this.b = b;
	}

	@Override
	public int compareTo(Edge rhs) {
		if (a.compareTo(rhs.a) < 0) {
			return -1;
		} else if (a.compareTo(rhs.a) > 0) {
			return 1;
		} else {
			if (b.compareTo(rhs.b) < 0) {
				return -1;
			} else if (b.compareTo(rhs.b) > 0) {
				return 1;
			} else {
				return 0;
			}
		}
	}
}

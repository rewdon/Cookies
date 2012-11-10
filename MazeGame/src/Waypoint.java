/*
 Oliver Barreto
 */

import java.util.ArrayList;


public class Waypoint implements Comparable<Waypoint> {
	private int x;
	private int y;
	private int height;
	private int cost;
	private int gold;
	private int mapX;
	private int mapY;
	private int noNeighbors;

	private double distanceFromSource;
	private double distanceToGoal;
	private double distance;
	private Waypoint parent;
	private ArrayList<Coordinate> neighborCoordinates;

	public Waypoint(int x, int y, int height, int cost, int gold, int mapX,
			int mapY) {
		neighborCoordinates = new ArrayList<Coordinate>();
		this.x = x;
		this.y = y;
		this.height = height;
		this.cost = cost;
		this.gold = gold;
		this.mapX = mapX;
		this.mapY = mapY;
	}

	public Waypoint() {
		this.x = 0;
		this.y = 0;
		this.height = 0;
		this.cost = 0;
		this.gold = 0;
		this.mapX = 0;
		this.mapY = 0;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCost() {
		return cost;
	}

	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getGold() {
		return gold;
	}

	public void setGold(int gold) {
		this.gold = gold;
	}

	public int getMapX() {
		return mapX;
	}

	public void setMapX(int mapX) {
		this.mapX = mapX;
	}

	public int getMapY() {
		return mapY;
	}

	public void setMapY(int mapY) {
		this.mapY = mapY;
	}

	static public int hashKey(int x, int y) {
		return (512 * x) + y;
	}

	public double getDistanceFromSource() {
		return distanceFromSource;
	}

	public void setDistanceFromSource(double distanceFromSource) {
		this.distanceFromSource = distanceFromSource;
	}

	public double getDistanceToGoal() {
		return distanceToGoal;
	}

	public void setDistanceToGoal(double distanceToGoal) {
		this.distanceToGoal = distanceToGoal;
	}

	public double getDistance() {
		return distance;
	}

	public void setDistance(double distance) {
		this.distance = distance;
	}
	
	public void setParent(Waypoint p) {
		parent = p;
	}
	
	public Waypoint getParent() {
		return parent;
	}
	
	public boolean isSameLocation(Waypoint p) {
		return (getX() == p.getX() && getY() == p.getY());
	}
	
	public int compareTo(Waypoint rhs) {
		if (distance < rhs.distance) {
			return -1;
		} else if (distance > rhs.distance) {
			return 1;
		} else {
			return 0;
		}
	}
	
	public void addNeighborCoordinate(int x, int y) {
		neighborCoordinates.add(new Coordinate(x, y));
	}
	
	public ArrayList<Coordinate> getNeighborCoordinates() {
		return neighborCoordinates;
	}
	
	@Override
	public String toString() {
		return "(" + x + ", " + y + ")";
	}
}

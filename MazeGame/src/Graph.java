/*
 Oliver Barreto
 */

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.PriorityQueue;

public class Graph {
	/*
	 * Map info
	 */
	private HashMap<Integer, Waypoint> mapInfo;

	/**
	 * Constructor
	 * 
	 * @param mapInfo
	 */
	public Graph(HashMap<Integer, Waypoint> mapInfo) {
		this.mapInfo = mapInfo;
	}

	public double computeDistanceFromSource(Waypoint a, Waypoint b) {
		int diff = Math.abs(a.getX() - b.getX())
				+ Math.abs(a.getY() - b.getY());
		return (diff == 2) ? (20.0 * Math.sqrt(2)) : 20.0;
	}

	public double computeHeuristicDistanceToGoal(Waypoint n, Waypoint goal) {
		return 20.0 * (Math.abs(n.getX() - goal.getX()) + Math.abs(n.getY()
				- goal.getY()));
	}

	public ArrayList<Waypoint> getNeighborsOf(Waypoint a) {
		ArrayList<Waypoint> neighbors = new ArrayList<Waypoint>();
		ArrayList<Coordinate> neighborCoordinates = a.getNeighborCoordinates();
		if (neighborCoordinates.size() != 0) {
			for (int i = 0; i < neighborCoordinates.size(); ++i) {
				// check if hashmap has that coordinate
				if (mapInfo.containsKey(
						Waypoint.hashKey(neighborCoordinates.get(i).x, neighborCoordinates.get(i).y))) {
					
					// add them into neighbors
					neighbors.add(
						mapInfo.get(
							Waypoint.hashKey(
									neighborCoordinates.get(i).x, 
									neighborCoordinates.get(i).y)));
				}
			}
		}
		return neighbors;
	}

	public void resetData(HashMap<Integer, Waypoint> copy) {
		mapInfo = new HashMap<Integer, Waypoint>();
		for (Integer key : copy.keySet()) {
			Waypoint value = copy.get(key);
			Waypoint copyOfValue = new Waypoint(
					value.getX(), value.getY(), value.getHeight(), value.getCost(), 
					value.getGold(), value.getMapX(), value.getMapY());
			
			for (Coordinate c : value.getNeighborCoordinates()) {
				copyOfValue.addNeighborCoordinate(c.x, c.y);
			}
			mapInfo.put(key, copyOfValue);
		}
	}
	
	/**
	 * Whatever here
	 * 				
	 * @param start
	 * 				what is this?
	 * @param goal
	 * 				what is this?
	 * @return
	 * 
	 * @link http://www.policyalmanac.org/games/aStarTutorial.htm
	 */
	public ArrayList<Waypoint> astarCompute(Waypoint start, Waypoint goal) {
		ArrayList<Waypoint> path = new ArrayList<Waypoint>();
		PriorityQueue<Waypoint> openedSet = new PriorityQueue<Waypoint>();
		HashSet<Integer> openedSetHelper = new HashSet<Integer>();
		HashSet<Integer> closedSet = new HashSet<Integer>();

		start.setDistance(0.0);
		start.setDistanceFromSource(0.0);
		start.setDistanceToGoal(0.0);

		goal.setDistance(0.0);
		goal.setDistanceFromSource(0.0);
		goal.setDistanceToGoal(0.0);

		openedSet.add(start);
		openedSetHelper.add(Waypoint.hashKey(start.getX(), start.getY()));

		while (!openedSet.isEmpty()) {
			Waypoint current = openedSet.poll();
			openedSetHelper.remove(Waypoint.hashKey(current.getX(),
					current.getY()));
			closedSet.add(Waypoint.hashKey(current.getX(), current.getY()));

			// we're done!
			if (current.isSameLocation(goal)) {
				while (current.getParent() != null) {
					path.add(current);
					current = current.getParent();
				}

				// reverse the path
				ArrayList<Waypoint> temp = new ArrayList<Waypoint>();
				for (int i = 0; i < path.size(); ++i) {
					temp.add(path.get(path.size() - i - 1));
				}
				
				path = temp;
				path.add(goal);
				return path;

			} else {
				ArrayList<Waypoint> neighbors = getNeighborsOf(current);
				for (Waypoint n : neighbors) {
					if (closedSet.contains(Waypoint.hashKey(n.getX(), n.getY()))) { 
						continue;
					} else {
						if (!openedSet.contains(Waypoint.hashKey(n.getX(),n.getY()))) {
							n.setParent(current);
							// record new F, G, H
							n.setDistanceFromSource(current.getDistanceFromSource() + computeDistanceFromSource(current, n));
							n.setDistanceToGoal(computeHeuristicDistanceToGoal(n, goal));
							n.setDistance(n.getDistanceFromSource() + n.getDistanceToGoal());
							openedSet.add(n);
						} else { // it's already on the OPEN SET
							double costFromThisPathToN = current.getDistanceFromSource() + computeDistanceFromSource(current, n);
							// we have a better path, going from "current node"
							if (costFromThisPathToN < n.getDistanceFromSource()) {
								n.setParent(current);
								n.setDistanceFromSource(costFromThisPathToN);
								n.setDistance(n.getDistanceFromSource() + n.getDistanceToGoal());
							}
						}
					}
				}

			}
		}
		return path;
	}
}

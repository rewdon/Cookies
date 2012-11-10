/*
 Oliver Barreto
 */

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * @author Oliver Barreto
 */

public class Game extends SimFrame {
	// eliminate warning @ serialVersionUID
	private static final long serialVersionUID = 42L;

	// constants for this demonstration
	private final int min = 20;
	private final int max = 520;

	// GUI components for application's menu
	private Game app;

	// application variables;
	private ArrayList<Bot> bot;

	// count of moves to make before stopping
	private int moves;

	// waypoints
	private HashMap<Integer, Waypoint> hashMap;

	// all treasures
	private ArrayList<Waypoint> treasures;

	// graph util
	private Graph graph;

	// player
	private Player player;
	private Player friend;
	
	enum PathState {
		Goal,
		Treasure,
	}
	
	private PathState pathState;

	public static void main(String[] args) {
		Game app = new Game("app", "terrain282.png");
		try {
			app.loadWayPointFromFiles("waypointNeighborP2.txt");
		} catch (FileNotFoundException e) {
			System.out.println("File not found, bro!");
		}
		app.start(); // start is inherited from SimFrame
	}
	

	/**
	 * x, y, height, cost, gold, mapX, mapY
	 * 
	 * @param fileName
	 * @throws FileNotFoundException
	 */
	public void loadWayPointFromFiles(String fileName)
			throws FileNotFoundException {
		hashMap = new HashMap<Integer, Waypoint>();
		treasures = new ArrayList<Waypoint>();

		File fin = new File(fileName);
		Scanner scanner = new Scanner(fin);
		int x = 0;
		int y = 0;
		int height = 0;
		int cost = 0;
		int gold = 0;
		int mapX = 0;
		int mapY = 0;
		int noNeighbors = 0;
		int nX = 0;
		int nY = 0;
		while (scanner.hasNext()) {
			x = scanner.nextInt();
			y = scanner.nextInt();
			height = scanner.nextInt();
			cost = scanner.nextInt();
			gold = scanner.nextInt();
			mapX = scanner.nextInt();
			mapY = scanner.nextInt();
			// read all neighbors
			noNeighbors = scanner.nextInt();

			Waypoint wp = new Waypoint(x, y, height, cost, gold, mapX, mapY);
			for (int i = 0; i < noNeighbors; ++i) {
				nX = scanner.nextInt();
				nY = scanner.nextInt();
				wp.addNeighborCoordinate(nX, nY);
				
			}
			hashMap.put(new Integer(Waypoint.hashKey(x, y)), wp);
			if (gold != 0) {
				treasures.add(wp);
			}
		}
		scanner.close();
	}

	/**
	 * Make the application: create the MenuBar, "help" dialogs,
	 */
	public Game(String frameTitle, String imageFile) {
		super(frameTitle, imageFile);
		buildGUI();
		player = new Player("one", 20, 20, Color.green);
	}

	private void buildGUI() {
		// create menus
		JMenuBar menuBar = new JMenuBar();
		// set About and Usage menu items and listeners.
		aboutMenu = new JMenu("About");
		aboutMenu.setMnemonic('A');
		aboutMenu.setToolTipText("Display information about this program");
		// create a menu item and the dialog it invoke
		usageItem = new JMenuItem("usage");
		authorItem = new JMenuItem("author");
		usageItem.addActionListener( // anonymous inner class event handler
				new ActionListener() {
					public void actionPerformed(ActionEvent event) {
						JOptionPane
								.showMessageDialog(
										Game.this,
										"Simulation creates 1 bot that moves in a A* walk. \n"
												+ "Each \"turn\" a bot is randomly selected to move. \n"
												+ "Speed of the simulation \"turns\" is set by the slider. \n"
												+ "User prompts are displayed in the status line (bottom).\n"
												+ "1.  Use mouse click to set initial positions for 3 bots\n"
												+ "2.  \"Start\" button will begin simulation. \n"
												+ "3.  \"Stop\" button will halt (pause) simulation. \n"
												+ "4.  \"Clear\" button will erase current simulation turns \n"
												+ "    and restore initial simulation bot positions.\n"
												+ "5.  \"Reset\" button will erase all simulation values \n"
												+ "    and enable use to start a new simulation.\n",
										"Usage", JOptionPane.PLAIN_MESSAGE);
					}
				});
		// create a menu item and the dialog it invokes
		authorItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				JOptionPane.showMessageDialog(Game.this, "Oliver Barreto\n"
						+ "California State University Northridge \n"
						+ "oliver.barreto.147@csun.edu \n" , "Author",
						JOptionPane.INFORMATION_MESSAGE, new ImageIcon(
								"renzo.png"));
			}
		});
		// add menu items to menu
		aboutMenu.add(usageItem); // add menu item to menu
		aboutMenu.add(authorItem);
		menuBar.add(aboutMenu);
		setJMenuBar(menuBar);
		validate(); // resize layout managers
		// construct the application specific variables
	}

	/**
	 * Create BayesBots, add them to application's ArrayList and add them to
	 * AnimatePanel's ArrayList.
	 * 
	 * @param name
	 *            of the bot
	 * @param x
	 *            initial horizontal position of the bot
	 * @param y
	 *            initial vertical position of the bot
	 * @param color
	 *            of the bot
	 */

	/**
	 * Set up the actors (Bots), wayPoints (Markers), and possible traveral
	 * paths (Connectors) for the simulation.
	 */
	public void setSimModel() {
		setStatus("Initial state of simulation");
		HashSet<Edge> edges = new HashSet<Edge>();
		
		// add 81 permanent markers
		for (int x = min; x <= max; x += min) {
			for (int y = min; y <= max; y += min) {
				if (hashMap.containsKey(Waypoint.hashKey(x, y))) {
					Waypoint p = hashMap.get(Waypoint.hashKey(x, y));
					if (p.getGold() != 0)
						animatePanel.addPermanentDrawable(new Marker(x, y,
								Color.yellow, 4));
					else if (p.getCost() != 0)
						animatePanel.addPermanentDrawable(new Marker(x, y,
								Color.cyan, 3));
					else if (p.getMapX() != 0 || p.getMapY() != 0)
						animatePanel.addPermanentDrawable(new Marker(x, y,
								Color.magenta, 2));
					else
						animatePanel.addPermanentDrawable(new Marker(x, y,
								Color.black));
					
					ArrayList<Coordinate> neighbors = hashMap.get(Waypoint.hashKey(x, y)).getNeighborCoordinates();
					for (Coordinate c : neighbors) {
						Edge forward = new Edge(new Coordinate(x, y), new Coordinate(c.x, c.y));
						Edge backward = new Edge(new Coordinate(c.x, c.y), new Coordinate(x, y));
						if (!edges.isEmpty()) {
							if (!edges.contains(forward) && !edges.contains(backward)) {
								edges.add(forward);
								edges.add(backward);
								animatePanel.addPermanentDrawable(new Connector(x, y, c.x, c.y, Color.black));
							}
						} else {
							edges.add(forward);
							edges.add(backward);
							animatePanel.addPermanentDrawable(new Connector(x, y, c.x, c.y, Color.black));
						}
					}
				}
			}
		}
		animatePanel.repaint();

		// create Bot collection
		bot = new ArrayList<Bot>();

		/*
		 * Get start position from mouse
		 */
		setStatus("Enter start position");
		waitForMousePosition();
		Point start = estimateMousePosition((int) mousePosition.getX(),
				(int) mousePosition.getY());
		player = new Player("start", (int) start.getX(), (int) start.getY(),
				Color.green);
		animatePanel.addBot(player);

		/*
		 * Get end position from mouse
		 */
		setStatus("Enter end position");
		waitForMousePosition();
		Point end = estimateMousePosition((int) mousePosition.getX(),
				(int) mousePosition.getY());
		friend = new Player("Goal", (int) end.getX(), (int) end.getY(),
				Color.red);
		animatePanel.addBot(friend);

		// initialize moves
		moves = 0;
		graph = new Graph(hashMap);
	}

	private Point estimateMousePosition(int x, int y) {
		if (x % 20 > 10) {
			x = ((x / 20) + 1) * 20;
		} else {
			x = (x / 20) * 20;
		}
		if (y % 20 > 10) {
			y = ((y / 20) + 1) * 20;
		} else {
			y = (y / 20) * 20;
		}
		return new Point(x, y);
	}

	private void stupidAlgorithm() {
		pathState = PathState.Goal;
		graph.resetData(hashMap);
		ArrayList<Waypoint> currentFollowingPath = graph.astarCompute(hashMap
				.get(Waypoint.hashKey((int) player.getCurrentPosition().getX(),
						(int) player.getCurrentPosition().getY())), hashMap
				.get(Waypoint.hashKey((int) friend.getCurrentPosition().getX(),
						(int) friend.getCurrentPosition().getY())));

		int nextN = 0;
		for (int i = 0; i < currentFollowingPath.size(); ++i) {
			System.out.println(currentFollowingPath.get(i));
		}

		int x;
		int y;
		while (runnable()) {
			Waypoint current = currentFollowingPath.get(nextN);
			x = currentFollowingPath.get(nextN).getX();
			y = currentFollowingPath.get(nextN).getY();
			
			// player make a move
			player.moveTo(x, y);

			// retrieve information of current node
			Waypoint temp = hashMap.get(Waypoint.hashKey(x, y));
			graph.resetData(hashMap);
			
			// switch to goal path
			if (temp.getMapX() != 0 || temp.getMapY() != 0) {
				// find the closest treasure
				int idx = findClosestTreasure(current);
				if (idx != -1) {
					int tX = (int) treasures.get(idx).getX();
					int tY = (int) treasures.get(idx).getY();
					
					// remove this treasure
					treasures.remove(idx);
					
					graph.resetData(hashMap);
					currentFollowingPath = 	
						graph.astarCompute(
							hashMap.get(Waypoint.hashKey(x, y)), 
							hashMap.get(Waypoint.hashKey(tX, tY)));
					
					nextN = 0;
					pathState = PathState.Treasure;
				}
			}
			
			nextN++;
			if (nextN == currentFollowingPath.size()) {
				if (pathState == PathState.Goal) {
					return;
				} else {
					graph.resetData(hashMap);
					currentFollowingPath = graph.astarCompute(hashMap
							.get(Waypoint.hashKey((int) player.getCurrentPosition().getX(),
									(int) player.getCurrentPosition().getY())), hashMap
							.get(Waypoint.hashKey((int) friend.getCurrentPosition().getX(),
									(int) friend.getCurrentPosition().getY())));
					nextN = 0;
				}
			}

			// update state
			checkStateToWait();

		}
	}

	private double computeDistance(Waypoint a, Waypoint b) {
		return Math.sqrt((a.getX() - b.getX()) * (a.getX() - b.getX())
				+ (a.getY() - b.getY()) * (a.getY() - b.getY()));
	}

	/**
	 * Return the index of the closest treasure with 
	 * respect to current
	 * 
	 * @param current
	 * 			The current ...
	 * 
	 * @return
	 * 			index of ArrayList<Waypoint> treasures
	 */
	private int findClosestTreasure(Waypoint current) {
		if (treasures.size() >= 0) {
			double distance = computeDistance(current, treasures.get(0));
			int idx = 0;
			for (int i = 1; i < treasures.size(); ++i) {
				if (computeDistance(current, treasures.get(i)) < distance) {
					distance = computeDistance(current, treasures.get(i));
					idx = i;
				}
			}
			return idx;
		} else {
			return -1; // no more treasures
		}
	}

	/**
	 * The "algorithm" used is very simple. Move a randomly selected
	 * BayesianBot, 1/5 of time draw 2 temporary markers and their temporary
	 * connector
	 */
	public synchronized void simulateAlgorithm() {
		stupidAlgorithm();
	}

	private void updatePlayerStrength(Player player, Waypoint closet) {
		Waypoint current = hashMap.get(Waypoint.hashKey((int) player
				.getCurrentPosition().getX(), (int) player.getCurrentPosition()
				.getY()));

		// subtract first, talk later
		int cost = (int) Math2DUtil.calculateDistance(current, closet);
		player.setStrength(player.getStrength() - cost);

		// now other cases
		if (closet.getGold() != 0) {
			player.setStrength(player.getStrength() + closet.getGold());
		}
	}
}

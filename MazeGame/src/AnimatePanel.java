
/*
AnimatePanel.java

Mike Barnes
8/15/2011
 */


import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.event.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * AnimatePanel is the GUI component of the simulation framework. It uses a
 * BorderLayout manager to have canvas for visually animating the algorith in
 * the center and a Box container of control buttons in the South. The canvas is
 * where the Bots, Markers, and Connectors are drawn. The control buttons enable
 * the user to:
 * 
 * <ul type = disc>
 * <li>adjust the speed of the simulation
 * <li>stop and start the simulation
 * <li>clear the simulation's temporary markers and connectors
 * <li>reset the simulation model
 * </ul>
 * 
 * <p>
 * AnimatePanel UML class diagram
 * <p>
 * <Img align left src="../UML/AnimatePanel.png">
 * 
 * @since 8/15/2011
 * @version 2.0
 * @author G. M. Barnes
 */

public class AnimatePanel extends JPanel {
	// eliminate warnings @ serialVersionUID
	private static final long serialVersionUID = 42L;
	/** reference to simulation's main class */
	private SimFrame appFrame;
	/** drawing area */
	private JLabel canvas;
	/** control the speed of the simulation's animation */
	private JSlider slider;
	/** hold the "state control" buttons */
	private Box box;
	/** start the simulation running with current model */
	public JButton start;
	/** stop the simulation running with current model */
	public JButton stop;
	/**
	 * clear the temporary markers and connectors from the current model, set
	 * all bots to their initial model state. "Start" will re-run the current
	 * simulation model.
	 */
	public JButton clear;
	/**
	 * remove all bots, markers, and connectors from the model, enable "Start"
	 * to setup a new simulation model
	 */
	public JButton reset;
	/** hold all the bots */
	private ArrayList<Bot> bot;
	/** hold all temporary Connectors and Markers */
	private ArrayList<Drawable> temporaryDrawables;
	/** hold all permanent Connectors and Markers */
	private ArrayList<Drawable> permanentDrawables;
	// animation delay values -- mostly constants
	/** default animation speed 1 fps */
	private final int delayInitial = 1000;
	/** fastest animation speed 25 fps */
	private final int delayMin = 40;
	/** slowest animation speed 1 frame every 5 seconds */
	private final int delayMax = 5000;
	/** increment "ticks" between slider values */
	private final int delayStep = 500;
	/** current milliseconds to "sleep" between animation frames */
	private int delayInterval = delayInitial;

	/**
	 * Make an AnimatePanel
	 * 
	 * @param parent
	 *            reference to simulation's main class
	 * @param imageFile
	 *            picture to use as canvas's background -- usually a terrain
	 */

	public AnimatePanel(SimFrame parent, String imageFile) {
		appFrame = parent;
		// create GUI components
		setLayout(new BorderLayout());
		canvas = new JLabel(new ImageIcon(imageFile));
		canvas.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent event) {
				appFrame.setPoint(event.getPoint());
			}
		});
		box = new Box(BoxLayout.X_AXIS);
		start = new JButton("Start");
		start.setToolTipText("start animation delay");
		start.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				setComponentState(false, true, true, false, false);
				appFrame.setStatus("simulation running:  animation delay is "
						+ delayInterval + " msec.");
				appFrame.setSimRunning(true);
				appFrame.stopWait();
			}
		});
		stop = new JButton("Stop");
		stop.setToolTipText("stop animation delay");
		stop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				appFrame.setSimRunning(false);
				setComponentState(true, false, false, true, true);
				appFrame.setStatus("simulation stopped running");
			}
		});
		clear = new JButton("Clear");
		clear.setToolTipText("clear to start state");
		clear.addActionListener(new ActionListener() {
			// clear simulation
			public void actionPerformed(ActionEvent event) {
				// delete temporary markers and connectors, and bots
				clearTemporaryDrawables(); // temporaryDrawables.clear();
				Bot b;
				Iterator<Bot> botIterator = bot.iterator();
				while (botIterator.hasNext()) {
					b = botIterator.next();
					b.reset();
				}
				initializeDelayValues();
				setComponentState(true, false, false, false, true);
				appFrame.setStatus("simulation clear to start state");
				repaint(); // update display
			}
		});
		reset = new JButton("Reset");
		reset.setToolTipText("reset to beginning state");
		reset.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				clearTemporaryDrawables(); // delete all temporary drawables
				permanentDrawables.clear(); // delete all permanent drawables
				initializeDelayValues();
				bot.clear();
				setComponentState(false, false, false, false, false);
				appFrame.setModelValid(false);
				appFrame.setStatus("restart simulation from initial state");
				repaint(); // update display
				// notify waiting start simulation thread
				appFrame.stopWait();
			}
		});
		slider = new JSlider(new DefaultBoundedRangeModel(delayInitial,
				delayStep, delayMin, delayMax));
		slider.setToolTipText("slider sets the animation delay between events");
		slider.setMaximumSize(new Dimension(200, 50));
		// set animation delay when slider is changed
		slider.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				DefaultBoundedRangeModel sliderModel = (DefaultBoundedRangeModel) slider
						.getModel();
				delayInterval = sliderModel.getValue();
				appFrame.setStatus("simulation running:  animation delay is "
						+ delayInterval + " msec.");
			}
		});
		// add components to box
		box.add(start);
		box.add(Box.createHorizontalGlue());
		box.add(slider);
		box.add(stop);
		box.add(clear);
		box.add(reset);
		setComponentState(false, false, false, false, false);
		// add components to panel
		add(canvas, BorderLayout.CENTER);
		add(box, BorderLayout.SOUTH);
		validate();
		// create non-GUI variables
		bot = new ArrayList<Bot>();
		temporaryDrawables = new ArrayList<Drawable>();
		permanentDrawables = new ArrayList<Drawable>();
	}

	/** Restore the slider values to initial condition */
	public void initializeDelayValues() {
		delayInterval = delayInitial;
		slider.setValue(delayInterval);
	}

	/** Enable / disable "state control" buttons -- show current state */
	public void setComponentState(boolean startState, boolean sliderState,
			boolean stopState, boolean clearState, boolean resetState) {
		start.setEnabled(startState);
		slider.setEnabled(sliderState);
		stop.setEnabled(stopState);
		clear.setEnabled(clearState);
		reset.setEnabled(resetState);
	}

	/**
	 * Store new Bot in collection
	 * 
	 * @param b
	 *            new bot to add to simulation
	 */

	public void addBot(Bot b) {
		bot.add(b);
	}

	/**
	 * Store Markers or Connectors as permanent drawable objects. Permanent
	 * drawables only deleted with "reset" events.
	 * 
	 * @param d
	 *            Connector or Marker to be added to collection
	 */

	public void addPermanentDrawable(Drawable d) {
		permanentDrawables.add(d);
	}

	/**
	 * Store Markers or Connectors as temporary drawable objects. Temporary
	 * drawables are deleted with "clear" or "reset" events, and by
	 * clearTemporaryDrawables().
	 * 
	 * @param d
	 *            Connector or Marker to be added to collection
	 */

	public synchronized void addTemporaryDrawable(Drawable d) {
		temporaryDrawables.add(d);
	}

	/**
	 * Clear the display of all temporary drawable opjects.
	 */

	public synchronized void clearTemporaryDrawables() {
		System.out.println("clear\n");
		temporaryDrawables.clear();
	}

	/**
	 * @return millisecond delay value of animation speed
	 */

	public int getDelayInterval() {
		return delayInterval;
	}

	/**
	 * Displays Connectors and Markers from Drawable collection. Called from
	 * paint(Graphics g).
	 * 
	 * @param g
	 *            2D graphics context
	 * @param drawables
	 *            Connectors and Markers
	 */

	private void drawDrawables(Graphics g, ArrayList<Drawable> drawables) {
		Iterator<Drawable> iterator = drawables.iterator();
		Drawable d;
		while (iterator.hasNext()) {
			d = iterator.next();
			d.draw(g);
		}
	}

	/**
	 * Display the animation. Uses drawDrawables(Graphics g, ArrayList<Drawable>
	 * drawables) to display temporary and permanent Connectors and Markers.
	 * 
	 * @param g
	 *            2D graphics context
	 */

	public void paint(Graphics g) {
		super.paint(g);
		// draw permanents
		drawDrawables(g, permanentDrawables); // draw permanents
		drawDrawables(g, temporaryDrawables); // draw temporaries
		// draw all bots
		Bot b;
		Iterator<Bot> botIterator = bot.iterator();
		while (botIterator.hasNext()) {
			b = botIterator.next();
			b.draw(g);
		}
	}

}
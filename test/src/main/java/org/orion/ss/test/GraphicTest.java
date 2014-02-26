package org.orion.ss.test;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.orion.ss.model.impl.Game;
import org.orion.ss.service.GameService;
import org.orion.ss.test.components.CurrentPlayerLabel;
import org.orion.ss.test.components.GameLogPanel;
import org.orion.ss.test.components.PlayerPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphicTest implements Observer {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);

	private final static String VERSION = "0.1";

	/* GUI constants */
	public final static Rectangle WINDOW_BOUNDS = new Rectangle(0, 0, 1370, 500);
	public final static Rectangle TAB_BOUNDS = new Rectangle(0, 0, 1370, 400);
	public final static Rectangle INFO_DIALOG_BOUNDS = new Rectangle(100, 100, 400, 250);
	public final static int LOG_TEXT_AREA_HEIGHT = 300;
	public final static int LEFT_MARGIN = 10;
	public final static int RIGHT_MARGIN = 10;
	public final static int TOP_MARGIN = 10;
	public final static int BOTTOM_MARGIN = 10;
	public final static int LATERAL_SWING_MARGIN = 20;
	public final static int COLUMN_WIDTH = 120;
	public final static int COLUMN_WIDTH_LARGE = 160;
	public final static int COLUMN_WIDTH_XLARGE = 200;
	public final static int ROW_HEIGHT = 25;

	private Game game;
	private JFrame mainFrame;

	private GameService gameService;

	private boolean paused = false;

	/* GUI components */
	private GameLogPanel gameLogPanel;
	private CurrentPlayerLabel currentPlayerLabel;
	private JTabbedPane mainPane;
	private PlayerPanel playerPanel;

	public final static void main(String[] args) {
		GraphicTest test = new GraphicTest();
		logger.info("Starting test at " + new Date());
		test.prepareGame();
		test.mountGUI();
		test.start();
	}

	protected void start() {
		showInfoDialog("The Game is prepared.");
		gameService.startGame();
		while (!gameService.gameHasEnded()) {
			this.showInfoDialog("" + game.getCurrentPlayer());
			pause();
		}
		gameService.computeScore();
		playerPanel.dismiss();
	}

	protected void prepareGame() {
		game = new GameSample().buildGame();
		gameService = new GameService(game);
		game.getLog().addObserver(this);
	}

	protected void addPlayerPanel() {
		playerPanel = new PlayerPanel(this, game);
		mainPane.addTab("Current Player", playerPanel);
		mainPane.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				JTabbedPane source = (JTabbedPane) e.getSource();
				for (Component component : source.getComponents()) {
					if (component instanceof PlayerPanel) {
						component.setEnabled(false);
					}
				}
				Component component = source.getSelectedComponent();
				if (component instanceof PlayerPanel) {
					PlayerPanel panel = (PlayerPanel) source.getSelectedComponent();
					panel.setEnabled(true);
				}
			}

		});
		game.addObserver(playerPanel);
	}

	protected void mountGUI() {
		mainFrame = new JFrame("Graphic Tester v" + VERSION);
		mainFrame.setBounds(WINDOW_BOUNDS);
		mainFrame.getContentPane().setLayout(null);
		mainPane = new JTabbedPane();
		mainPane.setBounds(TAB_BOUNDS);
		mainFrame.getContentPane().add(mainPane);
		gameLogPanel = new GameLogPanel();
		mainPane.addTab("Game Log", gameLogPanel);
		currentPlayerLabel = new CurrentPlayerLabel();
		game.addObserver(currentPlayerLabel);
		mainFrame.getContentPane().add(currentPlayerLabel);
		mainFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				logger.info("Ending test at " + new Date());
				System.exit(0);
				super.windowClosing(e);
			}
		});
		addPlayerPanel();
		mainFrame.setVisible(true);
	}

	private void pause() {
		paused = true;
		while (paused) {
		}
	}

	private void resume() {
		paused = false;
	}

	public void nextPlayer() {
		gameService.nextPlayer();
		resume();
	}

	@Override
	public void update(Observable observed, Object text) {
		this.gameLogPanel.update((String) text);
		mainFrame.repaint();
	}

	protected void showInfoDialog(String text) {
		final JDialog dialog = new JDialog();
		dialog.setTitle(text);
		dialog.setBounds(INFO_DIALOG_BOUNDS);
		dialog.setLayout(null);
		dialog.setVisible(true);
		dialog.addWindowListener(new WindowAdapter() {

			@Override
			public void windowClosing(WindowEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
				resume();
			}
		});
		JLabel label = new JLabel(text);
		label.setBounds(20, 20, 240, 30);
		dialog.add(label);
		JButton button = new JButton("Capisco, jefe");
		button.setBounds(100, 160, 200, 30);
		dialog.add(button);
		button.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
				resume();
			}

		});
		pause();
		dialog.setModal(true);
	}

}

/**
 * Game flow 0 Previous phase 0.1.1 Player 1, Management phase (buy / upgrade) 0.1.n Player n, Management phase (buy / upgrade) 0.2.1 Player
 * 1, Deploy phase 0.2.n Player n, Deploy phase 1 Turn 1.1 Weather forecast and soil state change 1.2 Initiative determination 1.3.1 Player
 * 1 turn activation / pass 1.3.n Player n turn activation / pass
 * 
 */

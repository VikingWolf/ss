package org.orion.ss.test;

import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;

import org.orion.ss.model.impl.Game;
import org.orion.ss.model.impl.Player;
import org.orion.ss.service.GameService;
import org.orion.ss.service.ManagementService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GraphicTest {

	protected final static Logger logger = LoggerFactory.getLogger(Test.class);
	
	private final static Rectangle WINDOW_BOUNDS = new Rectangle(0, 0, 1370, 500);
	private final static Rectangle INFO_DIALOG_BOUNDS = new Rectangle(100, 100, 400, 250);
	private final static int LEFT_MARGIN = 10;
	private final static int RIGHT_MARGIN = 10;
	private final static int TOP_MARGIN = 10;
	private final static int BOTTOM_MARGIN = 10;
	private final static int LATERAL_SWING_MARGIN = 20;
	private final static String VERSION = "0.1";
	
	private Game game;
	private JFrame mainFrame;
	
	private GameService gameService;
	private ManagementService managementService;
	
	/* GUI components */
	private GameLogPanel gameLogPanel;

	
	public final static void main(String[] args){
		GraphicTest test = new GraphicTest();
		logger.info("Starting test at " + new Date());
		test.prepareGame();
		test.mountGUI();
		test.start();		
	}
	
	protected void start(){
		gameService.managementPhase(managementService.getSuitablePlayers());
	}
	
	protected void prepareGame(){
		game = new GameSample().buildGame();
		gameService = new GameService(game);
		managementService = new ManagementService(game);
	}
	
	protected void mountGUI(){
		mainFrame = new JFrame("Graphic Tester v" + VERSION);
		mainFrame.setBounds(WINDOW_BOUNDS);
		mainFrame.getContentPane().setLayout(null);
		JTabbedPane mainPane = new JTabbedPane();
		mainPane.setBounds(WINDOW_BOUNDS);
		mainFrame.getContentPane().add(mainPane);
		gameLogPanel = new GameLogPanel();
		mainPane.addTab("Game Log", gameLogPanel);
		for (Player player : game.getPlayers()){
			PlayerPanel panel = new PlayerPanel();
			panel.setBounds(WINDOW_BOUNDS);
			panel.setLayout(null);
			mainPane.addTab(player.getEmail(), panel);
		}
		mainFrame.addWindowListener(new WindowAdapter(){
			@Override
			public void windowClosing(WindowEvent e) {
				logger.info("Ending test at " + new Date());
				System.exit(0);
				super.windowClosing(e);
			}
		});
		gameLogPanel.addLog("Welcome to SS Game Tester v" + VERSION);
		gameLogPanel.addLog("Game prepared: " + game.getId());
		gameLogPanel.addLog("Game services prepared.");
		gameLogPanel.addSeparator();
		gameLogPanel.display(game);
		gameLogPanel.addSeparator();
		mainFrame.setVisible(true);
		showInfoDialog("The Game is prepared.");
	}
	
	protected void showInfoDialog(String text){
		final JDialog dialog = new JDialog();
		dialog.setTitle(text);
		dialog.setBounds(INFO_DIALOG_BOUNDS);
		dialog.setLayout(null);
		dialog.setVisible(true);
		dialog.addWindowListener(new WindowAdapter(){
			
			@Override
			public void windowClosing(WindowEvent e) {
				dialog.setVisible(false);
				dialog.dispose();
			}
		});
		JLabel label = new JLabel(text);
		label.setBounds(20, 20, 240, 30);
		dialog.add(label);
		JButton button = new JButton("Capisco, jefe");
		button.setBounds(100, 160, 200, 30);
		dialog.add(button);
		button.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setVisible(false);
				dialog.dispose();				
			}
			
		});

	}	
	
	class PlayerPanel extends JPanel {
		
	}

	class GameLogPanel extends JPanel {
		
		private JTextArea textArea;
		private final static int TEXT_AREA_HEIGHT = 300;
		
		public GameLogPanel(){
			super();
			setLayout(null);
			setBounds(WINDOW_BOUNDS);
			textArea = new JTextArea();
			textArea.setEditable(false);
			JScrollPane scrollPane = new JScrollPane(textArea);
			scrollPane.setBounds(GraphicTest.LEFT_MARGIN, GraphicTest.TOP_MARGIN, this.getWidth() - GraphicTest.LATERAL_SWING_MARGIN - GraphicTest.LEFT_MARGIN - GraphicTest.RIGHT_MARGIN, TEXT_AREA_HEIGHT);
			add(scrollPane);
		}
		
		public void addLog(String log){
			textArea.append(log + "\n");
		}
		
		public void addSeparator(){
			textArea.append("===============================================================================================================================================\n");
		}
		
		public void display(Game game){
			textArea.append("turn=" + game.getTurn() + "\n");
			textArea.append("date=" + game.getDate() + "\n");
			textArea.append("turn duration=" + game.getSettings().getTurnDuration() + "\n");
			textArea.append("hex side=" + game.getSettings().getHexSide() + "\n");
			textArea.append("stack limit=" + game.getSettings().getStackLimit() + "\n");
			textArea.append("turn limit=" + game.getSettings().getTimeLimit() + "\n");
			textArea.append("turn limit margin=" + game.getSettings().getTimeMargin() + "\n");
			textArea.append("market=\n" + game.getMarket());
		}
		
	}
}

/** Game flow
 * 		0 		Previous phase
 * 			0.1.1		Player 1, Management phase (buy / upgrade)
 * 			0.1.n		Player n, Management phase (buy / upgrade)
 * 			0.2.1		Player 1, Deploy phase
 * 			0.2.n		Player n, Deploy phase 
 * 		1		Turn
 * 			1.1		Weather forecast and soil state change
 * 			1.2		Initiative determination
 * 			1.3.1	Player 1 turn activation / pass
 * 			1.3.n	Player n turn activation / pass		
 *			
 */

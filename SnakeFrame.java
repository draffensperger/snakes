import java.io.*;
import java.beans.*;
import javax.imageio.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class SnakeFrame extends Frame implements Observer {
	private static final String DEFAULT_GAME_FILE = "game0.xml";

	private Label highScoreLabel;
	private Label highScoreMessage;
	private Label snakeLivesLabels[];
	private Label snakeScoreLabels[];
	private Label scoreLabelers[];
	private Label livesLabelers[];
	private Displayer displayer;
	private Game game;
	private String gameFile;
	private ArrayList snakes;
	private Panel infoPanel;
	private Panel otherInfo;
	private int boxWidth;
	private int boxHeight;
		
	public static void main(String args[]) {	
		if (args.length > 0) {	
			new SnakeFrame(args[0]);
		} else {
			new SnakeFrame(DEFAULT_GAME_FILE);	
		}
	}
	public SnakeFrame(String gameFile) {
		this.gameFile = gameFile;
		setBackground(Color.lightGray);				
					
		displayer = new Displayer();
		displayer.setBackground(getBackground());
		add(displayer, "Center");				
		
		//saveGame();
		newGame();
		
		addWindowListener(new ReflectionListener(this, "quit"));
						
		setTitle("Snakes");		
		addKeyListener(new KeyController());		
		
		infoPanel = new Panel();
		infoPanel.setLayout(new BorderLayout());
		Panel scorePanel = new Panel();
		scorePanel.add(new Label("High Score"));						
		highScoreLabel = new Label("5000");
		highScoreMessage = new Label("DaveMan");
		scorePanel.add(highScoreLabel);
		scorePanel.add(highScoreMessage);
		infoPanel.add(scorePanel, "North");
	
		updateSnakesArray();
		
		otherInfo = new Panel();
		otherInfo.setLayout(new GridLayout(2, snakes.size() * 2));
		snakeLivesLabels = new Label[snakes.size()];
		snakeScoreLabels = new Label[snakes.size()];
		livesLabelers = new Label[snakes.size()];
		scoreLabelers = new Label[snakes.size()];
		for (int i = 0; i < snakes.size(); i++) {
			livesLabelers[i] = new Label();
			otherInfo.add(livesLabelers[i]);
			snakeLivesLabels[i] = new Label();
			otherInfo.add(snakeLivesLabels[i]);			
		}
		for (int i = 0; i < snakes.size(); i++) {
			scoreLabelers[i] = new Label();
			otherInfo.add(scoreLabelers[i]);
			snakeScoreLabels[i] = new Label();
			otherInfo.add(snakeScoreLabels[i]);			
		}						
		infoPanel.add(otherInfo, "Center");
		
		add(infoPanel, "North");		
		add(new Label("F2 starts a new game"), "South");
		setSize(getToolkit().getScreenSize());
		updateScoreBoard();						
		setVisible(true);
		
		requestFocus();
		game.setBoxHeight(displayer.getHeight() / game.getFieldHeight());
		game.setBoxWidth(displayer.getWidth() / game.getFieldWidth());
		game.startGame();
		
		displayer.repaint();
	}
        public void saveGame() {
		try {
			Game game = GameMaker.makeTwoPlayerGame();
			FileOutputStream os = new FileOutputStream(gameFile);			
			XMLEncoder encoder = new XMLEncoder(os);			
			encoder.writeObject(game);
			encoder.flush();
			encoder.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
        public void newGame() {
		game = loadGame();
		game.setDisplayer(displayer);
		game.setKeyInputComponent(this);
		game.setGameObjectsObserver(this);		
		game.initGame();		
	}
	public Game loadGame() {
		try {
			Game game = null;
			FileInputStream is = new FileInputStream(gameFile);			
			XMLDecoder decoder = new XMLDecoder(is);
			game = (Game) decoder.readObject();
			decoder.close();			
			is.close();
			return game;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	public void quit() {
		System.exit(0);
	}		
	private void updateSnakesArray() {
		Iterator iterator = game.getGameObjectsIterator();
		snakes = new ArrayList();
		while (iterator.hasNext()) {
			GameObject obj = (GameObject) iterator.next();
			if (obj instanceof Snake) {
				snakes.add(obj);
			}
		}
	}	
	private void updateScoreBoard() {					
		updateSnakesArray();
		
		for (int i = 0; i < snakes.size(); i++) {
			Snake s = (Snake) snakes.get(i);
			scoreLabelers[i].setText(s.getName() + " Score:");
			livesLabelers[i].setText(s.getName() + " Lives:");
			snakeScoreLabels[i].setText(String.valueOf(s.getScore()));
			snakeLivesLabels[i].setText(String.valueOf(s.getLives()));			
		}		
	}
	public void update(Observable o, Object obj) {		
		updateScoreBoard();
	}
	public void update(Graphics g) {
		paint(g);
	}
	private class Displayer extends Component {
		public void paint(Graphics g) {
			g.drawImage(game.getField(), 0, 0, game.getTotalWidth(), game.getTotalHeight(), this);
		}		
		public Dimension getPreferredSize() {
			return new Dimension(game.getTotalWidth(), game.getTotalHeight());
		}
	}
	private class KeyController extends KeyAdapter {		
		public void keyPressed(KeyEvent evt) {			
			if (evt.getKeyCode() == KeyEvent.VK_F2) {	
				game.startGame();
				updateScoreBoard();
			}
		}
	}
}

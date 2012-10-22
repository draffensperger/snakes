import java.util.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.*;
import java.applet.*;

public class Game implements KeyListener {		
	private Timer simulator;
	private static final int SIMULATION_START_DELAY = 100;
	
	private AudioClip advancesLevelClip;		
	private AudioClip backgroundMusic;	
		
	private int simulationPeriod;		
	private int level;
	private int fieldWidth;
	private int fieldHeight;
	private int boxWidth;
	private int boxHeight;
	private Paint bgPaint;
	private Collection gameObjects;
	private Collection originalObjects;

	private long longestObstruct;
	private long timeForSnakes;
	private long timeForGoodies;
	private long timeForWalls;
	private Graphics2D fieldGraphics;
	private boolean startNewGame;	
	private boolean repaintImmediately;
	private boolean shouldSimulate;
	private GameObject[][] fieldData;
	private BufferedImage field;
	private TimerTask simulateTask;
	private Observer gameObjectsObserver;	
	private Component displayer;
	private Component keyInputComponent;	
	
	public Game() {
		shouldSimulate = false;
		startNewGame = false;				
		simulator = new Timer();
	}
	public BufferedImage getField() {
		return field;
	}	
	public void setSimulationPeriod(int val) {simulationPeriod = val;}
	public int getSimulationPeriod() {return simulationPeriod;}
	public void setFieldWidth(int width) {
		fieldWidth = width;
		allocateField();
	}
	public void setBoxWidth(int width) {
		boxWidth = width;
		allocateField();
	}
	public void setBoxHeight(int height) {
		boxHeight = height;
		allocateField();
	}
	public void setBackgroundPaint(Paint bgPaint) {
		this.bgPaint = bgPaint;
	}
	public Paint getBackgroundPaint() {
		return bgPaint;
	}
	public int getTotalWidth() {
		return fieldWidth * boxWidth;
	}
	public int getTotalHeight() {
		return fieldHeight * boxHeight;
	}
	public int getBoxWidth() {		
		return boxWidth;
	}
	public int getBoxHeight() {
		return boxHeight;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		if (simulateTask != null) {
			// This is done in case the setLevel() method takes a long time,
			// in which case, other tasks will not "pile up."
			try {
				simulateTask.cancel();
			} catch (Exception e) {
				e.printStackTrace();
			}			
		}
		
		shouldSimulate = false;
		
		timeForGoodies = 0;
		timeForSnakes = 0;
		timeForWalls = 0;
		longestObstruct = 0;
		repaintImmediately = false;
		long start = System.currentTimeMillis();
		System.out.println("In set level " + (System.currentTimeMillis() - start));
		boolean originalShouldSimulate = shouldSimulate;
				
		if (advancesLevelClip != null) {
			//advancesLevelClip.play();
		}		
		System.out.println("Played sound " + (System.currentTimeMillis() - start));
		this.level = level;
		clearField();
		System.out.println("Cleared field " + (System.currentTimeMillis() - start));
		Iterator i = gameObjects.iterator();
		while (i.hasNext()) {			
			((GameObject) i.next()).levelAdvanced(level);
		}
		System.out.println("Iterated over objects " + (System.currentTimeMillis() - start));
		
		displayer.repaint();		
		shouldSimulate = originalShouldSimulate;
		System.out.println("Repainted " + (System.currentTimeMillis() - start));
		repaintImmediately = true;
		
		System.out.println("Time for goodies = " + timeForGoodies + ", walls " +
				timeForWalls + ", snakes " + timeForSnakes + ", longest call " + longestObstruct);		 
		
		shouldSimulate = true;
		simulateTask = new Simulate();
		
		try {
			simulator.schedule(simulateTask, 0, simulationPeriod);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void setGameObjects(Collection objs) {
		if (gameObjects != null) {
			gameObjects.clear();
		}
		Iterator i = objs.iterator();
		while (i.hasNext()) {
			addGameObject((GameObject) i.next());
		}		
	}	
	public Collection getGameObjects() {
		return gameObjects;
	}

	public Iterator getGameObjectsIterator() {
		return gameObjects.iterator();				
	}
	
	public void setGameObjectsObserver(Observer o) {
		gameObjectsObserver = o;
		Iterator i = gameObjects.iterator();
		while (i.hasNext()) {
			((GameObject) i.next()).addObserver(o);
		}
	}
	public boolean isSimulationRunning() {
		return shouldSimulate;
	}
	public void setDisplayer(Component displayer) {
		this.displayer = displayer;
	}
	public void setKeyInputComponent(Component keyInputComponent) {
		this.keyInputComponent = keyInputComponent;
		keyInputComponent.addKeyListener(this);	
	}
	public void setAdvancesLevelSound(AudioClip clip) {
		advancesLevelClip = clip;
	}
	public AudioClip getAdvancesLevelSound() {
		return advancesLevelClip;
	}
	public void setBackgroundMusic(AudioClip clip) {
		backgroundMusic = clip;
	}
	public AudioClip getBackgroundMusic() {
		return backgroundMusic;
	}
	
	private void allocateField() {
		if (fieldWidth > 0 && boxWidth > 0 && fieldHeight > 0 && boxHeight > 0) {
			fieldData = new GameObject[fieldWidth][fieldHeight];
			field = new BufferedImage(getTotalWidth(), getTotalHeight(), BufferedImage.TYPE_INT_RGB);			
			fieldGraphics = (Graphics2D) field.createGraphics();
		}
	}
	
	public void initGame() {		
		if (backgroundMusic != null) {
			backgroundMusic.loop();			
		}		
		shouldSimulate = false;		
		try {
			originalObjects = cloneAll(gameObjects);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public void startGame() {		
		shouldSimulate = false;
		gameObjects = cloneAll(originalObjects);		
		setLevel(1);
		shouldSimulate = true;
	}	
	
	public void setFieldHeight(int height) {
		fieldHeight = height;
		allocateField();
	}	
	public void clearField() {						
		for (int i = 0; i < fieldWidth; i++) {
			for (int j = 0; j < fieldHeight; j++) {
				fieldData[i][j] = null;
			}
		}				
		
		fieldGraphics.setPaint(bgPaint);
		fieldGraphics.fillRect(0, 0, getTotalWidth(), getTotalHeight());				
	}
	public boolean isInBounds(int x, int y) {
		return x >= 0 && y >= 0 && x < fieldData.length && y < fieldData[0].length;
	}	
	public void clear(int x, int y) {
		obstruct(x, y, null);
	}
	public int getFieldWidth() {
		return fieldWidth;
	}
	public int getFieldHeight() {
		return fieldHeight;
	}
	public GameObject getObjectAt(int x, int y) {
		if (!isInBounds(x, y)) {
			return null;
		} else {
			return fieldData[x][y];
		}
	}
	private void ensureGameObjectsConstructed() {
		if (gameObjects == null) {
			gameObjects = new ArrayList();
		}
	}	
	public void addGameObject(GameObject obj) {	
		ensureGameObjectsConstructed();		
				
		if (!gameObjects.contains(obj)) {
			gameObjects.add(obj);
			obj.setParent(this);
			if (gameObjectsObserver != null) {
				obj.addObserver(gameObjectsObserver);			
			}
		}
	}
	public void obstruct(int x, int y, GameObject obj) {						
		if (isInBounds(x, y)) {
			if (fieldData[x][y] != obj) {
				fieldData[x][y] = obj;			
				if (obj == null) {
					fieldGraphics.setPaint(bgPaint);
					fieldGraphics.fillRect(x * boxWidth, y * boxHeight, boxWidth, boxHeight);
				} else {				
					long start = System.currentTimeMillis();					
					obj.paintSquare(x * boxWidth, y * boxHeight, boxWidth, boxHeight, fieldGraphics);
					long elapsed = System.currentTimeMillis() - start;
					if (elapsed > longestObstruct) {
						longestObstruct = elapsed;
					}
					if (obj instanceof Snake) {
						timeForSnakes += elapsed;
					} else if (obj instanceof Wall) {
						timeForWalls += elapsed;
					} else if (obj instanceof Goodie) {
						timeForGoodies += elapsed;
					}
				}
				if (displayer != null && repaintImmediately) {
					displayer.repaint(x * boxWidth, y * boxHeight, boxWidth, boxHeight);		
				}
			}
		}		
	}		
	
	public void keyPressed(KeyEvent e) {
		if (shouldSimulate) {
			Iterator i = gameObjects.iterator();				
			while (i.hasNext()) {
				if (shouldSimulate) {
					((GameObject) i.next()).keyPressed(e);
				}
			}
		}
	}	
	public void keyReleased(KeyEvent e) {
		if (shouldSimulate) {
			Iterator i = gameObjects.iterator();				
			while (i.hasNext()) {
				if (shouldSimulate) {
					((GameObject) i.next()).keyReleased(e);
				}
			}
		}
	}	
	public void keyTyped(KeyEvent e) {
		if (shouldSimulate) {
			Iterator i = gameObjects.iterator();				
			while (i.hasNext()) {
				if (shouldSimulate) {
					((GameObject) i.next()).keyTyped(e);
				}
			}		
		}
	}
	
	private Collection cloneAll(Collection c) {
		ArrayList clone = new ArrayList(c.size());
		Iterator i = c.iterator();
		while (i.hasNext()) {
			clone.add(((GameObject) i.next()).clone());
		}
		return clone;
	}
	
	private class Simulate extends TimerTask {
		public void run() {
			if (shouldSimulate) {
				Iterator i = gameObjects.iterator();				
				while (i.hasNext()) {
					if (shouldSimulate) {
						((GameObject) i.next()).simulate();
					}
				}			
			}			
		}		
	}
}

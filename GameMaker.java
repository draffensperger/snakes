import java.io.*;
import java.beans.*;
import javax.imageio.*;
import java.awt.*;
import java.applet.*;
import java.awt.event.*;
import java.util.*;
import java.awt.image.*;

public class GameMaker {	
	private static Paint charPaint;	
	
	private static Paint[] snakePaints = {
		new GradientBean(0.0f, 0.0f, Color.green.darker(), 50.0f, 50.0f, Color.green, true),
		new GradientBean(0.0f, 0.0f, Color.blue.darker(), 50.0f, 50.0f, Color.blue, true),
		new GradientBean(0.0f, 0.0f, Color.orange.darker(), 50.0f, 50.0f, Color.orange, true),
	};
	private static String[] snakeNames = {"Green", "Blue", "AI"};
	
	public static Game makeTwoPlayerGame() {				
		AudioClip snakeDiesClip = null;
		AudioClip snakeEatsClip = null;
		Game game = new Game();
		
		game.setSimulationPeriod(50);
		
		try { 
			game.setAdvancesLevelSound(new AudioBean("C:/Documents and Settings/Dave/Documents/programming/snakes/nextlevel.wav"));
			snakeDiesClip = new AudioBean("C:/Documents and Settings/Dave/Documents/programming/snakes/dies.wav");
			snakeEatsClip = new AudioBean("C:/Documents and Settings/Dave/Documents/programming/snakes/eats.wav");
			game.setBackgroundMusic(new AudioBean("C:/Documents and Settings/Dave/Documents/programming/snakes/bgmusic.mid"));
		} catch (Exception e) {			
			e.printStackTrace();
		}				
				
		game.setFieldWidth(75);
		game.setFieldHeight(60);
		game.setBoxWidth(10);
		game.setBoxHeight(10);
		
		try {
			game.setBackgroundPaint(new TextureBean("C:/Documents and Settings/Dave/Documents/programming/snakes/brownFloor.jpg", 
					new Rectangle(0, 0, game.getTotalWidth(), game.getTotalHeight())));
		} catch (Exception e) {
			e.printStackTrace();
			game.setBackgroundPaint(Color.cyan);
		}
		
		LineWall walls = new LineWall();
		walls.setIncludeExternalWalls(true);
		walls.addLine(1, new IntLine(1, 1, 20, 30));
		walls.addLine(2, new IntLine(20, 30, 50, 50));
		walls.addLine(3, new IntLine(10, 10, 40, 45));
		walls.addLine(3, new IntLine(20, 20, 20, 45));
		walls.addLine(3, new IntLine(10, 20, 20, 20));
		walls.setPaint(new GradientBean(0.0f, 0.0f, Color.red, 400.0f, 400.0f, Color.red.darker(), true));				
		walls.setParent(game);
		game.addGameObject(walls);
				
		Snake[] snakes = new Snake[snakeNames.length];		
		for (int i = 0; i < snakeNames.length - 1; i++) {
			snakes[i] = new HumanSnake();
			snakes[i].setParent(game);
			snakes[i].setName(snakeNames[i]);
			snakes[i].setBuffer(5);
			snakes[i].setLives(5);
			snakes[i].setStartLength(3);
			snakes[i].setFadeDuration(10);
			snakes[i].setUnfadedPaint(snakePaints[i]);
			snakes[i].setMaxLength(200);
			snakes[i].setEatsSound(snakeEatsClip);
			snakes[i].setDiesSound(snakeDiesClip);			
			game.addGameObject(snakes[i]);
		}
		
		HumanSnake h1 = (HumanSnake) snakes[0];
		h1.setUpKeyCode(KeyEvent.VK_UP);
		h1.setDownKeyCode(KeyEvent.VK_DOWN);
		h1.setLeftKeyCode(KeyEvent.VK_LEFT);
		h1.setRightKeyCode(KeyEvent.VK_RIGHT);
		
		HumanSnake h2 = (HumanSnake) snakes[1];
		h2.setUpKeyCode(KeyEvent.VK_W);
		h2.setDownKeyCode(KeyEvent.VK_S);
		h2.setLeftKeyCode(KeyEvent.VK_A);
		h2.setRightKeyCode(KeyEvent.VK_D);
		
		snakes[2] = new AISnake();
		snakes[2].setStartLength(3);
		snakes[2].setParent(game);
		snakes[2].setUnfadedPaint(snakePaints[2]);
		snakes[2].setName(snakeNames[2]);
		snakes[2].setFadeDuration(10);
		snakes[2].setMaxLength(200);
		snakes[2].setLives(5);
		snakes[2].setEatsSound(snakeEatsClip);
		snakes[2].setDiesSound(snakeDiesClip);
		game.addGameObject(snakes[2]);
		
		Goodie goodie = new Goodie("1");
		goodie.setPaint(new GradientBean(0.0f, 0.0f, Color.yellow.brighter(), 8.0f, 8.0f, Color.yellow, true));
		goodie.setParent(game);
		game.addGameObject(goodie);
		
		return game;
	}
}

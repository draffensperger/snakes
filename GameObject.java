import java.awt.*;
import java.awt.event.*;
import java.util.*;

public class GameObject extends Observable implements KeyListener, Cloneable {	
	private Game parent;
	private String name;
	private Paint paint;
	
	public void setParent(Game val) {
		parent = val;		
	}
	public Game getParent() {
		return parent;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}	
	public void setPaint(Paint paint) {
		this.paint = paint;
	}
	public Paint getPaint() {
		return paint;
	}
	
	public void paintSquare(int x, int y, int width, int height, Graphics2D g2d) {						
		if (g2d.getPaint() != paint) {
			g2d.setPaint(paint);
		}
		g2d.fillRect(x, y, width, height);		
	}
	public void levelAdvanced(int level) {
	}
	public void simulate() {
	}
	
	public void keyPressed(KeyEvent e) {
	}	
	public void keyReleased(KeyEvent e) {
	}	
	public void keyTyped(KeyEvent e) {
	}
	public Object clone() {
		try {
			return (GameObject) super.clone();
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
			return null;
		}
	}
}

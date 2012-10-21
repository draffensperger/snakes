import java.awt.event.*;

public class HumanSnake extends Snake {
	private int leftKeyCode;
	private int rightKeyCode;
	private int upKeyCode;
	private int downKeyCode;
	
	public HumanSnake() {
	}
	public void setUpKeyCode(int code) {
		this.upKeyCode = code;
	}
	public void setDownKeyCode(int code) {
		this.downKeyCode = code;
	}
	public void setLeftKeyCode(int code) {
		this.leftKeyCode = code;
	}
	public void setRightKeyCode(int code) {
		this.rightKeyCode = code;
	}
	public int getRightKeyCode() {
		return rightKeyCode;
	}
	public int getLeftKeyCode() {
		return leftKeyCode;
	}
	public int getUpKeyCode() {
		return upKeyCode;
	}
	public int getDownKeyCode() {
		return downKeyCode;
	}
	public void keyPressed(KeyEvent evt) {	
		if (getParent().isSimulationRunning()) {
			if (evt.getKeyCode() == leftKeyCode) setDirection(LEFT);
			else if (evt.getKeyCode() == rightKeyCode) setDirection(RIGHT);
			else if (evt.getKeyCode() == downKeyCode) setDirection(DOWN);
			else if (evt.getKeyCode() == upKeyCode) setDirection(UP);
		}
	}
}

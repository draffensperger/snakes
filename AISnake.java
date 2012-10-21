import java.applet.*;
import java.awt.*;
import java.util.*;

public class AISnake extends Snake {			
	private int[] dirValues = new int[NUM_DIRS];
	private int upDownBias = NO_DIR;
	private int leftRightBias = NO_DIR;
	public synchronized void simulate() {				
		Goodie goodie = findGoodie();
		if (goodie != null) {
			int goalX = goodie.getX();
			int goalY = goodie.getY();						
			
			for (int dir = RIGHT; dir <= DOWN; dir++) {
				dirValues[dir] = 0;
			}
			
			for (int dir = RIGHT; dir <= DOWN; dir++) {
				int curDist = distance(getHeadX(), getHeadY(), goalX, goalY);
				int dirDist = distance (getHeadX() + xComp(dir), getHeadY() + yComp(dir), goalX, goalY);												
				if (dir != inverse(getDirection())) {				
					if (isClear(dir)) {
						dirValues[dir] += 16;
					}
					if (dirDist < curDist) {
						dirValues[dir] += 4;
					}					
				}
			}
			if (dirValues[UP] == dirValues[DOWN]) {
				if (upDownBias == NO_DIR) {
					if (Math.random() > 0.5) {
						upDownBias = UP;
					} else {
						upDownBias = DOWN;
					}
				} else {
					dirValues[upDownBias] += 1;
				}
			} else {
				upDownBias = NO_DIR;
			}
			if (dirValues[LEFT] == dirValues[RIGHT]) {
				if (leftRightBias == NO_DIR) {
					if (Math.random() > 0.5) {
						leftRightBias = LEFT;
					} else {
						leftRightBias = RIGHT;
					}
				} else {
					dirValues[leftRightBias] += 1;
				}
			} else {
				leftRightBias = NO_DIR;
			}
			int bestDir = getDirection();
			for (int dir = RIGHT; dir <= DOWN; dir++) {
				if (dirValues[dir] > dirValues[bestDir]) {
					bestDir = dir;					
				} //else if (dirValues[dir] == dirValues[getDirection()] && Math.random() < 0.5) {
					//setDirection(dir);
				//}
			}
			setDirection(bestDir);
		}
		super.simulate();
	}
	private Goodie findGoodie() {		
		GameObject curObj;
		Iterator gameObjs = getParent().getGameObjectsIterator();
		while(gameObjs.hasNext()) {
			curObj = (GameObject) gameObjs.next();
			if (curObj instanceof Goodie) {
				return (Goodie) curObj;				
			}
		}
		return null;
	}
	private static int distance(int x1, int y1, int x2, int y2) {
		return Math.abs(x1 - x2) + Math.abs(y1 - y2);
	}	
	private boolean isClear(int dir) {
		GameObject soonToHit = getParent().getObjectAt(getHeadX() + xComp(dir), 
				getHeadY() + yComp(dir));
		return soonToHit == null || soonToHit instanceof Goodie || 
			(soonToHit instanceof Snake && soonToHit != this);
	}
}
